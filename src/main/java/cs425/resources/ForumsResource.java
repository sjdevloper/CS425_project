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

import cs425.api.Forum;
import cs425.auth.Account;
import cs425.jdbi.ForumDAO;
import cs425.jdbi.GroupDAO;
import cs425.jdbi.GroupUserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/forums")
@Produces(MediaType.APPLICATION_JSON)
public class ForumsResource
{
	public ForumsResource(DBI jdbi)
	{
		this.dao = jdbi.onDemand(ForumDAO.class);
		this.groupDao = jdbi.onDemand(GroupDAO.class);
		this.groupUserDao = jdbi.onDemand(GroupUserDAO.class);
	}
	
	@RolesAllowed("GROUPUSER")
	@GET 
	@CacheControl(noCache = true)
	public Collection<Forum> getAll()
	{
		Collection<Forum> all = dao.selectAll();
		
		logger.info("getAll: {} forums", all.size());
		
		for (Forum forum: all)
			forum.setGroupName(
				groupDao.select(forum.getGroupID()).getName());
		
		return all;
	}
	
	@RolesAllowed("GROUPUSER")
	@GET @Path("/{groupID}")
	@CacheControl(noCache = true)
	public Collection<Forum> getAll(
		@PathParam("groupID") @NotEmpty String groupID)
	{
		Collection<Forum> all = dao.selectAll(groupID);
		String groupName = groupDao.select(groupID).getName();
		
		logger.info("get {}: {} forums", groupID, all.size());
		
		for (Forum forum: all)
			forum.setGroupName(groupName);
		
		return all;
	}
	
	@RolesAllowed("GROUPUSER")
	@POST @Path("/{groupID}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
		@Auth Account account,
		@PathParam("groupID") @NotEmpty String groupID,
		@FormParam("title") @NotEmpty String title,
		@FormParam("approved") String approved,
		@FormParam("bonus") Integer bonus)	
	{
		String id = UUID.randomUUID().toString();
		String creator = account.getName();

		logger.info("add {}/{} by {}: ({},{},{})",
			groupID, id, creator, title, approved, bonus);
		
		String mod = groupUserDao.checkMember(groupID, creator);
		if (mod == null )
		{
			logger.debug("{} is not user of {}", creator, groupID);
			return Response.status(Status.BAD_REQUEST).build();
		}
		else if (!mod.equals("yes"))
		{
			approved = "no";
			bonus = 0;
		}
		
		dao.insert(new Forum(
			id, title, groupID, creator, approved, bonus));
	
		URI self = uri.getAbsolutePathBuilder().path(id).build();
		
		return Response.created(self).build();
	}
	
	@RolesAllowed("GROUPUSER")
	@PUT @Path("/{groupID}/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response update(
		@Auth Account account,
		@PathParam("groupID") @NotEmpty String groupID,
		@PathParam("id") @NotEmpty String id,
		@FormParam("title") @NotEmpty String title,
		@FormParam("approved") @NotEmpty String approved,
		@FormParam("bonus") Integer bonus)	
	{
		String updator = account.getName();
		
		logger.info("update {}/{} by {}: ({},{},{})",
			groupID, id, updator, title, approved, bonus);
		
		String mod = groupUserDao.checkMember(groupID, updator);
		if ((mod == null) || !mod.equals("yes"))
		{
			logger.debug("{} is not moderator of {}", updator, groupID);
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		dao.update(new Forum(
			id, title, null, null, approved, bonus));
	
		URI self = uri.getAbsolutePathBuilder().path(id).build();
		
		return Response.created(self).build();	
	}

	@RolesAllowed("GROUPUSER")
	@DELETE @Path("/{groupID}/{id}")
	public Response delete(
			@Auth Account account,
			@PathParam("groupID") @NotEmpty String groupID,
			@PathParam("id") @NotEmpty String id)
	{
		String deletor = account.getName();
		
		logger.info("delete {}/{} by {}",
			groupID, id, deletor);
		
		String mod = groupUserDao.checkMember(groupID, deletor);
		if ((mod == null) || !mod.equals("yes"))
		{
			logger.debug("{} is not moderator of {}", deletor, groupID);
			return Response.status(Status.BAD_REQUEST).build();
		}

		dao.delete(id);
		
		return Response.ok().build();
    }
	
	private final ForumDAO dao;
	private final GroupDAO groupDao;
	private final GroupUserDAO groupUserDao;
	
    @Context
    UriInfo uri;

	private static final Logger logger
		= LoggerFactory.getLogger(ForumsResource.class);		
}
