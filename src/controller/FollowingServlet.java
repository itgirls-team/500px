package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.db.DbManager;
import model.db.UserDao;

@WebServlet("/following")
public class FollowingServlet extends HttpServlet {

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
		String pageToRedirect = request.getParameter("pageToRedirect");
		request.getSession().setAttribute("pageToRedirect", pageToRedirect);
		boolean followedUserIsFollowed = true;
		Set<User> followed;
		Map<User, Boolean> followedUsersAreFollowed = new HashMap<User, Boolean>();
		try {
			followed = UserDao.getInstance(connection)
					.getAllFollowedForUser(((User) (request.getSession().getAttribute("user"))).getUserName());
			request.getSession().setAttribute("followed", followed);

			for (User followedUser : followed) {
				followedUsersAreFollowed.put(followedUser, followedUserIsFollowed);
			}
			request.getSession().setAttribute("isFollowed", followedUsersAreFollowed);
			if (followedUsersAreFollowed.isEmpty()) {
				request.getSession().setAttribute("noFollowed", true);
			}
			response.sendRedirect("following.jsp");
		} catch (SQLException e) {
			request.setAttribute("error", "problem with the database. Could not execute query!");
			return;
		}
	}
}
