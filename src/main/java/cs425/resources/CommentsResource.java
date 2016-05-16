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
import javax.ws.rs.core.Response.Status;

import org.hibernate.validator.constraints.NotEmpty;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs425.api.Comment;
import cs425.auth.Account;
import cs425.jdbi.CommentDAO;
import cs425.jdbi.ForumDAO;
import cs425.jdbi.GroupUserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/comments")
@Produces(MediaType.APPLICATION_JSON)
public class CommentsResource
{
	public CommentsResource(DBI jdbi)
	{
		this.dao = jdbi.onDemand(CommentDAO.class);
		this.forumDao = jdbi.onDemand(ForumDAO.class);	
		this.groupUserDao = jdbi.onDemand(GroupUserDAO.class);
	}
	
	@RolesAllowed("ADMIN")
	@GET 
	@CacheControl(noCache = true)
	public Collection<Comment> getAll(){
		return dao.selectAll();
	}
	
	@RolesAllowed("GROUPUSER")
	@GET @Path("/{groupID}/{forumID}")
	@CacheControl(noCache = true)
	public Collection<Comment> getAll(
		@PathParam("groupID") @NotEmpty String groupID,
		@PathParam("forumID") @NotEmpty String forumID)
	{
		Collection<Comment> all = dao.selectAll(forumID);
		String title = forumDao.select(forumID).getTitle();
		
		logger.info("get {}/{}: {} comments", groupID, forumID, all.size());
		
		for (Comment comment: all)
			comment.setTitle(title);
		
		return all;
	}

	@RolesAllowed("GROUPUSER")
	@POST @Path("/{groupID}/{forumID}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
		@Auth Account account,
		@PathParam("groupID") @NotEmpty String groupID,
		@PathParam("forumID") @NotEmpty String forumID,
		@FormParam("topic") @NotEmpty String topic,
		@FormParam("message") @NotEmpty String message,
		@FormParam("bonus") Integer bonus)
	{
		String author = account.getName();
		
		logger.info("add {}/{} by {}: ({},{},{})",
			groupID, forumID, author, topic, message, bonus);
		
		String mod = groupUserDao.checkMember(groupID, author);
		if (mod == null)
		{
			logger.debug("{} is not user of {}", author, groupID);
			return Response.status(Status.BAD_REQUEST).build();
		}
		else if (!mod.equals("yes"))
		{
			bonus = 0;
		}

		dao.insert(new Comment(0, topic, message, author, forumID, bonus));
		
		URI self = uri.getAbsolutePathBuilder().path("").build();
			
		return Response.created(self).build();
	}

	@RolesAllowed("GROUPUSER")
	@PUT @Path("/{groupID}/{forumID}/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response update(
		@Auth Account account,
		@PathParam("groupID") @NotEmpty String groupID,
		@PathParam("forumID") @NotEmpty String forumID,
		@PathParam("id") Integer id,
		@FormParam("topic") @NotEmpty String topic,
		@FormParam("message") @NotEmpty String message,
		@FormParam("bonus") Integer bonus)
	{
		String updator = account.getName();
		
		logger.info("update {}/{}/{} by {}: ({},{},{})",
			groupID, forumID, id, updator, topic, message, bonus);
		
		String mod = groupUserDao.checkMember(groupID, updator);
		if ((mod == null) || !mod.equals("yes"))
		{
			logger.debug("{} is not moderator of {}", updator, groupID);
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		dao.update(new Comment(id, topic, message, null, null, bonus));
		
		URI self = uri.getAbsolutePathBuilder().path("").build();
			
		return Response.created(self).build();
	}
	
	@RolesAllowed("GROUPUSER")
	@DELETE @Path("/{groupID}/{forumID}/{id}")
	public Response delete(
			@Auth Account account,
			@PathParam("groupID") @NotEmpty String groupID,
			@PathParam("forumID") @NotEmpty String forumID,
			@PathParam("id") Integer id)
	{
		String deletor = account.getName();
		
		logger.info("delete {}/{}/{} by {}",
			groupID, forumID, id, deletor);
		
		String mod = groupUserDao.checkMember(groupID, deletor);
		if ((mod == null) || !mod.equals("yes"))
		{
			logger.debug("{} is not moderator of {}", deletor, groupID);
			return Response.status(Status.BAD_REQUEST).build();
		}

		dao.delete(id);
		
		return Response.ok().build();
    }
	
	private final CommentDAO dao; 
	private final ForumDAO forumDao;
	private final GroupUserDAO groupUserDao;
	
    @Context
    UriInfo uri;

	private static final Logger logger
		= LoggerFactory.getLogger(CommentsResource.class);		
}
