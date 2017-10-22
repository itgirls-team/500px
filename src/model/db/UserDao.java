package model.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import model.User;
import utils.CommonUtils;

public class UserDao {

	private static UserDao instance;
	private static Connection connection;

	private UserDao() {
	}

	public static synchronized UserDao getInstance(Connection con) {
		if (instance == null) {
			instance = new UserDao();
		}
		connection = con;
		return instance;
	}

	public synchronized void insertUser(String userName, String password, String email, String firstName,
			String lastName, String description, String pictureName) throws SQLException {
		PreparedStatement ps = null;
		ps = connection.prepareStatement(
				"INSERT INTO users (first_name,last_name,email,username,password,register_date,profile_picture,description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setString(1, firstName);
		ps.setString(2, lastName);
		ps.setString(3, email);
		ps.setString(4, userName);
		ps.setString(5, password);
		ps.setDate(6, Date.valueOf(LocalDate.now()));
		ps.setString(7, pictureName);
		ps.setString(8, description);
		ps.executeUpdate();

		if (ps != null) {
			ps.close();
		}
	}

	public synchronized boolean existUser(String userName) throws SQLException {
		PreparedStatement ps = null;
		boolean userExists = true;
		if (!CommonUtils.isValidString(userName)) {
			userExists = false;
		}
		ps = connection.prepareStatement("SELECT count(*)>0 FROM users WHERE username=?;");
		ps.setString(1, userName);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			userExists = rs.getBoolean(1);
		}
		if (ps != null) {
			ps.close();
		}
		if (rs != null) {
			rs.close();
		}
		return userExists;
	}

	public synchronized String deleteUser(String username) throws SQLException {
		if (!CommonUtils.isValidString(username)) {
			return "Invalid username";
		}
		PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE username=?;");
		ps.setString(1, username);
		ps.executeUpdate();

		if (ps != null) {
			ps.close();
		}
		return "User deleted successfully";
	}

