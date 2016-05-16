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

import cs425.api.Group;
import cs425.jdbi.GroupUserDAO;
import cs425.jdbi.GroupDAO;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
public class GroupsResource {
	public GroupsResource(DBI jdbi){
		this.dao = jdbi.onDemand(GroupDAO.class);
		this.groupUserDao = jdbi.onDemand(GroupUserDAO.class);
	}

	@GET 
	@CacheControl(noCache = true)
	public Collection<Group> getAll(){
		Collection<Group> groups = dao.selectAll();
		
		for (Group group: groups) {
			group.setModerators(
				groupUserDao.findModerators(group.getId()));
		}
		
		return groups;
	}
	
	@RolesAllowed("ADMIN")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
		@FormParam("name") @NotEmpty String name,
		@FormParam("type") @NotEmpty String type)
	{
		String id = UUID.randomUUID().toString();
		if (type.equals("courseGroup")){
			return Response.status(Status.BAD_REQUEST).build();
		}
		logger.info("add {}: {}", id, name);
		
		dao.insert(new Group(id, name, type, null));

		URI self = uri.getAbsolutePathBuilder().path(id).build();
			
		return Response.created(self).build();
	}
	
	@RolesAllowed("ADMIN")
	@PUT @Path("/{id}")
	public Response update(
		@PathParam("id") @NotEmpty String id,
		@FormParam("name") @NotEmpty String name,
		@FormParam("type") @NotEmpty String type)
	{
		logger.info("update {}: {},{}",
				id, name, type);
		
		dao.update(new Group(id, name, type, null));
			
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
	
	private final GroupDAO dao;
	private final GroupUserDAO groupUserDao;
	
    @Context
    UriInfo uri;

	private static final Logger logger
		= LoggerFactory.getLogger(GroupsResource.class);		
}
