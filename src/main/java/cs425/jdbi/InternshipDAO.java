package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import cs425.api.Internship;

public interface InternshipDAO
{
	@SqlQuery("SELECT * FROM internships")
	Collection<Internship> selectAll();
	
	@SqlQuery("SELECT * FROM internships WHERE username=:username")
	Collection<Internship> selectAll(@Bind("username") String username);
	
	@SqlUpdate(
		"INSERT INTO internships (username, internshipID, internship, publicInternship)"
		+" values (:username, :internshipID, :internship, :publicInternship)")
	void insert(@BindBean Internship i);

	@SqlUpdate(
		"DELETE FROM internships"
		+" WHERE internshipID=:internshipID and username=:username")
	void delete(
		@Bind("username") String username,
		@Bind("internshipID") String internshipID);
	
	@SqlUpdate(
		"UPDATE internships"
		+" SET internship=:internship, publicInternship=:publicInternship"
		+" WHERE internshipID=:internshipID and username=:username")
	void update(@BindBean Internship i);
}
