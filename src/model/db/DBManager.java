package model.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

	private static DBManager instance;
	private Connection connection;

	public DBManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not found or failed to load!");
		}
		final String DB_IP = "127.0.0.1";
		final String DB_PORT = "3306";
		final String DB_DBNAME = "project";
		final String DB_USER = "root";
		final String DB_PASS = "vessy";

		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + DB_IP + ":" + DB_PORT + "/" + DB_DBNAME, DB_USER,
					DB_PASS);

		} catch (SQLException e) {
			System.out.println("There is problem with the connection to the DB! " + e.getMessage());
		}
	}

	public synchronized static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("Problem closing the connection to DB " + e.getMessage());
			}
		}
	}

	public Connection getConnection() {
		return connection;
	}

}
