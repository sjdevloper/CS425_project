package cs425.auth;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.jetty.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Authentication and authorization via cookie-based session. 
 */
@Priority(Priorities.AUTHENTICATION)
public class CookieAuth implements ContainerRequestFilter
{
	public CookieAuth(String cookieName, String cookiePath)
	{
		this.cookieName = cookieName;
		this.cookiePath = cookiePath;
	}

	public CookieAuth()
	{
		this.cookieName = "SessionToken";
		this.cookiePath = "/";
	}
	
	/**
	 * For login: store account and return session cookie.
	 */
	public NewCookie put(Account account)
	{
		String session = UUID.randomUUID().toString();
		
		account.setSession(session);
		sessions.put(session, account);
		
		return new NewCookie(
			cookieName, session, cookiePath, null,
			null, NewCookie.DEFAULT_MAX_AGE, false);
	}
	
	/**
	 * For logout: remove user session.
	 */
	public void purge(Account account)
	{
		sessions.remove(account.getSession());
	}
	
	/**
	 * Obtain session from cookie and convert it into User.  
	 */
	@Override
	public void filter(ContainerRequestContext requestContext)
		throws IOException
	{
		Cookie c = requestContext.getCookies().get(cookieName);
		if (c == null)
	        throw new WebApplicationException(Response.SC_UNAUTHORIZED);

		String session = c.getValue();
		Account account = sessions.get(session);
		if (account == null)
		{
			logger.debug("{}: {}={}, unknown",
				requestContext.getUriInfo().getAbsolutePath().getPath(),
				c.getName(), c.getValue());

			throw new WebApplicationException(Response.SC_UNAUTHORIZED);
		}

		logger.debug("{}: {}={}, {}",
			requestContext.getUriInfo().getAbsolutePath().getPath(),
			c.getName(), c.getValue(), account);
		
		requestContext.setSecurityContext(new SecurityContext() {
			@Override
			public Principal getUserPrincipal()
			{
				return account;
			}
				
			@Override
			public boolean isUserInRole(String role)
			{
				return account.isUserInRole(role);
			}

			@Override
			public boolean isSecure()
			{
				return requestContext.getSecurityContext().isSecure();
			}

			@Override
			public String getAuthenticationScheme()
			{
				return CookieAuth.class.getName();
			}
		});
    }

	private final String cookieName;
	private final String cookiePath;
	
	private final ConcurrentHashMap<String, Account>
		sessions = new ConcurrentHashMap<>();
	
	private static final Logger logger
		= LoggerFactory.getLogger(CookieAuth.class);
}
