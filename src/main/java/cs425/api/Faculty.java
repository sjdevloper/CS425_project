package cs425.api;

public class Faculty extends User{
	public Faculty() {
	}
	
	
	public Faculty(String username, String name, String password, 
			String email, String position, Integer yearJoined, 
			String experience, String publicEmail, String publicPosition,
			String publicYearJoined, String publicExperience) {
		
		super(username, password, "faculty");
		
		this.username = username;
		this.name = name;
		this.email = email;
		this.position = position;
		this.yearJoined = yearJoined;
		this.experience = experience;
		this.publicEmail = publicEmail;
		this.publicPosition = publicPosition;
		this.publicYearJoined = publicYearJoined;
		this.publicExperience = publicExperience;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getYearJoined() {
		return yearJoined;
	}
	public void setYearJoined(Integer yearJoined) {
		this.yearJoined = yearJoined;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getPublicEmail() {
		return publicEmail;
	}
	public void setPublicEmail(String publicEmail) {
		this.publicEmail = publicEmail;
	}
	public String getPublicPosition() {
		return publicPosition;
	}
	public void setPublicPosition(String publicPosition) {
		this.publicPosition = publicPosition;
	}
	public String getPublicYearJoined() {
		return publicYearJoined;
	}
	public void setPublicYearJoined(String publicYearJoined) {
		this.publicYearJoined = publicYearJoined;
	}
	public String getPublicExperience() {
		return publicExperience;
	}
	public void setPublicExperience(String publicExperience) {
		this.publicExperience = publicExperience;
	}

	private String username;
	private String name;
	private String email;
	private String position; 
	private Integer yearJoined;
	private String experience;
	private String publicEmail; 
	private String publicPosition; 
	private String publicYearJoined;
	private String publicExperience;
}
