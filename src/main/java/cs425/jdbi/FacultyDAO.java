package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import cs425.api.Faculty;

public interface FacultyDAO
{
	@SqlQuery(
		"SELECT * FROM faculty NATURAL JOIN users")
	Collection<Faculty> selectAll();

	@SqlQuery(
		"SELECT * FROM faculty NATURAL JOIN users"
		+ " WHERE username=:username")
	Faculty select(@Bind("username") String username);
	
	@SqlUpdate(
		"INSERT INTO faculty (username, name, email, position, yearJoined, experience,"
		+ " publicEmail, publicPosition, publicYearJoined, publicExperience)"
		+ " values (:username, :name, :email, :position, :yearJoined, :experience,"
		+ " :publicEmail, :publicPosition, :publicYearJoined, :publicExperience)")
	void insert(@BindBean Faculty f);

	@SqlUpdate(
		"DELETE FROM faculty WHERE username=:username")
	void delete(@Bind("username") String username);
	
	@SqlUpdate(
		"UPDATE faculty"
		+" SET email=:email, position=:position, yearJoined=:yearJoined,"
		+" experience=:experience, publicEmail=:publicEmail, publicPosition=:publicPosition,"
		+" publicYearJoined=:publicYearJoined, publicExperience=:publicExperience"
		+" WHERE username=:username")
	void update(@BindBean Faculty f);
}