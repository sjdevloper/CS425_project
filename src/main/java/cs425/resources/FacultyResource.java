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

import cs425.api.Faculty;
import cs425.auth.Account;
import cs425.jdbi.FacultyDAO;
import cs425.jdbi.UserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/faculty")
@Produces(MediaType.APPLICATION_JSON)
public class FacultyResource {
	public FacultyResource(DBI jdbi){
		this.dao = jdbi.onDemand(FacultyDAO.class);
		this.userdao = jdbi.onDemand(UserDAO.class);
	}

	@RolesAllowed("ADMIN")
	@GET 
	@CacheControl(noCache = true)
	public Collection<Faculty> getAll(){
		return dao.selectAll();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
			@FormParam("username") @NotEmpty String username,
			@FormParam("name") @NotEmpty String name,
			@FormParam("password") @NotEmpty String password,
			@FormParam("email") @NotEmpty String email,
			@FormParam("position") @NotEmpty String position,
			@FormParam("yearJoined") Integer yearJoined,
			@FormParam("experience") @NotEmpty String experience,
			@FormParam("publicEmail") @NotEmpty String publicEmail,
			@FormParam("publicPosition") @NotEmpty String publicPosition,
			@FormParam("publicYearJoined")  @NotEmpty String publicYearJoined,
			@FormParam("publicExperience") @NotEmpty String publicExperience)
	{
		logger.info("add {}: ({},{},{},{},{})",
			username, position, yearJoined, experience);
		
		Faculty f = new Faculty(username, name, password, email,
				position, yearJoined, experience, publicEmail,
				publicPosition,publicYearJoined, publicExperience);
		
		userdao.insert(f);
		dao.insert(f);
		
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
	public Faculty get(
		@Auth Account account,
		@PathParam("username") @NotEmpty String username)
	{
		if (account.getName().equals(username) || account.isUserInRole("ADMIN"))
		{
			logger.info("get {}", username);
			
			return dao.select(username);
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
			@FormParam("position") @NotEmpty String position,
			@FormParam("yearJoined") Integer yearJoined,
			@FormParam("experience") @NotEmpty String experience,
			@FormParam("publicEmail") @NotEmpty String publicEmail,
			@FormParam("publicPosition") @NotEmpty String publicPosition,
			@FormParam("publicYearJoined")  @NotEmpty String publicYearJoined,
			@FormParam("publicExperience") @NotEmpty String publicExperience)
	{
		if (account.getName().equals(username) || account.isUserInRole("ADMIN"))
		{
			logger.info("update {}: ({},{},{},{},{})",
					username, position, yearJoined, experience);
		
			Faculty f = new Faculty(username, name, password, email,
					position, yearJoined, experience, publicEmail,
					publicPosition, publicYearJoined, publicExperience);
			
			userdao.update(f);
			dao.update(f);
			
			URI self = uri.getAbsolutePathBuilder().path(username).build();
			
			return Response.created(self).build();
		}
		else
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);	
	}
	private final FacultyDAO dao; 
	private final UserDAO userdao;
	
    @Context
    UriInfo uri;

	private static final Logger logger
		= LoggerFactory.getLogger(FacultyResource.class);		
}
