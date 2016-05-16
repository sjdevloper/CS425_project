package cs425.api;

import java.util.Collection;

public class Group {
	public Group() {
	}

	public Group(String id, String name, String type, 
			Collection<String> moderators) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.moderators = moderators;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Collection<String> getModerators() {
		return moderators;
	}
	public void setModerators(Collection<String> moderators) {
		this.moderators = moderators;
	}

	private String id;
	private String name;
	private String type;
	private Collection<String> moderators;
	
}