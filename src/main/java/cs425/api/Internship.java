package cs425.api;

public class Internship {
	public Internship() {
	}
	
	public Internship(String username, String internshipID,
		String internship, String publicInternship) {
		super();
		this.username = username;
		this.internshipID = internshipID;
		this.internship = internship;
		this.publicInternship = publicInternship;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getInternshipID() {
		return internshipID;
	}
	public void setInternshipID(String internshipID) {
		this.internshipID = internshipID;
	}
	public String getInternship() {
		return internship;
	}
	public void setInternship(String internship) {
		this.internship = internship;
	}

	
	public String getPublicInternship() {
		return publicInternship;
	}

	public void setPublicInternship(String publicInternship) {
		this.publicInternship = publicInternship;
	}


	private String username;
	private String internshipID;
	private String internship;
	private String publicInternship;
}
