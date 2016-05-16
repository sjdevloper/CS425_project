package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import cs425.api.Group;

public interface GroupDAO
{
	@SqlQuery("SELECT * FROM InterestGroupsClubs")
	Collection<Group> selectAll();
	
	@SqlQuery("SELECT * FROM InterestGroupsClubs WHERE id=:id")
	Group select(@Bind("id") String id);

	@SqlUpdate("DELETE FROM InterestGroupsClubs WHERE id=:id")
	void delete(@Bind("id") String id);

	@SqlUpdate(
		"INSERT INTO InterestGroupsClubs (id, name, type)"
		+" values (:id, :name, :type)")
	void insert(@BindBean Group g);
	
	@SqlUpdate(
		"UPDATE InterestGroupsClubs"
		+" SET name=:name, type=:type"
		+" WHERE id=:id")
	void update(@BindBean Group g);
}