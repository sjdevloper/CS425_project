package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import cs425.api.User;

public interface UserDAO
{
	@SqlQuery(
		"SELECT * FROM users")
	Collection<User> selectAll();
	
	@SqlQuery(
		"SELECT * FROM users WHERE username=:username")
	User select(@Bind("username") String username);

	@SqlUpdate(
		"INSERT INTO users (username, password, type)"
		+" values (:username, :password,:type)")
	void insert(@BindBean User u);

	@SqlUpdate(
		"DELETE FROM users WHERE username=:username")
	void delete(@Bind("username") String username);
	
	@SqlUpdate(
		"UPDATE users"
		+" SET password=:password"
		+" WHERE username=:username")
	void update(@BindBean User u);
}   
	