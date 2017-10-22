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
@WebServlet("/followers")
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Set<User> followers;
		try {
			followers = UserDao.getInstance(connection)
					.getAllFollowersForUser(((User) (request.getSession().getAttribute("user"))).getUserName());
			request.getSession().setAttribute("followers", followers);
			response.sendRedirect("followers.jsp");
		} catch (SQLException e) {
			// TODO
		}
	}
}
