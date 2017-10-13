package model;

import java.util.HashSet;
import java.util.Set;

public class Tag {

	private int id;
	private String title;
	private Set<Post> postsOfTag = new HashSet<Post>();
	
	
	
	public Tag(int id, String title, Set<Post> postsOfTag) {
		super();
		this.id = id;
		this.title = title;
		this.postsOfTag = postsOfTag;
	}


	public Tag(String title, Set<Post> postsOfTag) {
		this.title = title;
		this.postsOfTag = postsOfTag;
	}

	//Getters
	public int getId() {
		return id;
	}


	public String getTitle() {
		return title;
	}


	public Set<Post> getPostsOfTag() {
		return postsOfTag;
	}

	//Setters
	
	public void setTagId(int id) {
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
		Tag other = (Tag) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
		
}
