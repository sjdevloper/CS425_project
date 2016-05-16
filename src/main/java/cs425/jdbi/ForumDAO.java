package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import cs425.api.Forum;

public interface ForumDAO
{
	@SqlQuery("SELECT * FROM discussionForums")
	Collection<Forum> selectAll();
	
	@SqlQuery("SELECT * FROM discussionForums WHERE groupID=:groupID")
	Collection<Forum> selectAll(@Bind("groupID") String groupID);
	
	@SqlQuery("SELECT * FROM discussionForums WHERE id=:id")
	Forum select(@Bind("id") String id);

	@SqlUpdate("DELETE FROM discussionForums WHERE id=:id")
	void delete(@Bind("id") String id);
	
	@SqlUpdate(
		"INSERT INTO discussionForums (id, title, groupID, creator, approved, bonus)"
		+" values(:id, :title, :groupID, :creator, :approved, :bonus)")
	void insert(@BindBean Forum d);
	
	@SqlUpdate(
		"UPDATE discussionForums"
		+" SET title=:title, approved=:approved, bonus=:bonus"
		+" WHERE id=:id")
	void update(@BindBean Forum d);
}   
