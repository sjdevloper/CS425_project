package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import cs425.api.Project;

public interface ProjectDAO
{
	@SqlQuery("SELECT * FROM researchprojects")
	Collection<Project> selectAll();
	
	@SqlQuery("SELECT * FROM researchprojects WHERE username=:username")
	Collection<Project> selectAll(@Bind("username") String username);
	
	@SqlUpdate(
		"INSERT INTO researchprojects (username, projectID, project, publicProject)"
		+" values (:username, :projectID, :project, :publicProject)")
	void insert(@BindBean Project i);

	@SqlUpdate(
		"DELETE FROM researchprojects"
		+" WHERE projectID=:projectID and username=:username")
	void delete(
		@Bind("username") String username,
		@Bind("projectID") String projectID);
	
	@SqlUpdate(
		"UPDATE researchprojects"
		+" SET project=:project, publicProject=:publicProject"
		+" WHERE projectID=:projectID and username=:username")
	void update(@BindBean Project i);
}
