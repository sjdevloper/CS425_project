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
import javax.ws.rs.core.Response.Status;

import org.hibernate.validator.constraints.NotEmpty;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs425.api.TA;
import cs425.auth.Account;
import cs425.jdbi.TADAO;
import cs425.jdbi.TakeDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/TAs")
@Produces(MediaType.APPLICATION_JSON)
public class TAResource {
	public TAResource(DBI jdbi){
		this.dao = jdbi.onDemand(TADAO.class);
		this.take_dao = jdbi.onDemand(TakeDAO.class);
	}
	
	@GET 
	@CacheControl(noCache = true)
	public Collection<TA> getAll(){
		Collection<TA> TAs = dao.selectAll();
		return TAs;
	}

	@RolesAllowed("ADMIN")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
		@Auth Account account,
		@FormParam("courseID") @NotEmpty String courseID,
		@FormParam("courseTA") @NotEmpty String courseTA)
	{
		String id = UUID.randomUUID().toString();

		logger.info("add {}: ({},{})",
			id, courseID, courseTA);
		
		String student = take_dao.select(courseID, courseTA);
		if (student != null){
			logger.debug("The person is a student of the course");
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		dao.insert(new TA(id, courseID, courseTA));
	
		URI self = uri.getAbsolutePathBuilder().path(id).build();
		
		return Response.created(self).build();
	}
	
	@RolesAllowed("ADMIN")
	@PUT @Path("/{id}")
	public Response update(
			@Auth Account account,
			@PathParam("id") @NotEmpty String id,
			@FormParam("courseID") @NotEmpty String courseID,
			@FormParam("courseTA") @NotEmpty String courseTA)
	{
		logger.info("update {}: {},{},{}",
				id, courseID, courseTA);
		
		String student = take_dao.select(courseID, courseTA);
		if (student != null){
			logger.debug("The person is a student of the course");
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		dao.update(new TA(id, courseID, courseTA));
		
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
	
	private final TADAO dao; 
	private final TakeDAO take_dao; 
	
    @Context
    UriInfo uri;

	private static final Logger logger
		= LoggerFactory.getLogger(TAResource.class);		
}
