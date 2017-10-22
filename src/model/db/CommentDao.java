package model.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import model.Comment;
import model.Post;
import model.User;

public class CommentDao {

	private static final String ADD_COMMENT = "INSERT INTO comments (date_upload, description,post_id, user_id) VALUES (?,?,?,?)";
	private static final String DELETE_COMMENT = "DELETE FROM comments WHERE comment_id=?";
	private static final String DELETE_ALL_COMMENTS = "DELETE FROM comments WHERE comment_id = ?";
	private static final String LIKE_COMMENT = "INSERT INTO users_like_comments (user_id,comment_id) VALUES (?,?)";
	
	
			private static Connection con = DbManager.getInstance().getConnection();
	private static CommentDao instance;

	private CommentDao() {
	}

	public static synchronized CommentDao getInstance() {
		if (instance == null) {
			instance = new CommentDao();
		}
		return instance;
	}

	public synchronized void addComment(Comment comment) throws SQLException {
		PreparedStatement ps = con.prepareStatement(ADD_COMMENT);
		ps.setDate(1, Date.valueOf(LocalDate.now()));
		ps.setString(2, comment.getDescription());
		ps.setLong(3, comment.getPost().getId());
		ps.setLong(4, comment.getUser());
		ps.executeUpdate();

		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		long id = rs.getLong(1);
		comment.setId(id);
		comment.setdateAndTimeOfUpload(LocalDateTime.now());

		if (ps != null) {
			ps.close();
		}
		if (rs != null) {
			rs.close();
		}
	}

	// delete comment
	public synchronized void deleteComment(Comment c) throws SQLException {
		PreparedStatement ps = con.prepareStatement(DELETE_COMMENT);
		ps.setLong(1, c.getId());
		ps.executeUpdate();
		if (ps != null) {
			ps.close();
		}

	}

	// delete all comments
	public synchronized void deleteComments(Post p) throws SQLException {
		PreparedStatement ps = con.prepareStatement(DELETE_ALL_COMMENTS);
		ps.setLong(1, p.getId());
		ps.executeUpdate();
		if (ps != null) {
			ps.close();
		}
	}

	// like comment
	public synchronized void likeComment(long commentId, String username) throws SQLException {
		PreparedStatement ps = con.prepareStatement(LIKE_COMMENT);

		PreparedStatement selectUserId = con.prepareStatement("SELECT user_id FROM userd WHERE username=?;");
		selectUserId.setString(1, username);
		ResultSet rs = selectUserId.executeQuery();
		rs.next();
		long userId = rs.getLong(1);

		ps.setLong(1, userId);
		ps.setLong(2, commentId);
		ps.executeUpdate();

		if (selectUserId != null) {
			selectUserId.close();
		}
		if (rs != null) {
			rs.close();
		}
		if (ps != null) {
			ps.close();
		}
	}

