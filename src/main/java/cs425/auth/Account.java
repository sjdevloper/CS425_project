package cs425.auth;

import java.security.Principal;

public class Account implements Principal
{
	public static Account makeAdmin(String name)
	{
		return new Account(name, Role.ADMIN);
	}
	
	public static Account makeUser(String name)
	{
		return new Account(name, Role.USER);
	}

	public static Account makeGroupUser(String name)
	{
		return new Account(name, Role.GROUPUSER);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	public boolean isUserInRole(String r)
	{
		if (role == Role.ADMIN)
			return true;

		if ((role == Role.USER)
			&& r.equals("USER"))
			return true;
		
		if ((role == Role.GROUPUSER)
			&& r.equals("GROUPUSER"))
			return true;
		
		return false;
	}
	
	@Override
	public String toString()
	{
		return name+" as "+role;
	}
	
	private enum Role {ADMIN, USER, GROUPUSER};
	
	private Account(String name, Role role)
	{
		this.name = name;
		this.role = role;
	}
	
	void setSession(String session)
	{
		assert this.session.equals("");
		
		this.session = session;
	}
	
	String getSession()
	{
		assert !session.equals("");

		return session;
	}
	
	private String session = "";
	private final String name;
	private final Role role;
}
