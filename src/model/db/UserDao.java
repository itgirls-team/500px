package model.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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

	public void getUser(String username) {
		// TODO implement
		// make userDto
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
