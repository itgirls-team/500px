package model;

import java.time.LocalDateTime;

public class Comment {

	private long id;
	private long userId;
	private String description;
	private LocalDateTime dateAndTimeOfUpload;
	private Post post;
	private int numberOfLikes;
	private int numberOfDislikes;
	//private HashSet<User> usersLikedTheComment = new HashSet<User>();

	public Comment(long user, String description, Post post) {
		this.userId = user;
		this.description = description;
		this.post = post;
		dateAndTimeOfUpload = LocalDateTime.now();
	}

	public Comment(long id, long user, String description, Post post) {
		this(user, description, post);
		this.id = id;
	}

	public Comment(long commentId, long userId, String description, LocalDateTime dateAndTimeOfUpload,
			int numberOfLikes, int numberOfDislikes) {
		//this.user.setId(userId);
		this.userId = userId;
		this.id = commentId;
		this.description = description;
		this.numberOfLikes = numberOfLikes;
		this.numberOfDislikes = numberOfDislikes;
		this.dateAndTimeOfUpload = dateAndTimeOfUpload;
	}


	/*public Comment(long id, long user, String description, Post post, HashSet<User> usersLikedTheComment) {
		this(id, user, description, post);
		//this.usersLikedTheComment = usersLikedTheComment;
	}
	*/

	public long getId() {
		return id;
	}

	public long getUser() {
		return userId;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", userId=" + userId + ", description=" + description + ", dateAndTimeOfUpload="
				+ dateAndTimeOfUpload + ", post=" + post + ", numberOfLikes=" + numberOfLikes + ", numberOfDislikes="
				+ numberOfDislikes + ", usersLikedTheComment=" + "]";
	}

	public LocalDateTime getdateAndTimeOfUpload() {
		return dateAndTimeOfUpload;
	}

	public Post getPost() {
		return post;
	}

	/*
	public Set<User> getUsersLikedTheComment() {
		return Collections.unmodifiableSet(usersLikedTheComment);
	}*/


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
