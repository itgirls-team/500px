package model.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserDao {

	private static UserDao instance;

	private UserDao() {
	}

	public static synchronized UserDao getInstance() {
		if (instance == null) {
			instance = new UserDao();
		}
		return instance;
	}

	public void insertUser(String userName, String password, String email, String firstName, String lastName,
			String description, String pictureName) {
		Connection connection = DBManager.getInstance().getConnection();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(
					"INSERT INTO users (username,firstName,lastName,password,email,description,profilePicture,registerDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, userName);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, password);
			ps.setString(5, email);
			ps.setString(6, description);
			ps.setString(7, pictureName);
			ps.setDate(8, Date.valueOf(LocalDate.now()));
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Problem with the prepared statement " + e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					System.out.println("Problem closing the connection " + e.getMessage());
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					System.out.println("Problem closing the prepared statement " + e.getMessage());
				}
			}
		}
	}

	public boolean existUser(String userName) throws SQLException {
		Connection connection = DBManager.getInstance().getConnection();
		PreparedStatement ps = null;
		boolean userExists = true;

		try {
			ps = connection.prepareStatement("SELECT count(*)>0 FROM users WHERE username=?;");
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				userExists = rs.getBoolean(1);
			}

		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					System.out.println("Problem closing the connection " + e.getMessage());
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					System.out.println("Problem closing the prepared statement " + e.getMessage());
				}
			}
		}
		return userExists;
	}

	public void deleteUser(long id) {
		// TODO implement
	}

	public void getUser(long id) {
		// TODO implement
	}
}
