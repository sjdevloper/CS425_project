package cs425.api;

public class Forum {
	public Forum() {
	}

	public Forum(String id, String title, String groupID, 
		String creator, String approved, Integer bonus) {
		super();
		this.id = id;
		this.title = title;
		this.groupID = groupID;
		this.creator = creator;
		this.approved = approved;
		this.bonus = bonus;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getApproved() {
		return approved;
	}
	public void setApproved(String approved) {
		this.approved = approved;
	}
	public Integer getBonus() {
		return bonus;
	}
	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	private String id;
	private String title;
	private String groupID;
	private String creator;
	private String approved;
	private Integer bonus;
	private String groupName = null;
}
