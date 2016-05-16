/**
 * 
 */
package cs425.api;

/**
 * @author Shanshan Jiang
 *
 */
public class Take {
	public Take() {
	}

	public Take(String student, String courseID, String grade, String approved, 
			Integer totalBonus) {
		super();
		this.student = student;
		this.courseID = courseID;
		this.grade = grade;
		this.approved = approved;
		this.totalBonus = totalBonus;
	}

	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
	}
	public String getCourseID() {
		return courseID;
	}
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getApproved() {
		return approved;
	}
	public void setApproved(String approved) {
		this.approved = approved;
	}
	public Integer getTotalBonus() {
		return totalBonus;
	}
	public void setTotalBonus(Integer totalBonus) {
		this.totalBonus = totalBonus;
	}

	private String student;
	private String courseID;
	private String grade;
	private String approved;
	private Integer totalBonus;
}
