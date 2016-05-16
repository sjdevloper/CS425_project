package cs425.jdbi;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import cs425.api.GroupUser;

public interface GroupUserDAO
{
	@SqlQuery(
		"SELECT * FROM groupusers")
	Collection<GroupUser> selectAll();
	
	@SqlQuery(
		"SELECT * FROM groupusers WHERE groupUser=:groupUser")
	GroupUser select(@Bind("groupUser") String groupUser);

	@SqlUpdate(
		"INSERT INTO groupusers (groupUser, username, groupID, groupPwd, isModerator, approved)"
		+" values (:groupUser, :username, :groupID, :groupPwd, :isModerator, :approved)")
	void insert(@BindBean GroupUser g);
	
	@SqlUpdate(
		"UPDATE groupusers"
		+" SET username=:username, groupID=:groupID,"
		+" groupPwd=:groupPwd, isModerator=:isModerator, approved=:approved"
		+" WHERE groupUser=:groupUser")
	void update(@BindBean GroupUser g);

	@SqlUpdate(
		"DELETE FROM groupusers WHERE groupUser=:groupUser")
	void delete(@Bind("groupUser") String groupUser);

/*	
	@SqlUpdate(
		"UPDATE groupusers "
		+"SET isModerator='yes' "
		+"WHERE groupUser=:groupUser")
	void setModerator(@Bind("groupUser") String groupUser);
*/
	@SqlQuery(
		"SELECT groupUser FROM groupusers "
		+"WHERE groupID=:groupID and isModerator='yes'")
	Collection<String> findModerators(@Bind("groupID") String groupID);
	
	@SqlQuery(
		"SELECT isModerator FROM groupusers"
		+" WHERE groupID=:groupID AND groupUser=:groupUser")
	String checkMember(@Bind("groupID") String groupID,
			@Bind("groupUser") String groupUser);
}