	public synchronized User getUser(String username) throws SQLException {
		User user = null;
		if (existUser(username)) {
			PreparedStatement ps = connection.prepareStatement(
					"SELECT first_name,last_name,email,username,register_date,profile_picture,description FROM users WHERE username=?;");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			rs.next();
			user = new User(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"),
					rs.getString("username"), rs.getDate("register_date").toLocalDate(),
					rs.getString("profile_picture"), rs.getString("description"));

			// Set<Album> albumsOfUser =
			// AlbumDao.getInstance().getAllAlbumFromUser(user);
			// user.setAlbumsOfUser(albumsOfUser);

			Set<User> followers = getAllFollowersForUser(username);
			user.setFollowers(followers);

			Set<User> following = getAllFollowedForUser(username);
			user.setFollowing(following);

			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return user;
	}

	
	public synchronized Set<User> getAllFollowersForUser(String username) throws SQLException {

		Set<User> allFollowers = new HashSet();

		if (!CommonUtils.isValidString(username)) {
			return null;
		}
		connection.setAutoCommit(false);
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT user_id FROM users WHERE username=?;");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Long userId = rs.getLong(1);

			String selectAllFollowersFromDB = "SELECT follower_id FROM user_follower WHERE user_id=?;";

			PreparedStatement psOne = connection.prepareStatement(selectAllFollowersFromDB);
			psOne.setLong(1, userId);
			ResultSet rsOne = psOne.executeQuery();
			while (rsOne.next()) {
				Long followerId = rsOne.getLong("follower_id");

				PreparedStatement psTwo = connection.prepareStatement("SELECT username FROM users WHERE user_id=?;");
				psTwo.setLong(1, followerId);
				ResultSet rsTwo = psTwo.executeQuery();
				rsTwo.next();
				String usernameOfFollower = rsTwo.getString(1);

				allFollowers.add(getBasicUser(usernameOfFollower));
				if (psTwo != null) {
					psTwo.close();
				}
				if (rsTwo != null) {
					rsTwo.close();
				}
			}
			if (ps != null) {
				ps.close();
			}
			if (psOne != null) {
				psOne.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (rsOne != null) {
				rsOne.close();
			}
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException();
		} finally {
			connection.setAutoCommit(true);
		}
		return allFollowers;
	}

	public synchronized Set<User> getAllFollowedForUser(String followerUsername) throws SQLException {
		if (!CommonUtils.isValidString(followerUsername)) {
			return null;
		}
		connection.setAutoCommit(false);
		Set<User> allFollowed = new HashSet();
		try {
			// take id of the follower
			PreparedStatement ps = connection.prepareStatement("SELECT user_id FROM users WHERE username=?;");
			ps.setString(1, followerUsername);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Long followerId = rs.getLong(1);

			String selectAllFollowedFromDB = "SELECT user_id FROM user_follower WHERE follower_id=?;";

			PreparedStatement psOne = connection.prepareStatement(selectAllFollowedFromDB);
			psOne.setLong(1, followerId);
			ResultSet rsOne = psOne.executeQuery();
			while (rsOne.next()) {
				Long followedUserId = rsOne.getLong("user_id");

				PreparedStatement psTwo = connection.prepareStatement("SELECT username FROM users WHERE user_id=?;");
				psTwo.setLong(1, followedUserId);
				ResultSet rsTwo = psTwo.executeQuery();
				rsTwo.next();
				String usernameOfFollowed = rsTwo.getString(1);

				allFollowed.add(getBasicUser(usernameOfFollowed));
				if (psTwo != null) {
					psTwo.close();
				}
				if (rsTwo != null) {
					rsTwo.close();
				}
			}
			if (ps != null) {
				ps.close();
			}
			if (psOne != null) {
				psOne.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (rsOne != null) {
				rsOne.close();
			}

			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException();
		} finally {
			connection.setAutoCommit(true);
		}
		return allFollowed;
	}

	public synchronized void addToFollowedUsers(User user, User follower) throws SQLException {
		Long userId = 0L;
		Long followerId = 0L;

		connection.setAutoCommit(false);
		try {
			if (existUser(user.getUserName()) && existUser(follower.getUserName())) {
				PreparedStatement ps = null;
				ps = connection.prepareStatement("INSERT INTO user_follower (user_id,follower_id) VALUES (?, ?)");

				PreparedStatement psUser = null;
				psUser = connection.prepareStatement("SELECT user_id FROM users WHERE username=?;");
				psUser.setString(1, user.getUserName());
				ResultSet rsUser = psUser.executeQuery();
				if (rsUser.next()) {
					userId = rsUser.getLong(1);
				}
				PreparedStatement psFollower = null;
				psFollower = connection.prepareStatement("SELECT user_id FROM users WHERE username=?;");
				psFollower.setString(1, follower.getUserName());
				ResultSet rsFollower = psFollower.executeQuery();
				if (rsFollower.next()) {
					followerId = rsFollower.getLong(1);
				}
				if (userId != 0L && followerId != 0L) {
					ps.setLong(1, userId);
					ps.setLong(2, followerId);
					ps.executeUpdate();
				}
				if (ps != null) {
					ps.close();
				}
				if (psUser != null) {
					psUser.close();
				}
				if (psFollower != null) {
					psFollower.close();
				}
				if (rsUser != null) {
					rsUser.close();
				}
				if (rsFollower != null) {
					rsFollower.close();
				}
			}
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException();

		} finally {
			connection.setAutoCommit(true);
		}

	}

	public synchronized void removeFromFollowedUsers(User user, User follower) throws SQLException {
		Long userId = 0L;
		Long followerId = 0L;

		connection.setAutoCommit(false);
		try {
			if (existUser(user.getUserName()) && existUser(follower.getUserName())) {
				PreparedStatement ps = null;
				ps = connection.prepareStatement("DELETE FROM user_follower WHERE user_id=? AND follower_id=?");

				PreparedStatement psUser = null;
				psUser = connection.prepareStatement("SELECT user_id FROM users WHERE username=?;");
				psUser.setString(1, user.getUserName());
				ResultSet rsUser = psUser.executeQuery();
				if (rsUser.next()) {
					userId = rsUser.getLong(1);
				}
				PreparedStatement psFollower = null;
				psFollower = connection.prepareStatement("SELECT user_id FROM users WHERE username=?;");
				psFollower.setString(1, follower.getUserName());
				ResultSet rsFollower = psFollower.executeQuery();
				if (rsFollower.next()) {
					followerId = rsFollower.getLong(1);
				}
				if (userId != 0L && followerId != 0L) {
					ps.setLong(1, userId);
					ps.setLong(2, followerId);
					ps.executeUpdate();
				}
				if (ps != null) {
					ps.close();
				}
				if (psUser != null) {
					psUser.close();
				}
				if (psFollower != null) {
					psFollower.close();
				}
				if (rsUser != null) {
					rsUser.close();
				}
				if (rsFollower != null) {
					rsFollower.close();
				}
			}
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw new SQLException();

		} finally {
			connection.setAutoCommit(true);
		}
	}

	public synchronized ArrayList<User> getAllUsers() throws SQLException {
		PreparedStatement ps = connection.prepareStatement(
				"SELECT first_name,last_name,email,username,register_date,profile_picture,description FROM users;");
		ResultSet rs = ps.executeQuery();
		ArrayList<User> users = new ArrayList<>();
		while (rs.next()) {
			users.add(new User(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"),
					rs.getString("username"), rs.getDate("register_date").toLocalDate(),
					rs.getString("profile_picture"), rs.getString("description")));
		}

		if (ps != null) {
			ps.close();
		}
		if (rs != null) {
			rs.close();
		}
		return users;
	}

	public synchronized boolean checkUserPass(String username, String password) throws SQLException {
		PreparedStatement ps = null;
		boolean passMatchUsername = false;

		ps = connection.prepareStatement("SELECT password FROM users WHERE username=?;");
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		rs.next();

		String actualPass = rs.getString(1);
		if (password.equals(actualPass)) {
			passMatchUsername = true;
		}
		if (ps != null) {
			ps.close();
		}
		if (rs != null) {
			rs.close();
		}
		return passMatchUsername;
	}

	public synchronized User getBasicUser(String username) throws SQLException {
		User user = null;
		if (existUser(username)) {
			PreparedStatement ps = connection.prepareStatement(
					"SELECT first_name,last_name,email,username,register_date,profile_picture,description FROM users WHERE username=?;");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			rs.next();
			user = new User(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"),
					rs.getString("username"), rs.getDate("register_date").toLocalDate(),
					rs.getString("profile_picture"), rs.getString("description"));

			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return user;
	}
	


}
