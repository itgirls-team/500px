package model.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import model.Album;
import model.Comment;
import model.Post;
import model.Tag;
import model.User;
import utils.CommonUtils;

public class AlbumDao {

	private static final String CREATE_ALBUM = "INSERT INTO albums (category, date_upload, picture, user_id ) VALUES (?,?,?,?)";
	private static final String SELECT_ALBUMS_BY_USER = "SELECT album_id, category, picture, user_id FROM albums WHERE user_id = ?";
	// private static final String SELECT_TAGS_FROM_POST = "SELECT t.title FROM
	// post_tag AS p JOIN tags AS t USING (tag_id) WHERE p.post_id = ? ";
	private static final String SELECT_POST_FROM_ALBUM = "SELECT post_id, image, counts_likes, counts_dislikes, description FROM posts WHERE album_id = ?";
	private static final String DELETE_POSTS_FROM_ALBUM = "DELETE FROM posts WHERE album_id = ?";
	private static final String DELETE_ALBUM = "DELETE FROM albums WHERE album_id =?";
	private static final String EXISTS_ALBUM = "SELECT count(*)>0 FROM albums WHERE category LIKE ?";
	private static Connection con = DbManager.getInstance().getConnection();
	private static AlbumDao instance;

	private AlbumDao() {
	}

	public static synchronized AlbumDao getInstance() {
		if (instance == null) {
			instance = new AlbumDao();
		}
		return instance;
	}

	// createAlbum
	public synchronized void createAlbum(Album a) throws SQLException {
		PreparedStatement ps = con.prepareStatement(CREATE_ALBUM, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, a.getCategory());
		ps.setDate(2, Date.valueOf(LocalDate.now()));
		ps.setString(3, a.getPicture());
		ps.setLong(4, a.getUser());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		a.setId(rs.getLong(1));
	}

	// getAllAlbumFromUser
	public Set<Album> getAllAlbumFromUser(long userId) throws SQLException {
		PreparedStatement ps = con.prepareStatement(SELECT_ALBUMS_BY_USER);
		ps.setLong(1, userId);
		ResultSet rs = ps.executeQuery();
		HashSet<Album> albums = new HashSet<>();
		while (rs.next()) {
			HashSet<Post> posts = new HashSet<>();
			PreparedStatement ps_posts = con.prepareStatement(SELECT_POST_FROM_ALBUM);
			ps_posts.setLong(1, rs.getLong("album_id"));
			ResultSet rs1 = ps_posts.executeQuery();
			while (rs.next()) {
				long postId = rs.getLong("post_id");
				String url = rs.getString("image");
				String description = rs.getString("description");
				int countLikes = rs.getInt("counts_likes");
				int countDislikes = rs.getInt("counts_dislikes");
				Set<Tag> tags = TagDao.getInstance().getAllTagsFromPost(postId);
				int albumId = rs.getInt("album_id");
				Set<Comment> commentsOfPost = CommentDao.getInstance().getAllComments(rs.getLong("post_id"));
				Set<User> usersWhoLike = PostDao.getInstance().getAllUsersWhoLikePost(postId);
				Set<User> usersWhoDislike = PostDao.getInstance().getAllUsersWhoDislikePost(postId);
				posts.add(new Post(postId, url, description, countLikes, countDislikes, tags, albumId, commentsOfPost,
						usersWhoLike, usersWhoDislike));
			}
			albums.add(new Album(rs.getLong("album_id"), rs.getString("category"), rs.getString("picture"), userId,
					posts));
		}
		return albums;
	}

	// deleteAlbum
	public void deleteAlbum(Album a) throws SQLException {
		PreparedStatement deletePosts = null;
		PreparedStatement deleteAlbum = null;
		try {
			con.setAutoCommit(false);
			deletePosts = con.prepareStatement(DELETE_POSTS_FROM_ALBUM);
			deletePosts.setLong(1, a.getId());
			deletePosts.executeUpdate();

			deleteAlbum = con.prepareStatement(DELETE_ALBUM);
			deleteAlbum.setLong(1, a.getId());
			deleteAlbum.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.err.print("Transaction is being rolled back");
			con.rollback();
			throw e;
		} finally {
			if (deletePosts != null) {
				deletePosts.close();
			}
			if (deleteAlbum != null) {
				deleteAlbum.close();
			}
			con.setAutoCommit(true);
		}
	}

	public synchronized boolean existAlbum(String category) throws SQLException {
		PreparedStatement ps = null;
		boolean albumExists = true;
		if (!CommonUtils.isValidString(category)) {
			albumExists = false;
		}
		ps = con.prepareStatement(EXISTS_ALBUM);
		ps.setString(1, category);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			albumExists = rs.getBoolean(1);
		}
		if (ps != null) {
			ps.close();
		}
		return albumExists;
	}

}
