package cs425.api;

public class Student extends User{
	public Student() {
	}
	
	public Student(String username, String name, String password, String email, Integer yearBegan, String semesterBegan, String degreeStatus,
			String degreeType, String publicEmail, String publicYearBegan, String publicSemesterBegan,
			String publicDegreeStatus, String publicDegreeType, String publicGPA) {

		super(username, password, "student");
		
		this.name = name;
		this.email = email;
		this.yearBegan = yearBegan;
		this.semesterBegan = semesterBegan;
		this.degreeStatus = degreeStatus;
		this.degreeType = degreeType;
		this.publicEmail = publicEmail;
		this.publicYearBegan = publicYearBegan;
		this.publicSemesterBegan = publicSemesterBegan;
		this.publicDegreeStatus = publicDegreeStatus;
		this.publicDegreeType = publicDegreeType;
		this.publicGPA = publicGPA;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getYearBegan() {
		return yearBegan;
	}
	public void setYearBegan(Integer yearBegan) {
		this.yearBegan = yearBegan;
	}
	public String getSemesterBegan() {
		return semesterBegan;
	}
	public void setSemesterBegan(String semesterBegan) {
		this.semesterBegan = semesterBegan;
	}
	public String getDegreeStatus() {
		return degreeStatus;
	}
	public void setDegreeStatus(String degreeStatus) {
		this.degreeStatus = degreeStatus;
	}
	public String getDegreeType() {
		return degreeType;
	}
	public void setDegreeType(String degreeType) {
		this.degreeType = degreeType;
	}
	public String getPublicEmail() {
		return publicEmail;
	}
	public void setPublicEmail(String publicEmail) {
		this.publicEmail = publicEmail;
	}
	public String getPublicYearBegan() {
		return publicYearBegan;
	}
	public void setPublicYearBegan(String publicYearBegan) {
		this.publicYearBegan = publicYearBegan;
	}
	public String getPublicSemesterBegan() {
		return publicSemesterBegan;
	}
	public void setPublicSemesterBegan(String publicSemesterBegan) {
		this.publicSemesterBegan = publicSemesterBegan;
	}
	public String getPublicDegreeStatus() {
		return publicDegreeStatus;
	}
	public void setPublicDegreeStatus(String publicDegreeStatus) {
		this.publicDegreeStatus = publicDegreeStatus;
	}
	public String getPublicDegreeType() {
		return publicDegreeType;
	}
	public void setPublicDegreeType(String publicDegreeType) {
		this.publicDegreeType = publicDegreeType;
	}
	public String getPublicGPA() {
		return publicGPA;
	}
	public void setPublicGPA(String publicGPA) {
		this.publicGPA = publicGPA;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}

	private String name; 
	private String email;
	private Integer yearBegan;
	private String semesterBegan;
	private String degreeStatus;
	private String degreeType;
	private String publicEmail;
	private String publicYearBegan;
	private String publicSemesterBegan;
	private String publicDegreeStatus;
	private String publicDegreeType;
	private String publicGPA;
	private double gpa = 0;
}