	public synchronized void removeLikeComment(long commentId, String username) throws SQLException {
		con.setAutoCommit(false);
		try {
			PreparedStatement ps = con
					.prepareStatement("DELETE FROM users_like_comments WHERE user_id=? AND comment_id=?");

			PreparedStatement selectUserId = con.prepareStatement("SELECT user_id FROM userd WHERE username=?;");
			selectUserId.setString(1, username);
			ResultSet rs = selectUserId.executeQuery();
			rs.next();
			long userId = rs.getLong(1);

			ps.setLong(1, userId);
			ps.setLong(2, commentId);
			ps.executeUpdate();

			if (selectUserId != null) {
				selectUserId.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw new SQLException();
		} finally {
			con.setAutoCommit(true);
		}
	}

	public synchronized void addDislike(long commentId, String username) throws SQLException {
		con.setAutoCommit(false);
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO users_dislike_comments (user_id, comment_id) VALUES (?,?);");

			PreparedStatement selectUserId = con.prepareStatement("SELECT user_id FROM userd WHERE username=?;");
			selectUserId.setString(1, username);
			ResultSet rs = selectUserId.executeQuery();
			rs.next();
			long userId = rs.getLong(1);

			ps.setLong(1, userId);
			ps.setLong(2, commentId);
			ps.executeUpdate();

			if (selectUserId != null) {
				selectUserId.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			con.commit();
		} catch (

		SQLException e) {
			con.rollback();
			throw new SQLException();
		} finally {
			con.setAutoCommit(true);
		}
	}

	public synchronized void removeDislike(long commentId, String username) throws SQLException {
		con.setAutoCommit(false);
		try {
			PreparedStatement ps = con
					.prepareStatement("DELETE FROM users_dislike_comments WHERE user_id=? AND comment_id=?");

			PreparedStatement selectUserId = con.prepareStatement("SELECT user_id FROM userd WHERE username=?;");
			selectUserId.setString(1, username);
			ResultSet rs = selectUserId.executeQuery();
			rs.next();
			long userId = rs.getLong(1);

			ps.setLong(1, userId);
			ps.setLong(2, commentId);
			ps.executeUpdate();

			if (selectUserId != null) {
				selectUserId.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			con.commit();
		} catch (

		SQLException e) {
			con.rollback();
			throw new SQLException();
		} finally {
			con.setAutoCommit(true);
		}
	}

	public synchronized void updateNumberOfLikesAndDislikes(long commentId, int numberOfLikes, int numberOfDislikes)
			throws SQLException {
		String updateCommentNumberOfLikesStatement = "UPDATE comments SET number_of_likes=?, number_of_dislikes=? WHERE comment_id=?;";
		PreparedStatement ps = con.prepareStatement(updateCommentNumberOfLikesStatement);
		ps.setInt(1, numberOfLikes);
		ps.setInt(2, numberOfDislikes);
		ps.setLong(3, commentId);
		ps.executeUpdate();

		if (ps != null) {
			ps.close();
		}
	}

	
	public synchronized Set<User> getAllCommentUserLikersFromDB(long comment_id) throws SQLException {
		Set<User> allComentLikers = new HashSet<>();
		con.setAutoCommit(false);
		try {
			String selectAllComentLikersFromDB = "SELECT user_id FROM users_like_comments WHEN comment_id=?;";

			PreparedStatement ps = con.prepareStatement(selectAllComentLikersFromDB);
			ps.setLong(1, comment_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Long userId = rs.getLong("user_id");

				PreparedStatement psOne = con.prepareStatement("SELECT username FROM users WHERE user_id=?;");
				psOne.setLong(1, userId);
				ResultSet rsOne = psOne.executeQuery();
				rsOne.next();
				String username = rsOne.getString(1);

				allComentLikers.add(UserDao.getInstance(con).getUser(username));
			}

			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			con.commit();
		} catch (

		SQLException e) {
			con.rollback();
			throw new SQLException();
		} finally {
			con.setAutoCommit(true);
		}
		return allComentLikers;
	}

	public synchronized Set<User> getAllCommentUserDislikersFromDB(long comment_id) throws SQLException {
		Set<User> allComentDislikers = new HashSet<>();

		String selectAllComentLikersFromDB = "SELECT user_id FROM users_dislike_comments WHEN comment_id=?;";
		con.setAutoCommit(false);
		try {
			PreparedStatement ps = con.prepareStatement(selectAllComentLikersFromDB);
			ps.setLong(1, comment_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Long userId = rs.getLong("user_id");

				PreparedStatement psOne = con.prepareStatement("SELECT username FROM users WHERE user_id=?;");
				psOne.setLong(1, userId);
				ResultSet rsOne = psOne.executeQuery();
				rsOne.next();
				String username = rsOne.getString(1);

				allComentDislikers.add(UserDao.getInstance(con).getUser(username));
			}
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
			con.commit();
		} catch (

		SQLException e) {
			con.rollback();
			throw new SQLException();
		} finally {
			con.setAutoCommit(true);
		}
		return allComentDislikers;
	}
	
	//add get all comments by post id
	public synchronized Set<Comment> getAllComments(long postId) throws SQLException {
		Set<Comment> comments = new HashSet<>();
		PreparedStatement ps = con.prepareStatement(
				"SELECT comment_id,user_id, description,date_upload,number_of_likes, number_of_dislikes FROM comments WHERE post_id = ?");
			ps.setLong(1, postId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Comment comment = new Comment(rs.getLong("comment_id"), rs.getLong("user_id"), rs.getString("description"),
					rs.getTimestamp("date_upload").toLocalDateTime(), rs.getInt("number_of_likes"),
					rs.getInt("number_of_dislikes"));
			comments.add(comment);
		}
		if (ps != null) {
			ps.close();
		}
		if (rs != null) {
			rs.close();
		}
		return comments;
	}
	/*
	public User getUser(long commentId,long userId){
		PreparedStatement ps = con.prepareStatement(
				"SELECT u.user_id,first_name,last_name,email,username,register_date,profile_picture,u.description FROM comments AS c JOIN users AS u ON (c.user_id = u.user_id) WHERE c.user_id = ? AND c.comment_id = ? ");
			ps.setLong(1, userId);
			ps.setLong(2, commentId);
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		User user = new User(rs.getLong("user_id"), rs.getString("first_name"), rs.getString("last_name"),
				rs.getString("email"), rs.getString("username"), rs.getTimestamp("register_date").toLocalDateTime(), rs.getString("profile_picture"),rs.getString("description"));
		
		if (ps != null) {
			ps.close();
		}
		if (rs != null) {
			rs.close();
		}
		return user;
	}*/
}
