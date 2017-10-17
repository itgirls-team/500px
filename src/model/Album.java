package model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Album {

	private long id;
	private String category;
	private LocalDate dateOfUpload;
	private long userId;
	private Set<Post> posts = new HashSet<Post>();

	public Album(String category, long user) {
		this.category = category;
		this.dateOfUpload = LocalDate.now();
		this.userId = user;
	}

	public Album(long id, String category, long user) {
		this(category, user);
		this.id = id;
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
