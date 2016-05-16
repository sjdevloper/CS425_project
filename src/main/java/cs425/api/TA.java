/**
 * 
 */
package cs425.api;

/**
 * @author Shanshan Jiang
 *
 */
public class TA {
	public TA() {
		
	}
	
	public TA(String id, String courseID, String courseTA) {
		super();
		this.id = id;
		this.courseID = courseID;
		this.courseTA = courseTA;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCourseID() {
		return courseID;
	}
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	public String getCourseTA() {
		return courseTA;
	}
	public void setCourseTA(String courseTA) {
		this.courseTA = courseTA;
	}

	private String id;
	private String courseID;
	private String courseTA;
}



