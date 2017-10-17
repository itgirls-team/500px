package model.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Album;
import model.Post;
import model.Tag;
import model.User;

public class PostDao {

	private static final String UPLOAD_POST = "INSERT INTO posts (image, counts_likes, counts_dislikes, description, date_upload, album_id) VALUES (?,?,?,?,?,?)";
	private static final String SELECT_POSTS_BY_ALBUM = "SELECT post_id,image,counts_likes,counts_dislikes,description,album_id FROM posts WHERE album_id = ?";
	private static final String SELECT_COUNT_OF_POSTS_IN_ALBUMS = "SELECT COUNT(P.post_id) AS posts, A.category FROM posts P INNER JOIN albums A USING(album_id) GROUP BY album_id";
	private static final String LIKE_POST = "INSERT INTO users_like_posts (post_id,user_id) VALUES (?,?)";
	private static final String DISLIKE_POST = "INSERT INTO users_dislike_posts (post_id,user_id) VALUES (?,?)";
	private static final String UPDATE_LIKES = "UPDATE posts SET counts_likes = counts_likes + 1  WHERE post_id = ?";
	private static final String UPDATE_DISLIKES = "UPDATE posts SET counts_dislikes = counts_dislikes + 1  WHERE post_id = ?";
	private static final String SELECT_USERS_WHO_LIKE_POST = "SELECT username,first_name,last_name,password,email,description,profile_picture,register_date FROM users_like_posts JOIN users  USING (user_id) WHERE post_id = ?";
	private static final String SELECT_TAGS_FROM_POST = "SELECT t.title FROM post_tag AS p JOIN tags AS t USING (tag_id) WHERE p.post_id = ? ";
	private static final String SELECT_POSTS_BY_TAG = "SELECT image, counts_likes, counts_dislikes, description, date_upload, album_id FROM posts JOIN post_tag USING(post_id) JOIN tags USING (tag_id) WHERE title = ?";
	private static final String SELECT_POSTS_ORDER_BY_LIKES = "SELECT post_id,image,counts_likes,counts_dislikes,description,album_id, SUM(counts_likes) AS likes FROM posts JOIN users_like_posts USING (post_id) GROUP BY post_id ORDER BY SUM(counts_likes) DESC";
	private static final String SELECT_POSTS_ORDER_BY_DATE = "SELECT * FROM posts ORDER BY date_upload DESC";
	private static final String DELETE_POST = "DELETE FROM posts WHERE post_id = ?";
	private static final String DELETE_ALL_TAGS_FROM_POST = "DELETE FROM post_tag WHERE post_id = ?";
	private static final String DELETE_ALL_COMMENTS_FROM_POST = "DELETE FROM comments WHERE post_id = ?";
	private static final String REMOVE_LIKE_OF_A_POST = "DELETE FROM users_like_posts WHERE  post_id = ? AND user_id = ?";

	private static PostDao instance;
	private static Connection con = DBManager.getInstance().getConnection();

	private PostDao() {
	}

	public static synchronized PostDao getInstance() {
		if (instance == null) {
			instance = new PostDao();
		}
		return instance;
	}

	// insertPost
	public synchronized void uploadPost(Post p) throws SQLException {
		PreparedStatement ps = con.prepareStatement(UPLOAD_POST, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, p.getPath());
		ps.setInt(2, p.getCountsOfLikes());
		ps.setInt(3, p.getCountsOfDislikes());
		ps.setString(4, p.getDescription());
		ps.setDate(5, Date.valueOf(LocalDate.now()));
		ps.setLong(6, p.getAlbumId());
		ps.executeUpdate();

		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		p.setPostId(rs.getLong(1));

		TagDao.getInstance().insertPostTags(p);
	}

