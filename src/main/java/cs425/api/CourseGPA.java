package cs425.api;

public class CourseGPA {
	public CourseGPA() {
		
	}
	
	public CourseGPA(Integer creditHour, String grade) {
		super();
		this.creditHour = creditHour;
		this.grade = grade;
	}
	public Integer getCreditHour() {
		return creditHour;
	}
	public void setCreditHour(Integer creditHour) {
		this.creditHour = creditHour;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	private Integer creditHour;
	private String grade;
}
