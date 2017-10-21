package model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Post {

	private long id;
	private String path;
	private int countsOfLikes;
	private int countsOfDislikes;
	private LocalDate dateOfUpload;
	private String description;
	private long albumId;
	private Set<Comment> commentsOfPost;
	private Set<User> usersWhoLike;
	private Set<User> usersWhoDislike;
	private Set<Tag> tagsOfPost;

	public Post(String path, String description, int countsOfLikes, int countsOfDislikes,
			Set<Tag> tags, long album_id ,Set<Comment> commentsOfPost, Set<User> usersWhoLike, Set<User> usersWhoDislike) {
		this.path = path;
		this.countsOfLikes = countsOfLikes;
		this.countsOfDislikes = countsOfDislikes;
		this.dateOfUpload = LocalDate.now();
		this.description = description;
		this.tagsOfPost = tags;
		this.albumId = album_id;
		this.commentsOfPost = commentsOfPost;
		this.usersWhoLike = usersWhoLike;
		this.usersWhoDislike = usersWhoDislike;
	}

	public Post(long id, String path, String description,int countsOfLikes, int countsOfDislikes,
			Set<Tag> tags, long album_id, Set<Comment> commentsOfPost, Set<User> usersWhoLike, Set<User> usersWhoDislike) {
		this(path, description,countsOfLikes,countsOfDislikes, tags, album_id,commentsOfPost,usersWhoLike,usersWhoDislike);
		this.id = id;
	}
	
	public Post(String path, String description,Set<Tag> tags, long album_id){
		this(path,description,0,0,tags,album_id, new HashSet<>(), new HashSet<>(), new HashSet<>());
	}
	
	public Post(long id,String path, String description,Set<Tag> tags, long album_id){
		this(path,description,0,0,tags,album_id, new HashSet<>(), new HashSet<>(), new HashSet<>());
		this.id = id;
	}
	
	// Getters
	public long getId() {
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

	public LocalDate getDateOfUpload() {
		return dateOfUpload;
	}

	public String getDescription() {
		return description;
	}

	public Set<Comment> getCommentsOfPost() {
		return Collections.unmodifiableSet(commentsOfPost);
	}

	public long getAlbumId() {
		return albumId;
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

	// Setter
	public void setPostId(long id) {
		this.id = id;
	}

	public void setCountsOfLikes(int countsOfLikes) {
		this.countsOfLikes = countsOfLikes;
	}

	public void setCountsOfDislikes(int countsOfDislikes) {
		this.countsOfDislikes = countsOfDislikes;
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
		Post other = (Post) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