	// getAllPostFromAlbum
	public HashSet<Post> getAllPostsFromAlbum(Album album) throws SQLException {
		PreparedStatement ps = con.prepareStatement(SELECT_POSTS_BY_ALBUM);
		ps.setLong(1, album.getId());
		ResultSet rs = ps.executeQuery();
		HashSet<Post> posts = new HashSet<>();
		while (rs.next()) {
			HashSet<Tag> tags = new HashSet<>();
			PreparedStatement ps_tags = con.prepareStatement(SELECT_TAGS_FROM_POST);
			ps_tags.setLong(1, rs.getLong("post_id"));
			ResultSet rs1 = ps_tags.executeQuery();
			while (rs1.next()) {
				tags.add(new Tag(rs1.getString("title")));
			}
			posts.add(new Post(rs.getLong("post_id"), rs.getString("image"), rs.getInt("counts_likes"),
					rs.getInt("counts_dislikes"), rs.getString("description"), tags, rs.getInt("album_id")));
		}
		return posts;
	}

	// getAllPostFrom“ag - not work
	public HashSet<Post> getAllPostsFromTag(String tag) throws SQLException {
		PreparedStatement ps = con.prepareStatement(SELECT_POSTS_BY_TAG);
		Tag t = TagDao.getInstance().getTag(tag);
		ps.setLong(1, t.getId());
		ResultSet rs = ps.executeQuery();
		HashSet<Post> posts = new HashSet<>();
		while (rs.next()) {
			HashSet<Tag> tags = new HashSet<>();
			PreparedStatement ps_tags = con.prepareStatement(SELECT_TAGS_FROM_POST);
			ps_tags.setLong(1, rs.getLong("post_id"));
			ResultSet rs1 = ps_tags.executeQuery();
			while (rs1.next()) {
				tags.add(new Tag(rs1.getString("title")));
			}
			posts.add(new Post(rs.getLong("post_id"), rs.getString("image"), rs.getInt("counts_likes"),
					rs.getInt("counts_dislikes"), rs.getString("description"), tags, rs.getInt("album_id")));
		}
		return posts;
	}

	// number of posts in album
	public Map<String, Integer> getPostsNumberInAlbum() {
		Map<String, Integer> albumNumber = new HashMap<String, Integer>();
		try {
			Statement st = DBManager.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery(SELECT_COUNT_OF_POSTS_IN_ALBUMS);
			while (rs.next()) {
				albumNumber.put(rs.getString("category"), rs.getInt("posts"));
			}
		} catch (SQLException e) {
			System.err.println("Error, cannot make postsDAO statement for album.");
		}
		return albumNumber;
	}

	// like post
	public void like(Post p, User u) throws SQLException {
		PreparedStatement ps = con.prepareStatement(LIKE_POST, Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, p.getId());
		ps.setLong(2, u.getId());
		ps.executeUpdate();

		PreparedStatement ps1 = con.prepareStatement(UPDATE_LIKES);
		ps1.setLong(1, p.getId());
		ps.executeUpdate();
	}

	// dislike post
	public void dislike(Post p, User u) throws SQLException {
		PreparedStatement ps = con.prepareStatement(DISLIKE_POST, Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, p.getId());
		ps.setLong(2, u.getId());
		ps.executeUpdate();

		PreparedStatement ps1 = con.prepareStatement(UPDATE_DISLIKES);
		ps1.setLong(1, p.getId());
		ps.executeUpdate();
	}

