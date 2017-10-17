package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.db.DbManager;
import model.db.UserDao;

/**
 * Servlet implementation class FollowersServlet
 */
@WebServlet("/FollowersServlet")
public class FollowersServlet extends HttpServlet {

	private Connection connection;

	@Override
	public void init() throws ServletException {
		// open connections
		super.init();
		connection = DbManager.getInstance().getConnection();
	}

	@Override
	public void destroy() {
		// close connections
		super.destroy();
		DbManager.getInstance().closeConnection();
	}

	// @Override
	// protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	// throws ServletException, IOException {
	// String msg = "";
	// String username = req.getParameter("username");
	// User user = new User("Veselina", "Stoyanova", "vesi@gmail.com", "vesi",
	// LocalDate.now(), "some pic", "yaaa");
	// User follower = new User("Yasen", "Partovski", "yasso@gmail.com",
	// "yasso", LocalDate.now(), "some pic", "yaaa");
	// try {
	// UserDao.getInstance(connection).removeFromFollowedUsers(user, follower);
	// } catch (SQLException e) {
	// msg = "User could not be unfollowed.Problem with the DB connection.";
	// }
	// resp.getWriter().append(msg);
	// }

	//
	// protected void doGet(HttpServletRequest request, HttpServletResponse
	// response)
	// throws ServletException, IOException {
	// String msg = "";
	// String username = request.getParameter("username");
	// try {
	// Set<User> followers =
	// UserDao.getInstance(connection).getAllFollowersForUser("vesi");
	// for (User user : followers) {
	// response.getWriter().append(user.getUserName());
	// }
	// } catch (SQLException e) {
	// msg = "User could not be followed.Problem with the DB connection.";
	// }
	// response.getWriter().append(msg);
	// }
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String msg = "";
		String username = request.getParameter("username");
		try {
			Set<User> followers = UserDao.getInstance(connection).getAllFollowedForUser("yasso");
			for (User user : followers) {
				response.getWriter().append(user.getUserName());
			}
		} catch (SQLException e) {
			msg = "User could not be followed.Problem with the DB connection.";
		}
		response.getWriter().append(msg);
	}
}
