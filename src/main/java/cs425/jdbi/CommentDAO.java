package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import cs425.api.Comment;
import cs425.api.Forum;

public interface CommentDAO
{
	@SqlQuery("SELECT * FROM comments")
	Collection<Comment> selectAll();
	
	@SqlQuery("SELECT * FROM comments WHERE forumID=:forumID")
	Collection<Comment> selectAll(@Bind("forumID") String forumID);

	@SqlQuery("SELECT * FROM comments WHERE id=:id")
	Forum select(@Bind("id") String id);

	@SqlUpdate("DELETE FROM comments WHERE id=:id")
	void delete(@Bind("id") Integer id);

	@SqlUpdate(
		"INSERT INTO comments (id, topic, message, author, forumID, bonus)"
		+ " values (comment_id.nextval, :topic, :message, :author, :forumID, :bonus)")
	void insert(@BindBean Comment c);
	
	@SqlUpdate(
		"UPDATE comments"
		+" SET topic=:topic, message=:message, bonus=:bonus"
		+" WHERE id=:id")
	void update(@BindBean Comment c);
}   