	// remove like of a post
	public void removePostLike(Post p, User u) throws SQLException {
		String query = REMOVE_LIKE_OF_A_POST;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(query);
			ps.setLong(1, p.getId());
			ps.setLong(2, u.getId());
			ps.executeUpdate();
		} finally {
			ps.close();
		}
	}

	// get all users who like this post
	public HashSet<User> getAllUsersWhoLikePost(Post p) throws SQLException {
		PreparedStatement ps = con.prepareStatement(SELECT_USERS_WHO_LIKE_POST);
		ps.setLong(1, p.getId());
		ResultSet rs = ps.executeQuery();
		HashSet<User> users = new HashSet<>();
		while (rs.next()) {
			users.add(new User(rs.getLong("user_id"), rs.getString("username"), rs.getString("first_name"),
					rs.getString("last_name"), rs.getString("password"), rs.getString("email"),
					rs.getString("description"), rs.getString("profile_picture"), rs.getDate("date").toLocalDate()));
		}
		return users;
	}

	// getTagsOnPost - only one but why????
	private HashSet<Tag> getTags(int post_id) throws SQLException {
		HashSet<Tag> tags = new HashSet<>();
		PreparedStatement ps = con.prepareStatement(SELECT_TAGS_FROM_POST);
		ps.setLong(1, post_id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			tags.add(new Tag(rs.getString("title")));
		}
		return tags;
	}

	// sort by like
	public LinkedHashSet<Post> getAllPostOrderByLikes() throws SQLException {
		PreparedStatement ps = con.prepareStatement(SELECT_POSTS_ORDER_BY_LIKES);
		ResultSet rs = ps.executeQuery();
		LinkedHashSet<Post> posts = new LinkedHashSet<>();
		while (rs.next()) {
			HashSet<Tag> tags = new HashSet<>();
			PreparedStatement ps_tags = con.prepareStatement(SELECT_TAGS_FROM_POST);
			ps_tags.setLong(1, rs.getLong("post_id"));
			ResultSet rs1 = ps_tags.executeQuery();
			while (rs1.next()) {
				tags.add(new Tag(rs1.getString("title")));
			}
			posts.add(new Post(rs.getLong("post_id"), rs.getString("image"), rs.getInt("counts_likes"),
					rs.getInt("counts_dislikes"), rs.getString("description"), tags, rs.getInt("album_id")));
		}
		return posts;

	}

	// sort by date
	public LinkedHashSet<Post> getAllPostOrderByDate() throws SQLException {
		PreparedStatement ps = con.prepareStatement(SELECT_POSTS_ORDER_BY_DATE);
		ResultSet rs = ps.executeQuery();
		LinkedHashSet<Post> posts = new LinkedHashSet<>();
		while (rs.next()) {
			HashSet<Tag> tags = new HashSet<>();
			PreparedStatement ps_tags = con.prepareStatement(SELECT_TAGS_FROM_POST);
			ps_tags.setLong(1, rs.getLong("post_id"));
			ResultSet rs1 = ps_tags.executeQuery();
			while (rs1.next()) {
				tags.add(new Tag(rs1.getString("title")));
			}
			posts.add(new Post(rs.getLong("post_id"), rs.getString("image"), rs.getInt("counts_likes"),
					rs.getInt("counts_dislikes"), rs.getString("description"), tags, rs.getInt("album_id")));
		}
		return posts;

	}

	// deletePost
	public void deletePost(Post p) throws SQLException {
		PreparedStatement deleteComments = null;
		PreparedStatement deleteTags = null;
		PreparedStatement deletePost = null;
		try {
			con.setAutoCommit(false);
			deleteComments = con.prepareStatement(DELETE_ALL_COMMENTS_FROM_POST);
			deleteComments.setLong(1, p.getId());
			deleteComments.executeUpdate();

			deleteTags = con.prepareStatement(DELETE_ALL_TAGS_FROM_POST);
			deleteTags.setLong(1, p.getId());
			deleteTags.executeUpdate();

			deletePost = con.prepareStatement(DELETE_POST);
			deletePost.setLong(1, p.getId());
			deletePost.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			System.err.print("Transaction is being rolled back");
			con.rollback();
		} finally {
			if (deleteComments != null) {
				deleteComments.close();
			}
			if (deleteTags != null) {
				deleteTags.close();
			}
			if (deletePost != null) {
				deletePost.close();
			}
			con.setAutoCommit(true);
		}
	}

	public static void main(String[] args) throws SQLException {
		// PostDao.getInstance().uploadPost(new Post("url4", "nice", 1));
		// System.out.println("ok");
		// HashSet<Post> posts = PostDao.getInstance().getAllPostsFromAlbum(1);
		// for(Post p : posts){
		// System.out.println(p);
		// }

		// Map<String, Integer> get =
		// PostDao.getInstance().getPostsNumberInAlbum();
		// for(Map.Entry<String, Integer> p : get.entrySet()){
		// System.out.println(p);
		// }

		// HashSet<Tag> getTag = PostDao.getInstance().getTags(2);
		// for(Tag t : getTag){
		// System.out.println(t);
		// }

		// HashSet<Post> posts =
		// PostDao.getInstance().getAllPostsFromTag("nice");
		// for (Post p : posts) {
		// System.out.println(p);
		// }
	}

}
