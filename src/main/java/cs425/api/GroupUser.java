package cs425.api;

public class GroupUser {
	public GroupUser() {
	}
	
	public GroupUser(String groupUser, String groupPwd, String groupID, 
			String username, String isModerator, String approved) {
		super();
		this.groupUser = groupUser;
		this.groupPwd = groupPwd;
		this.groupID = groupID;
		this.username = username;
		this.isModerator = isModerator;
		this.approved = approved;
	}

	public String getGroupUser() {
		return groupUser;
	}
	public void setGroupUser(String groupUser) {
		this.groupUser = groupUser;
	}
	public String getGroupPwd() {
		return groupPwd;
	}
	public void setGroupPwd(String groupPwd) {
		this.groupPwd = groupPwd;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIsModerator() {
		return isModerator;
	}
	public void setIsModerator(String isModerator) {
		this.isModerator = isModerator;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	private String groupUser;
	private String groupPwd;
	private String groupID;
	private String username;
	private String isModerator;
	private String approved;
	
}
