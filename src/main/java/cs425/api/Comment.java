package cs425.api;

public class Comment {
	public Comment() {
	}
		
	public Comment(Integer id, String topic, String message, 
			String author, String forumID, Integer bonus) {
		super();
		this.id = id;
		this.topic = topic;
		this.message = message;
		this.author = author;
		this.forumID = forumID;
		this.bonus = bonus;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getForumID() {
		return forumID;
	}
	public void setForumID(String forumID) {
		this.forumID = forumID;
	}
	public Integer getBonus() {
		return bonus;
	}
	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	private Integer id;
	private String topic;
	private String message;
	private String author;
	private String forumID;
	private Integer bonus;
	private String title = null; // forum title
}
