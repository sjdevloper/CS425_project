package cs425.api;

public class InstructorGPA {
	public InstructorGPA() {
	}
	
	public InstructorGPA(String min, String max, String instructor) {
		super();
		this.min = min;
		this.max = max;
		this.instructor = instructor;
	}

	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}


	private String min;
	private String max;
	private String instructor;
}
