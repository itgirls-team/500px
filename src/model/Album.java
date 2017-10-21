package model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Album {

	private long id;
	private String category;
	private LocalDate dateOfUpload;
	private String picture;
	private long userId;
	private Set<Post> posts;

	@Override
	public String toString() {
		return "Album [id=" + id + ", category=" + category + ", dateOfUpload=" + dateOfUpload + ", picture=" + picture
				+ ", userId=" + userId + ", posts=" + posts + "]";
	}

	public Album(String category, String picture, long user,Set<Post> posts) {
		this.category = category;
		this.dateOfUpload = LocalDate.now();
		this.picture = picture;
		this.userId = user;
		this.posts = posts;
	}

	public Album(long id, String category, String picture, long user,Set<Post> posts ) {
		this(category, picture, user,posts);
		this.id = id;
	}
	
	public Album(String category, String picture,long user) {
		this(category, picture, user, new HashSet<>());
	}
	


	// Getters
	public long getId() {
		return id;
	}

	public String getCategory() {
		return category;
	}

	public LocalDate getDateOfUpload() {
		return dateOfUpload;
	}

	public long getUser() {
		return userId;
	}

	public Set<Post> getPosts() {
		return Collections.unmodifiableSet(posts);
	}
	
	public String getPicture() {
		return picture;
	}

	// Setters
	public void setId(long id) {
		this.id = id;
	}

	// HashCode and Equals

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
