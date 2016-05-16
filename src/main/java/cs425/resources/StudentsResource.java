package cs425.resources;

import java.net.URI;
import java.util.Collection;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotEmpty;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs425.api.CourseGPA;
import cs425.api.Student;
import cs425.auth.Account;
import cs425.jdbi.StudentDAO;
import cs425.jdbi.TakeDAO;
import cs425.jdbi.UserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/student")
@Produces(MediaType.APPLICATION_JSON)
public class StudentsResource {
	public StudentsResource(DBI jdbi){
		this.dao = jdbi.onDemand(StudentDAO.class);
		this.userdao = jdbi.onDemand(UserDAO.class);
		this.takeDao = jdbi.onDemand(TakeDAO.class);
	}

	@RolesAllowed("ADMIN")
	@GET 
	@CacheControl(noCache = true)
	public Collection<Student> getAll(){
		Collection<Student> students = dao.selectAll();
		
		for (Student student: students)
			student.setGpa(calculateGPA(student.getUsername(), takeDao));
		
		return students;
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
			@FormParam("username") @NotEmpty String username,
			@FormParam("name") @NotEmpty String name,
			@FormParam("password") @NotEmpty String password,
			@FormParam("email") @NotEmpty String email,
			@FormParam("yearBegan") Integer yearBegan,
			@FormParam("semesterBegan") @NotEmpty String semesterBegan,
			@FormParam("degreeStatus") @NotEmpty String degreeStatus,
			@FormParam("degreeType") @NotEmpty String degreeType,
			@FormParam("publicEmail") @NotEmpty String publicEmail,
			@FormParam("publicYearBegan") @NotEmpty String publicYearBegan,
			@FormParam("publicSemesterBegan") @NotEmpty String publicSemesterBegan,
			@FormParam("publicDegreeStatus") @NotEmpty String publicDegreeStatus,
			@FormParam("publicDegreeType") @NotEmpty String publicDegreeType,
			@FormParam("publicGPA") @NotEmpty String publicGPA)
	{
		logger.info("add {}: ({},{},{},{})",
			username, yearBegan, semesterBegan,
			degreeStatus, degreeType);
		
		Student s = new Student(username, name, password, email,
				yearBegan, semesterBegan, degreeStatus, degreeType, 
				publicEmail,publicYearBegan,publicSemesterBegan,
				publicDegreeStatus,publicDegreeType, publicGPA);
		
		userdao.insert(s);
		dao.insert(s);
		
		URI self = uri.getAbsolutePathBuilder().path(username).build();
			
		return Response.created(self).build();
	}
	
	@RolesAllowed("USER")
	@DELETE @Path("/{username}")
	public Response delete(
		@Auth Account account,
		@PathParam("username") @NotEmpty String username)
	{
		if (account.getName().equals(username) || account.isUserInRole("ADMIN"))
		{
			logger.info("delete {}", username);
			
			dao.delete(username);
			
			return Response.ok().build();
		}
		else
			return Response.status(Response.Status.UNAUTHORIZED).build();
    }
	
	@RolesAllowed("USER")
	@GET @Path("/{username}")
	@CacheControl(noCache = true)
	public Student get(
		@Auth Account account,
		@PathParam("username") @NotEmpty String username)
	{
		if (account.getName().equals(username) || account.isUserInRole("ADMIN"))
		{
			logger.info("get {}", username);
			
			Student student = dao.select(username);
			
			student.setGpa(calculateGPA(username, takeDao));
			
			return student;
		}
		else
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
	
	@RolesAllowed("USER")
	@PUT @Path("/{username}")
	public Response update(
			@Auth Account account,
			@PathParam("username") @NotEmpty String username,
			@FormParam("name") @NotEmpty String name,
			@FormParam("password") @NotEmpty String password,
			@FormParam("email") @NotEmpty String email,
			@FormParam("yearBegan") Integer yearBegan,
			@FormParam("semesterBegan") @NotEmpty String semesterBegan,
			@FormParam("degreeStatus") @NotEmpty String degreeStatus,
			@FormParam("degreeType") @NotEmpty String degreeType,
			@FormParam("publicEmail") @NotEmpty String publicEmail,
			@FormParam("publicYearBegan") @NotEmpty String publicYearBegan,
			@FormParam("publicSemesterBegan") @NotEmpty String publicSemesterBegan,
			@FormParam("publicDegreeStatus") @NotEmpty String publicDegreeStatus,
			@FormParam("publicDegreeType") @NotEmpty String publicDegreeType,
			@FormParam("publicGPA") @NotEmpty String publicGPA)
	{
		if (account.getName().equals(username) || account.isUserInRole("ADMIN"))
		{
			logger.info("update {}: ({},{},{},{})",
					username, yearBegan, semesterBegan,
					degreeStatus, degreeType);
		
			Student s = new Student(username, name, password, email,
					yearBegan, semesterBegan, degreeStatus, degreeType, 
					publicEmail,publicYearBegan,publicSemesterBegan,
					publicDegreeStatus,publicDegreeType, publicGPA);
			
			userdao.update(s);
			dao.update(s);
			
			URI self = uri.getAbsolutePathBuilder().path(username).build();
			
			return Response.created(self).build();
		}
		else
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);	
	}
	
	public static double calculateGPA(String student, TakeDAO takeDao)
	{
		Collection<CourseGPA> gpas = takeDao.selectAllGradebystudent(student);
		
		int total_hours = 0;
		int total_gpa = 0;
		for (CourseGPA gpa: gpas)
		{
			total_hours += gpa.getCreditHour();
			if (gpa.getGrade().equals("A"))
				total_gpa += 4*gpa.getCreditHour();
			else if (gpa.getGrade().equals("B"))
				total_gpa += 3*gpa.getCreditHour();
			else if (gpa.getGrade().equals("C"))
				total_gpa += 2*gpa.getCreditHour();
			else if (gpa.getGrade().equals("D"))
				total_gpa += 1*gpa.getCreditHour();

			logger.debug("GPA {}: {}, {}, {}, {}",
				student, gpa.getGrade(), gpa.getCreditHour(),
				total_gpa, total_hours);
		}
		
		if (total_hours == 0)
			return 0;
		else
			return ((double)(total_gpa*100/total_hours))/100;
	}
	
	private final StudentDAO dao; 
	private final UserDAO userdao;
	private final TakeDAO takeDao;
	
    @Context
    UriInfo uri;

	private static final Logger logger
		= LoggerFactory.getLogger(StudentsResource.class);		
}
