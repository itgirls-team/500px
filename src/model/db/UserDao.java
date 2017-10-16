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
import java.util.concurrent.CopyOnWriteArrayList;

import model.User;

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

	public void insertUser(String userName, String password, String email, String firstName, String lastName,
			String description, String pictureName) throws SQLException {
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

	public boolean existUser(String userName) throws SQLException {
		PreparedStatement ps = null;
		boolean userExists = true;

		ps = connection.prepareStatement("SELECT count(*)>0 FROM users WHERE username=?;");
		ps.setString(1, userName);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			userExists = rs.getBoolean(1);
		}
		if (ps != null) {
			ps.close();
		}
		return userExists;
	}

	public void deleteUser(String username) throws SQLException {
		PreparedStatement ps = null;

		ps = connection.prepareStatement("DELETE FROM users WHERE username=?;");
		ps.setString(1, username);
		ps.executeUpdate();

		if (ps != null) {
			ps.close();
		}

	}

	public User getUser(String username) throws SQLException {
		// TODO implement
		// make userDto
		User user = null;
		if (existUser(username)) {
			PreparedStatement ps = null;
			ps = connection
					.prepareStatement("SELECT username,description,profile_picture FROM users WHERE username=?;");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				user = new User(rs.getString("username"), rs.getString("description"), rs.getString("profile_picture"));
			}
			if (ps != null) {
				ps.close();
			}
		}
		return user;
	}

	public void getAllFollowersFromDB() throws SQLException {
		// retrieve all users
		Set<User> allFollowers = new HashSet();
		String selectAllFollowersFromDB = "SELECT user_id, follower_id FROM user_follower WHERE ;";
		Statement statement = null;
		ResultSet result = null;

		statement = DbManager.getInstance().getConnection().createStatement();
		result = statement.executeQuery(selectAllFollowersFromDB);
		while (result.next()) {
			if (!(allFollowers.containsKey(result.getString("follower_email")))) {
				allFollowers.put(result.getString("follower_email"), new CopyOnWriteArrayList<>());
			}
			allFollowers.get(result.getString("follower_email")).add(result.getString("followed_email"));
		}
		return allFollowers;
	}

	public void getFollowed() {

	}

	public void addToFollowedUsers(User user, User follower) throws SQLException {
		Long userId = 0L;
		Long followerId = 0L;

		if (existUser(user.getUserName()) && existUser(follower.getUserName())) {
			PreparedStatement ps = null;
			ps = connection.prepareStatement("INSERT INTO user_follower (user_id,follower_id) VALUES (?, ?)");

			PreparedStatement psUser = null;
			psUser = connection.prepareStatement("SELECT user_id FROM users WHERE username=?;");
			psUser.setString(1, user.getUserName());
			ResultSet rsUser = ps.executeQuery();
			if (rsUser.next()) {
				userId = rsUser.getLong(1);
			}
			PreparedStatement psFollower = null;
			psFollower = connection.prepareStatement("SELECT user_id FROM users WHERE username=?;");
			psFollower.setString(1, follower.getUserName());
			ResultSet rsFollower = ps.executeQuery();
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
		}
	}

	public void removeFromFollowedUsers(User user, User follower) throws SQLException {
		Long userId = 0L;
		Long followerId = 0L;

		if (existUser(user.getUserName()) && existUser(follower.getUserName())) {
			PreparedStatement ps = null;
			ps = connection.prepareStatement("DELETE FROM user_follower WHERE user_id=? AND follower_id=?");

			PreparedStatement psUser = null;
			psUser = connection.prepareStatement("SELECT user_id FROM users WHERE username=?;");
			psUser.setString(1, user.getUserName());
			ResultSet rsUser = ps.executeQuery();
			if (rsUser.next()) {
				userId = rsUser.getLong(1);
			}
			PreparedStatement psFollower = null;
			psFollower = connection.prepareStatement("SELECT user_id FROM users WHERE username=?;");
			psFollower.setString(1, follower.getUserName());
			ResultSet rsFollower = ps.executeQuery();
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
		}
	}

	public void editUserProfile(long id) {
		// TODO implement
	}

	public boolean checkUserPass(String username, String password) throws SQLException {
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
		return passMatchUsername;
	}

}
