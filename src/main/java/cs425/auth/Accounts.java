package cs425.auth;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.NotEmpty;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs425.api.GroupUser;
import cs425.api.User;
import cs425.jdbi.GroupUserDAO;
import cs425.jdbi.UserDAO;
import io.dropwizard.auth.Auth;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class Accounts
{
	public Accounts(CookieAuth auth, DBI jdbi)
	{
		this.auth = auth;
		this.dao = jdbi.onDemand(UserDAO.class);
		this.groupUserDao = jdbi.onDemand(GroupUserDAO.class);
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(
		@FormParam("username") @NotEmpty String username,
		@FormParam("password") @NotEmpty String password)
	{
		HashMap<String, String> ret = new HashMap<>();
		ret.put("username", username);
		ret.put("home", "error");
		
		Account account = null;
		// check username/password, should use DB
		if (username.equals("admin") && password.equals("1234"))
		{
			account = Account.makeAdmin(username);
			ret.put("home", "admin");
		}
		else
		{
			User u = dao.select(username);
			
			if ((u != null) && u.getPassword().equals(password))
			{
				account = Account.makeUser(username);
				ret.put("home", u.getType());
			}
			else
			{
				logger.info("login failed: {}", username);
			}
		}
		if (account == null)
			return Response.ok(ret).build();
		
		// store account and generate cookie for authentication
		NewCookie c = auth.put(account);
			
		logger.info("login OK: {}, {}", account, c);
	
		return Response.ok(ret).cookie(c).build();
    }
	
	@POST
	@Path("/groupLogin")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response grouplogin(
		@FormParam("groupUser") @NotEmpty String groupUser,
		@FormParam("groupPwd") @NotEmpty String groupPwd)
	{
		HashMap<String, String> ret = new HashMap<>();
		ret.put("groupUser", groupUser);
		ret.put("home", "error");
		
		Account account = null;
		// check groupUser/groupPwd, should use DB
		if (groupUser.equals("admin") && groupPwd.equals("1234"))
		{
			account = Account.makeAdmin(groupUser);
			ret.put("home", "admin");
			ret.put("groupID", "admin");
		}
		else
		{
			GroupUser g = groupUserDao.select(groupUser);
			
			if ((g != null) && !g.getApproved().equals("yes"))
			{
				ret.put("home", "pending");
			}
			else if ((g != null) && g.getGroupPwd().equals(groupPwd))
			{
				account = Account.makeGroupUser(groupUser);
				ret.put("groupID", g.getGroupID());
				if (g.getIsModerator().equals("yes"))
					ret.put("home", "groupModerator");
				else
					ret.put("home", "groupUser");
			}
			else
			{
				logger.info("login failed: {}", groupUser);
			}
		}
		if (account == null)
			return Response.ok(ret).build();
		
		// store account and generate cookie for authentication
		NewCookie c = auth.put(account);
			
		logger.info("login OK: {}, {}", account, c);
	
		return Response.ok(ret).cookie(c).build();
    }
	
	@POST
	@Path("/logout")
	public Response logout(@Auth Account account)
	{
		logger.info("logout: {}", account);
		auth.purge(account);
		return Response.ok().build();
	}
	
	private final CookieAuth auth;
	private final UserDAO dao;
	private final GroupUserDAO groupUserDao;

	private static final Logger logger
		= LoggerFactory.getLogger(Accounts.class);
}
