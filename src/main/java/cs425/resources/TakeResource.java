/**
 * 
 */
package cs425.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotEmpty;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs425.api.InstructorGPA;
import cs425.api.Take;
import cs425.jdbi.TakeDAO;
import io.dropwizard.jersey.caching.CacheControl;

/**
 * @author Shanshan Jiang
 *
 */
@Path("/take")
@Produces(MediaType.APPLICATION_JSON)
public class TakeResource {
	public TakeResource(DBI jdbi) {
		this.dao = jdbi.onDemand(TakeDAO.class);
	}

	@RolesAllowed("ADMIN")
	@GET
	@CacheControl(noCache = true)
	public Collection<Take> getAll() {
		return dao.selectAll();
	}

	
	@RolesAllowed("ADMIN")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
			@FormParam("student") @NotEmpty String student,
			@FormParam("courseID") @NotEmpty String courseID, 
			@FormParam("grade") @NotEmpty String grade,
			@FormParam("approved") @NotEmpty String approved) 
	{
		logger.info("add {}: ({},{},{})", student, courseID, grade, approved);

		dao.insert(new Take(student, courseID, grade, approved, 0));

		URI self = uri.getAbsolutePathBuilder().path(courseID).build();

		return Response.created(self).build();
	}
	
	@RolesAllowed("ADMIN")
	@PUT
	@Path("/{student}/{courseID}")
	public Response update(
			@PathParam("student") @NotEmpty String student,
			@PathParam("courseID") @NotEmpty String courseID, 
			@FormParam("grade") @NotEmpty String grade,
			@FormParam("approved") @NotEmpty String approved) 
	{
		logger.info("add {}: ({},{},{})", student, courseID, grade, approved);

		dao.update(new Take(student, courseID, grade, approved, 0));

		URI self = uri.getAbsolutePathBuilder().path(courseID).build();

		return Response.created(self).build();
	}
	

	@RolesAllowed("ADMIN")
	@DELETE
	@Path("/{student}/{courseID}")
	public Response delete(
			@PathParam("student") @NotEmpty String student,
			@PathParam("courseID") @NotEmpty String courseID) {
		logger.info("delete {}", student);
		dao.delete(student, courseID);
		return Response.ok().build();
	}
	
	@GET @Path("/byInstructor/{instructor}") 
	@CacheControl(noCache = true)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Collection<Take> gradebyInstructor(
			@PathParam("instructor") @NotEmpty String instructor){
		
		logger.info("gradesbyins {}",instructor);

		return dao.gradebyInstructor(instructor);
	}

	@GET @Path("/GPAbyaInstructor/{title}/{instructor}") 
	@CacheControl(noCache = true)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Collection<InstructorGPA> GPAbyaInstructor(
			@PathParam("title") @NotEmpty String title,
			@PathParam("instructor") @NotEmpty String instructor){
		
		logger.info("GPAsbyins {},{}",title, instructor);

		InstructorGPA GPAbyIns = dao.GPAbyaInstructor(title, instructor);
		if (GPAbyIns.getMax() == null)
			return new ArrayList<>();
		
		GPAbyIns.setInstructor(instructor);
		return Arrays.asList(GPAbyIns);
	}
	
	@GET @Path("/GPAbyallInstructor/{title}") 
	@CacheControl(noCache = true)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Collection<InstructorGPA> GPAbyallInstructor(
			@PathParam("title") @NotEmpty String title){
		
		logger.info("GPAsbyins {}",title);

		return dao.GPAbyallInstructor(title);
	}
	private final TakeDAO dao;

	@Context
	UriInfo uri;

	private static final Logger logger = LoggerFactory.getLogger(TakeResource.class);
}
