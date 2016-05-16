package cs425.resources;

import java.util.Collection;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.hibernate.validator.constraints.NotEmpty;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs425.api.Comment;
import cs425.jdbi.GroupDAO;
import cs425.jdbi.PublicDAO;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/public")
@Produces(MediaType.APPLICATION_JSON)
public class PublicResource
{
	public PublicResource(DBI jdbi)
	{
		this.dao = jdbi.onDemand(PublicDAO.class);
		this.groupDao = jdbi.onDemand(GroupDAO.class);
	}
	
	@GET @Path("/comments-by-groupID/{groupID}") 
	@CacheControl(noCache = true)
	public Collection<Comment> getCommentsByGroupID(
		@PathParam("groupID") @NotEmpty String groupID)
	{
		return dao.selectCommentsByGroupIDTop5(groupID);
	}

	@GET @Path("/comments-by-user/{username}") 
	@CacheControl(noCache = true)
	public Collection<Comment> getCommentsByUser(
		@PathParam("username") @NotEmpty String username)
	{
		return dao.selectCommentsbyUserTop5(username);
	}
	
	@GET @Path("/comments-pop-topic") 
	@CacheControl(noCache = true)
	public HashMap<String, Object> popTopic()
	{		
		String topic = dao.popTopic();

		logger.info("popTopic: {}", topic);
		
		HashMap<String, Object> ret = new HashMap<>();
		ret.put("topic", topic);
		ret.put("comments", dao.selectCommentsByTopic(topic));
		
		return ret;
	}
	
	@GET @Path("/comments-pop-group") 
	@CacheControl(noCache = true)
	public HashMap<String, Object> popGroup()
	{		
		String groupID = dao.popGroup();
		
		logger.info("popGroup: {}", groupID);
		
		HashMap<String, Object> ret = new HashMap<>();
		ret.put("group", groupDao.select(groupID).getName());
		ret.put("comments", dao.selectCommentsByGroupID(groupID));
		
		return ret;
	}
	
	private final PublicDAO dao;
	private final GroupDAO groupDao;
	
    @Context
    UriInfo uri;

	private static final Logger logger
		= LoggerFactory.getLogger(PublicResource.class);		
}
