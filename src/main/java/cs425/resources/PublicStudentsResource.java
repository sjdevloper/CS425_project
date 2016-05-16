package cs425.resources;

import java.util.Collection;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.skife.jdbi.v2.DBI;
import cs425.api.Student;
import cs425.jdbi.StudentDAO;
import cs425.jdbi.TakeDAO;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/public-student")
@Produces(MediaType.APPLICATION_JSON)
public class PublicStudentsResource {
	public PublicStudentsResource(DBI jdbi){
		this.dao = jdbi.onDemand(StudentDAO.class);
		this.takeDao = jdbi.onDemand(TakeDAO.class);
	}

	@GET 
	@CacheControl(noCache = true)
	public Collection<Student> getAll() {
		Collection<Student> students = dao.selectAll(); 
		for (Student student: students)
		{
			if (!student.getPublicEmail().equals("yes"))
			{	
				student.setEmail("hidden");
			}
			if (!student.getPublicYearBegan().equals("yes"))
			{
				student.setYearBegan(-1);
			}
			if (!student.getPublicSemesterBegan().equals("yes"))
			{
				student.setSemesterBegan("hidden");
			}
			if (!student.getPublicDegreeStatus().equals("yes"))
			{
				student.setDegreeStatus("hidden");
			}
			if (!student.getPublicDegreeType().equals("yes"))
			{
				student.setDegreeType("hidden");
			}
			
			if (student.getPublicGPA().equals("yes"))
			{
				student.setGpa(StudentsResource.calculateGPA(
					student.getUsername(), takeDao));
			}
			else
			{
				student.setGpa(-1);
			}
		}
		return students;
	}
	
	private final StudentDAO dao;
	private final TakeDAO takeDao;
}
