package model;

import java.sql.Date;
import java.util.Collections;
import java.util.Set;

public class Album {

	private int id;
	private String category;
	private Date dateOfUpload;
	private User user;
	private Set<Post> posts;
	
	public Album(int id, String category, Date dateOfUpload, User user, Set<Post> posts) {
		this.id = id;
		this.category = category;
		this.dateOfUpload = dateOfUpload;
		this.user = user;
		this.posts = posts;
	}
	
	public Album(String category, Date dateOfUpload, User user, Set<Post> posts) {
		this.category = category;
		this.dateOfUpload = dateOfUpload;
		this.user = user;
		this.posts = posts;
	}
	
	//Getters
	
	public int getId() {
		return id;
	}
	public String getCategory() {
		return category;
	}
	public Date getDateOfUpload() {
		return dateOfUpload;
	}
	public User getUser() {
		return user;
	}
	public Set<Post> getPosts() {
		return Collections.unmodifiableSet(posts);
	}
	
	//Setters
	
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
		Album other = (Album) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
