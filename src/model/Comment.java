package model;

import java.sql.Date;

public class Comment {

	private int id;
	private String username;
	private String description;
	private Date dateOfUpload;
	private Post post;
	
	
	public Comment(int id, String username, String description, Date dateOfUpload, Post post) {
		this.id = id;
		this.username = username;
		this.description = description;
		this.dateOfUpload = dateOfUpload;
		this.post = post;
	}
	
	public Comment(String username, String description, Date dateOfUpload, Post post) {
		this.username = username;
		this.description = description;
		this.dateOfUpload = dateOfUpload;
		this.post = post;
	}
	//Getters
	public int getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getDescription() {
		return description;
	}
	public Date getDateOfUpload() {
		return dateOfUpload;
	}
	public Post getPost() {
		return post;
	}
	
	//Setter
	public void setId(int id) {
		this.id = id;
	}

	//HashCode and Equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	 
}
