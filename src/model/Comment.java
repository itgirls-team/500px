package model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Comment {

	private long id;
	private User user;
	private String description;
	private LocalDateTime dateAndTimeOfUpload;
	private Post post;
	private int numberOfLikes;
	private int numberOfDislikes;
	private HashSet<User> usersLikedTheComment = new HashSet<User>();

	public Comment(User user, String description, Post post) {
		this.user = user;
		this.description = description;
		this.post = post;
	}

	public Comment(long id, User user, String description, Post post) {
		this(user, description, post);
		this.id = id;
	}

	public Comment(long commentId, long userId, String description, LocalDateTime dateAndTimeOfUpload,
			int numberOfLikes, int numberOfDislikes) {
		this.user.setId(userId);
		this.id = commentId;
		this.description = description;
		this.numberOfLikes = numberOfLikes;
		this.numberOfDislikes = numberOfDislikes;
		this.dateAndTimeOfUpload = dateAndTimeOfUpload;
	}

	public Comment(long id, User user, String description, Post post, HashSet<User> usersLikedTheComment) {
		this(id, user, description, post);
		this.usersLikedTheComment = usersLikedTheComment;
	}

	public long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getdateAndTimeOfUpload() {
		return dateAndTimeOfUpload;
	}

	public Post getPost() {
		return post;
	}

	public Set<User> getUsersLikedTheComment() {
		return Collections.unmodifiableSet(usersLikedTheComment);
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setdateAndTimeOfUpload(LocalDateTime dateAndTimeOfUpload) {
		this.dateAndTimeOfUpload = dateAndTimeOfUpload;
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
		Comment other = (Comment) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
