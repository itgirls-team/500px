package model;

import java.sql.Date;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Post {

	private int id;
	private String path;
	private int countsOfLikes;
	private int countsOfDislikes;
	private Date dateOfUpload;
	private String description;
	private Album album;
	private Set<Comment> commentsOfPost=new HashSet<>();
	private Set<User> usersWhoLike = new HashSet<User>();
	private Set<User> usersWhoDislike = new HashSet<User>();
	private Set<Tag> tagsOfPost = new HashSet<Tag>();
	
	
	public Post(int id,String path, int countsOfLikes, int countsOfDislikes, Date dateOfUpload,
			String description, Set<Comment> commentsOfPost, Album album, Set<User> usersWhoLike,
			Set<User> usersWhoDislike, Set<Tag> tagsOfPost) {
		this.id = id;
		this.path = path;
		this.countsOfLikes = countsOfLikes;
		this.countsOfDislikes = countsOfDislikes;
		this.dateOfUpload = dateOfUpload;
		this.description = description;
		this.commentsOfPost = commentsOfPost;
		this.album = album;
		this.usersWhoLike = usersWhoLike;
		this.usersWhoDislike = usersWhoDislike;
		this.tagsOfPost = tagsOfPost;
	}
	
	public Post(String path, int countsOfLikes, int countsOfDislikes, Date dateOfUpload,
			String description, Set<Comment> commentsOfPost, Album album, Set<User> usersWhoLike,
			Set<User> usersWhoDislike, Set<Tag> tagsOfPost) {
		this.path = path;
		this.countsOfLikes = countsOfLikes;
		this.countsOfDislikes = countsOfDislikes;
		this.dateOfUpload = dateOfUpload;
		this.description = description;
		this.commentsOfPost = commentsOfPost;
		this.album = album;
		this.usersWhoLike = usersWhoLike;
		this.usersWhoDislike = usersWhoDislike;
		this.tagsOfPost = tagsOfPost;
	}



	//Getters
	public int getId() {
		return id;
	}


	public String getPath() {
		return path;
	}


	public int getCountsOfLikes() {
		return countsOfLikes;
	}


	public int getCountsOfDislikes() {
		return countsOfDislikes;
	}


	public Date getDateOfUpload() {
		return dateOfUpload;
	}


	public String getDescription() {
		return description;
	}


	public Set<Comment> getCommentsOfPost() {
		return Collections.unmodifiableSet(commentsOfPost);
	}


	public Album getAlbum() {
		return album;
	}


	public Set<User> getUsersWhoLike() {
		return Collections.unmodifiableSet(usersWhoLike);
	}


	public Set<User> getUsersWhoDislike() {
		return Collections.unmodifiableSet(usersWhoDislike);
	}

	
	public Set<Tag> getTagsOfPost() {
		return Collections.unmodifiableSet(tagsOfPost);
	}
	
	//Setter
	public void setPostId(int id) {
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
		Post other = (Post) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
