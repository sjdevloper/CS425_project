package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import cs425.api.Comment;

public interface PublicDAO
{
	@SqlQuery(
		"SELECT comments.id, author, message, topic,"
		+" forumID, comments.bonus, DiscussionForums.title"
		+" FROM comments JOIN discussionForums"
		+" ON comments.forumID = discussionforums.id"
	    +" WHERE groupID=:groupID"
	    +" ORDER BY ID DESC")
	Collection<Comment> selectCommentsByGroupID(@Bind("groupID") String groupID);

	@SqlQuery(
		"SELECT comments.id, author, message, topic,"
		+" forumID, comments.bonus, DiscussionForums.title"
		+" FROM comments JOIN discussionForums"
		+" ON comments.forumID = discussionforums.id"
	    +" WHERE groupID=:groupID AND ROWNUM <=5"
	    +" ORDER BY ID DESC")
	Collection<Comment> selectCommentsByGroupIDTop5(@Bind("groupID") String groupID);
	
	@SqlQuery(
		"SELECT comments.id, author, message, topic,"
		+" forumID, comments.bonus, DiscussionForums.title"
		+" FROM comments, groupUsers, DiscussionForums"
		+" WHERE comments.author = groupUsers.groupUser"
		+" AND comments.forumID = DiscussionForums.id"
		+" AND username=:username AND ROWNUM <=5"
		+" ORDER BY ID DESC")
	Collection<Comment> selectCommentsbyUserTop5(@Bind("username") String username);
	
	@SqlQuery(
		"SELECT comments.id, author, message, topic,"
		+" forumID, comments.bonus, DiscussionForums.title"
		+" FROM comments join DiscussionForums"
		+" on comments.forumID = DiscussionForums.id"
		+" WHERE topic=:topic"
		+" ORDER BY ID DESC")
	Collection<Comment> selectCommentsByTopic(@Bind("topic") String topic);
		
	@SqlQuery(
		"SELECT topic"
		+" FROM comments"
		+" WHERE ROWNUM <= 1"
		+" GROUP BY topic"
		+" ORDER BY count(*) DESC")
	String popTopic();
		
	@SqlQuery(
		"SELECT groupID FROM comments JOIN discussionForums"
		+" ON ForumID = discussionForums.id"
		+" WHERE ROWNUM <= 1"
	    +" GROUP BY groupID"
	    +" ORDER BY count(*) desc")
	String popGroup();
}   

