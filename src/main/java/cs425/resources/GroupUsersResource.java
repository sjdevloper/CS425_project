package cs425.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

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

import cs425.api.Course;
import cs425.api.Group;
import cs425.api.GroupUser;
import cs425.auth.Account;
import cs425.jdbi.CourseDAO;
import cs425.jdbi.GroupDAO;
import cs425.jdbi.GroupUserDAO;
import cs425.jdbi.TADAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.caching.CacheControl;

@Path("/groupUsers")
@Produces(MediaType.APPLICATION_JSON)
public class GroupUsersResource
{
	public GroupUsersResource(DBI jdbi)
	{
		this.dao = jdbi.onDemand(GroupUserDAO.class);
		this.groupDao = jdbi.onDemand(GroupDAO.class);
		this.courseDao = jdbi.onDemand(CourseDAO.class);
		this.TADao = jdbi.onDemand(TADAO.class);
	}

	@RolesAllowed("GROUPUSER")
	@GET 
	@CacheControl(noCache = true)
	public Collection<GroupUser> getAll()
	{
		return dao.selectAll();
	}
	
	@RolesAllowed("USER")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add(
		@Auth Account account,
		@FormParam("groupUser") @NotEmpty String groupUser,
		@FormParam("groupPwd") @NotEmpty String groupPwd,
		@FormParam("groupID") @NotEmpty String groupID,
		@FormParam("username") String username,
		@FormParam("isModerator") String isModerator,
		@FormParam("approved") String approved)
	{
		logger.info("add {} by {}: ({},{},{},{})",
			groupUser, account.getName(),
			username, groupID, isModerator, approved);
		
		if (account.isUserInRole("ADMIN"))
		{
			if (isModerator.equals("yes") && !checkTA(groupID, username))
				return Response.status(Status.BAD_REQUEST).build();
			
			dao.insert(new GroupUser(
				groupUser, groupPwd, groupID, username, isModerator, approved));
		}
		else
		{
			dao.insert(new GroupUser(
				groupUser, groupPwd, groupID, account.getName(), "no", "no"));
		}

		URI self = uri.getAbsolutePathBuilder().path(groupUser).build();
			
		return Response.created(self).build();
	}
	
	@RolesAllowed("ADMIN")
	@DELETE @Path("/{groupUser}")
	public Response delete(
		@PathParam("groupUser") @NotEmpty String groupUser)
	{
		logger.info("delete {}", groupUser);
		
		dao.delete(groupUser);
		
		return Response.ok().build();
    }
	
	@RolesAllowed("GROUPUSER")
	@PUT @Path("/{groupUser}")
	public Response update(
		@PathParam("groupUser") @NotEmpty String groupUser,
		@FormParam("groupPwd") @NotEmpty String groupPwd,
		@FormParam("groupID") @NotEmpty String groupID,
		@FormParam("username") @NotEmpty String username,
		@FormParam("isModerator") @NotEmpty String isModerator,
		@FormParam("approved") @NotEmpty String approved)
	{
		logger.info("update {}: ({},{},{},{})",
			groupUser, username, groupID, isModerator, approved);
		
		if (isModerator.equals("yes") && !checkTA(groupID, username))
			return Response.status(Status.BAD_REQUEST).build();
		
		dao.update(new GroupUser(
			groupUser, groupPwd, groupID, username, isModerator, approved));
			
		URI self = uri.getAbsolutePathBuilder().path(groupUser).build();
		
		return Response.created(self).build();
	}
	
	private boolean checkTA(String groupID, String username)
	{
		Group group = groupDao.select(groupID);
		if (!group.getType().equals("courseGroup"))
			return true;
		
		Course course = courseDao.findCourseByGroup(groupID);
		if (course.getInstructor().equals(username))
			return true;
		
		String courseID = course.getId();
		HashSet<String> TA = new HashSet<>(
				TADao.findTAByCourse(courseID));
		logger.debug("TAs for {}: {}", courseID,
			Arrays.toString(TA.toArray(new String[0])));
		if (!TA.contains(username))
		{
			logger.debug("{} is not a TA in course {}", 
				username, courseID);
			return false;
		}

		return true;
	}
	
	private final GroupUserDAO dao;
	private final GroupDAO groupDao;
	private final CourseDAO courseDao;
	private final TADAO TADao;
	
    @Context
    UriInfo uri;

	private static final Logger logger
		= LoggerFactory.getLogger(GroupUsersResource.class);		
}
