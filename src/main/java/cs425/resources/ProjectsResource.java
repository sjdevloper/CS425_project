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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotEmpty;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs425.api.Project;
import cs425.auth.Account;
import cs425.jdbi.ProjectDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectsResource
{
	public ProjectsResource(DBI jdbi)
	{
		this.dao = jdbi.onDemand(ProjectDAO.class);
	}

	@GET 
	@CacheControl(noCache = true)
	public Collection<Project> getAll(){
		return dao.selectAll();
	}
	
	@RolesAllowed("USER")
	@GET @Path("/{username}")
	@CacheControl(noCache = true)
	public Collection<Project> getAll(
		@Auth Account account,
		@PathParam("username") @NotEmpty String username)
	{
		if (account.getName().equals(username))
		{
			Collection<Project> all = dao.selectAll(username);
		
			logger.info("get {}: {} projects", username, all.size());
		
			return all;
		}
		else
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
	}
	
	@RolesAllowed("USER")
	@POST @Path("/{username}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
		@Auth Account account,
		@PathParam("username") @NotEmpty String username,
		@FormParam("project") @NotEmpty String project,
		@FormParam("publicProject") @NotEmpty String publicProject)
	{
		if (account.getName().equals(username)||account.isUserInRole("ADMIN"))
		{
			String projectID = UUID.randomUUID().toString();

			logger.info("add {}/{}: {}, {}",
				username, projectID, project, publicProject);
		
			dao.insert(new Project(
				username, projectID, project, publicProject));
		
			URI self = uri.getAbsolutePathBuilder().path(projectID).build();
			
			return Response.created(self).build();
		}
		else
			return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	
	@RolesAllowed("USER")
	@PUT @Path("/{username}/{projectID}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response update(
		@Auth Account account,
		@PathParam("username") @NotEmpty String username,
		@PathParam("projectID") @NotEmpty String projectID,
		@FormParam("project") @NotEmpty String project,
		@FormParam("publicProject") @NotEmpty String publicProject)
	{
		if (account.getName().equals(username))
		{
			logger.info("update {}/{}: {},{}",
				username, projectID, project, publicProject);
		
			dao.update(new Project(
				username, projectID, project, publicProject));
		
			URI self = uri.getAbsolutePathBuilder().path(projectID).build();
			
			return Response.created(self).build();
		}
		else
			return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@RolesAllowed("USER")
	@DELETE @Path("/{username}/{projectID}")
	public Response delete(
		@Auth Account account,
		@PathParam("username") @NotEmpty String username,
		@PathParam("projectID") @NotEmpty String projectID)
	{
		if (account.getName().equals(username))
		{
			logger.info("delete {}/{}", username, projectID);
			
			dao.delete(username, projectID);
			
			return Response.ok().build();
		}
		else
			return Response.status(Response.Status.UNAUTHORIZED).build();
    }
	
	private final ProjectDAO dao; 
	
    @Context
    UriInfo uri;

	private static final Logger logger
		= LoggerFactory.getLogger(ProjectsResource.class);		
}
