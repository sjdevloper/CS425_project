package cs425.api;

public class Project {
	public Project() {
	}
	
	public Project(String username, String projectID,
		String project, String publicProject) {
		this.username = username;
		this.projectID = projectID;
		this.project = project;
		this.publicProject = publicProject;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getPublicProject() {
		return publicProject;
	}

	public void setPublicProject(String publicProject) {
		this.publicProject = publicProject;
	}

	private String username;
	private String projectID;
	private String project;
	private String publicProject;
}
