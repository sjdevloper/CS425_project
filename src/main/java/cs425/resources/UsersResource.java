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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotEmpty;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs425.api.User;
import cs425.jdbi.UserDAO;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
	public UsersResource(DBI jdbi){
		this.dao = jdbi.onDemand(UserDAO.class);
	}

	@RolesAllowed("ADMIN")
	@GET 
	@CacheControl(noCache = true)
	public Collection<User> getAll(){
		return dao.selectAll();
	}
	
	@RolesAllowed("ADMIN")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
		@FormParam("username") @NotEmpty String username,
		@FormParam("password") @NotEmpty String password,
		@FormParam("type") @NotEmpty String type)
	{
		logger.info("add {}: {}", username, type);
			
		dao.insert(new User(username, password, type));
			
		URI self = uri.getAbsolutePathBuilder().path(username).build();
			
		return Response.created(self).build();
	}

	@RolesAllowed("ADMIN")
	@PUT @Path("/{username}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response update(
		@PathParam("username") @NotEmpty String username,
		@FormParam("password") @NotEmpty String password,
		@FormParam("type") @NotEmpty String type)
	{
		logger.info("update {}: {}", username, type);
			
		dao.update(new User(username, password, type));
			
		URI self = uri.getAbsolutePathBuilder().path(username).build();
			
		return Response.created(self).build();
	}
	
	@RolesAllowed("ADMIN")
	@DELETE @Path("/{username}")
	public Response delete(
		@PathParam("username") @NotEmpty String username)
	{
		logger.info("delete {}", username);
		
		dao.delete(username);
		
		return Response.ok().build();
    }
	
	private final UserDAO dao;
	
    @Context
    UriInfo uri;

	private static final Logger logger
		= LoggerFactory.getLogger(UsersResource.class);		
}
