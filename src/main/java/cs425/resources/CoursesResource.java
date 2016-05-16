/**
 * 
 */

package cs425.resources;

import java.net.URI;
import java.util.Collection;
import java.util.UUID;

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

import io.dropwizard.jersey.caching.CacheControl;

import cs425.api.Course;
import cs425.api.Group;
import cs425.jdbi.CourseDAO;
import cs425.jdbi.GroupDAO;

/**
 * @author Shanshan Jiang
 *
 */
@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
public class CoursesResource {
	public CoursesResource(DBI jdbi){
		this.dao = jdbi.onDemand(CourseDAO.class);
		this.groupDao = jdbi.onDemand(GroupDAO.class);
	}

	@GET 
	@CacheControl(noCache = true)
	public Collection<Course> getAll(){
		return dao.selectAll();
	}
	
	@RolesAllowed("ADMIN")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
		@FormParam("title") @NotEmpty String title,
		@FormParam("term") @NotEmpty String term,
		@FormParam("creditHour") @NotEmpty String creditHour,
		@FormParam("instructor") @NotEmpty String instructor)
	{
		String id = UUID.randomUUID().toString();
		String groupID = UUID.randomUUID().toString();
		
		logger.info("add {}/{}: ({},{},{},{})",
			id, groupID, title, term, instructor, creditHour);
			
		groupDao.insert(new Group(groupID, title+"_"+term+"_group", "courseGroup", null));
		dao.insert(new Course(id, title, term, creditHour, instructor, groupID));
		
		URI self = uri.getAbsolutePathBuilder().path(id).build();
		
		return Response.created(self).build();
	}
	
	@RolesAllowed("ADMIN")
	@PUT @Path("/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response update(
		@PathParam("id") @NotEmpty String id,			
		@FormParam("term") @NotEmpty String term,
		@FormParam("creditHour") @NotEmpty String creditHour,
		@FormParam("instructor") @NotEmpty String instructor)
	{
		logger.info("update {}: ({},{},{})",
			id, term, instructor, creditHour);
			
		dao.update(new Course(id, null, term, creditHour, instructor, null));
		
		URI self = uri.getAbsolutePathBuilder().path(id).build();
		
		return Response.created(self).build();
	}
	
	@RolesAllowed("ADMIN")
	@DELETE @Path("/{id}")
	public Response delete(
		@PathParam("id") @NotEmpty String id)
	{
		logger.info("delete {}", id);
		
		dao.delete(id);
		
		return Response.ok().build();
    }
	
	private final CourseDAO dao;
	private final GroupDAO groupDao;
	
    @Context
    UriInfo uri;

	private static final Logger logger
		= LoggerFactory.getLogger(CoursesResource.class);	


}
