package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.db.DbManager;
import model.db.UserDao;

@WebServlet("/unfollow")
public class UnfollowServlet extends HttpServlet {

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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String followedUserName = request.getParameter("followedUserName");
			String pageToRedirect = (String) request.getSession().getAttribute("pageToRedirect");
			User loggedUser = (User) (request.getSession().getAttribute("user"));
			UserDao.getInstance(connection).removeFromFollowedUsers(followedUserName, loggedUser.getUserName());
			Map<User, Boolean> followers = (Map<User, Boolean>) request.getSession().getAttribute("isFollowed");
			for (Map.Entry<User, Boolean> entry : followers.entrySet()) {
				if (entry.getKey().getUserName().equals(followedUserName)) {
					entry.setValue(false);
					break;
				}
			}
			request.getSession().setAttribute("isFollowed", followers);
			response.sendRedirect(pageToRedirect);
		} catch (SQLException e) {
			request.setAttribute("error", "problem with the database. Could not execute query!");
			e.printStackTrace();
			return;
		}
	}

}
