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
import java.util.List;
import java.util.Map;

import model.Post;
import model.Tag;
import model.User;

public class PostDao {

	private static final String UPLOAD_POST = "INSERT into posts (image, counts_likes, counts_dislikes, description, date_upload, album_id) VALUES (?,?,?,?,?,?);";
	private static final String SELECT_POSTS_BY_ALBUM = "SELECT post_id,image,counts_likes,counts_dislikes,description,album_id FROM posts WHERE album_id = ?";
	private static final String SELECT_COUNT_OF_POSTS_IN_ALBUMS = "SELECT COUNT(P.post_id) AS posts, A.category FROM posts P INNER JOIN albums A USING(album_id) GROUP BY album_id";
	private static final String UPDATE_LIKES = "UPDATE counts_like SET ? WHERE post_id = ? AND user_id = ?";
	private static final String UPDATE_DISLIKES = "UPDATE counts_dislike SET ? WHERE post_id = ? AND user_id = ?";
	private static final String SELECT_TAGS_FROM_POST = "SELECT t.title FROM post_tag AS p JOIN tags AS t USING (tag_id) WHERE p.post_id = ? ";
	// private static final String SELECT_COUNT_OF_POST = "SELECT COUNT(*) FROM
	// posts WHERE image=?";

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

	// insertPostInDB
	public void uploadPost(String image, int counts_like, int counts_dislike, String description, int album_id)
			throws SQLException {
		PreparedStatement ps = con.prepareStatement(UPLOAD_POST, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, image);
		ps.setInt(2, counts_like);
		ps.setInt(3, counts_dislike);
		ps.setString(4, description);
		ps.setDate(5, Date.valueOf(LocalDate.now()));
		ps.setInt(6, album_id);
		ps.executeUpdate();
	}

	public void uploadPost(Post p) throws SQLException {
		con.setAutoCommit(false);
		PreparedStatement ps = con.prepareStatement(UPLOAD_POST, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, p.getPath());
		ps.setInt(2, p.getCountsOfLikes());
		ps.setInt(3, p.getCountsOfDislikes());
		ps.setString(4, p.getDescription());
		ps.setDate(5, Date.valueOf(LocalDate.now()));
		ps.setInt(6, p.getAlbumId());
		ps.executeUpdate();

		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		p.setPostId(rs.getLong(1));

		TagDao.getInstance().insertPostTags(p);

		con.commit();
		con.setAutoCommit(true);
	}

	// getAllPostFromAlbum
	public HashSet<Post> getAllPostsFromAlbum(int album_id) throws SQLException {
		PreparedStatement ps = con.prepareStatement(SELECT_POSTS_BY_ALBUM);
		ps.setInt(1, album_id);
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

	// updateLikes
	public void like(Post p, User u) throws SQLException {
		PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(UPDATE_LIKES);
		ps.setInt(1, p.getCountsOfLikes() + 1);
		ps.setLong(2, p.getId());
		ps.setLong(3, u.getId());
		ps.executeUpdate();
	}

	// updateDislikes
	public void disLike(Post p, User u) throws SQLException {
		PreparedStatement ps = DBManager.getInstance().getConnection().prepareStatement(UPDATE_DISLIKES);
		ps.setInt(1, p.getCountsOfDislikes() + 1);
		ps.setLong(2, p.getId());
		ps.setLong(3, u.getId());
		ps.executeUpdate();
	}

	// getTagsOnPost - only one but why????
	private HashSet<Tag> getTags(int post_id) throws SQLException {
		HashSet<Tag> tags = new HashSet<>();
		PreparedStatement ps = con.prepareStatement(SELECT_TAGS_FROM_POST);
		ps.setInt(1, post_id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			tags.add(new Tag(rs.getString("title")));
		}
		return tags;
	}

	// deletePost
	// TODO

	public static void main(String[] args) throws SQLException {
		// PostDao.getInstance().uploadPost(new Post("url4", "nice", 1));
		System.out.println("ok");
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

	}

}
