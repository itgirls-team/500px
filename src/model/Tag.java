package model;

import java.util.HashSet;
import java.util.Set;

public class Tag {

	private long id;
	private String title;

	public Tag(String title) {
		this.title = title;
	}

	public Tag(long id, String title) {
		this(title);
		this.id = id;
	}

	// Getters
	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	// Setters
	public void setTagId(long id) {
		this.id = id;
	}

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
		Tag other = (Tag) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tag [title=" + title + "]";
	}

}
