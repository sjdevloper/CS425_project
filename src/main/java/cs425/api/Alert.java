package cs425.api;

public class Alert {
	public Alert() {
	}

	public Alert(String id, String alert, String moderator, String groupID) {
		super();
		this.id = id;
		this.alert = alert;
		this.moderator = moderator;
		this.groupID = groupID;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	public String getModerator() {
		return moderator;
	}
	public void setModerator(String moderator) {
		this.moderator = moderator;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	private String id;
	private String alert;
	private String moderator;
	private String groupID;
}
