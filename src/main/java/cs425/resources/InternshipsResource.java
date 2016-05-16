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

import cs425.api.Internship;
import cs425.auth.Account;
import cs425.jdbi.InternshipDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/internships")
@Produces(MediaType.APPLICATION_JSON)
public class InternshipsResource
{
	public InternshipsResource(DBI jdbi)
	{
		this.dao = jdbi.onDemand(InternshipDAO.class);
	}
	
	@GET 
	@CacheControl(noCache = true)
	public Collection<Internship> getAll(){
		return dao.selectAll();
	}
	
	@RolesAllowed("USER")
	@GET @Path("/{username}")
	@CacheControl(noCache = true)
	public Collection<Internship> getAll(
		@Auth Account account,
		@PathParam("username") @NotEmpty String username)
	{
		if (account.getName().equals(username))
		{
			Collection<Internship> all = dao.selectAll(username);
		
			logger.info("get {}: {} internships", username, all.size());
		
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
		@FormParam("internship") @NotEmpty String internship,
		@FormParam("publicInternship") @NotEmpty String publicInternship)
	{
		if (account.getName().equals(username)||account.isUserInRole("ADMIN"))
		{
			String internshipID = UUID.randomUUID().toString();

			logger.info("add {}/{}: {},{}",
				username, internshipID, internship, publicInternship);
		
			dao.insert(new Internship(username, internshipID,
				internship, publicInternship));
		
			URI self = uri.getAbsolutePathBuilder().path(internshipID).build();
			
			return Response.created(self).build();
		}
		else
			return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	
	@RolesAllowed("USER")
	@PUT @Path("/{username}/{internshipID}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response update(
		@Auth Account account,
		@PathParam("username") @NotEmpty String username,
		@PathParam("internshipID") @NotEmpty String internshipID,
		@FormParam("internship") @NotEmpty String internship,
		@FormParam("publicInternship") @NotEmpty String publicInternship)
	{
		if (account.getName().equals(username))
		{
			logger.info("update {}/{}: {},{}",
				username, internshipID, internship, publicInternship);
		
			dao.update(new Internship(
				username, internshipID, internship, publicInternship));
		
			URI self = uri.getAbsolutePathBuilder().path(internshipID).build();
			
			return Response.created(self).build();
		}
		else
			return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@RolesAllowed("USER")
	@DELETE @Path("/{username}/{internshipID}")
	public Response delete(
		@Auth Account account,
		@PathParam("username") @NotEmpty String username,
		@PathParam("internshipID") @NotEmpty String internshipID)
	{
		if (account.getName().equals(username))
		{
			logger.info("delete {}/{}", username, internshipID);
			
			dao.delete(username, internshipID);
			
			return Response.ok().build();
		}
		else
			return Response.status(Response.Status.UNAUTHORIZED).build();
    }
	
	private final InternshipDAO dao; 
	
    @Context
    UriInfo uri;

	private static final Logger logger
		= LoggerFactory.getLogger(InternshipsResource.class);		
}
