package cs425.api;

//@author Shanshan Jiang

public class Course {
	
	public Course()
	{
	}
	
	public Course(String id, String title, String term,
			String creditHour, String instructor, String groupID) {
		super();
		this.id = id;
		this.title = title;
		this.term = term;
		this.creditHour = creditHour;
		this.instructor = instructor;
		this.groupID = groupID;
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
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getCreditHour() {
		return creditHour;
	}
	public void setCreditHour(String creditHour) {
		this.creditHour = creditHour;
	}
	public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	private String id;
	private String title;
	private String term;
	private String creditHour;
	private String instructor;
	private String groupID;

}
