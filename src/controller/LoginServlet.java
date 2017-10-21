package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.db.DbManager;
import model.db.UserDao;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

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

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		try {
			if (validateLogInData(username, password)) {
				// login
				User user = UserDao.getInstance(connection).getUser(username);
				request.getSession().setAttribute("user", user);
				request.getSession().setAttribute("logged", true);
				response.sendRedirect("main.jsp");
			} else {
				// redirect to error page or to login.html again with popup for
				// error
				request.setAttribute("error", "username or password mismatch!");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			System.out.println("problem with the database. Could not execute query!");
			request.setAttribute("error", "problem with the database. Could not execute query! ");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	private boolean validateLogInData(String username, String password) throws SQLException {
		if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
			if (UserDao.getInstance(connection).existUser(username)
					&& UserDao.getInstance(connection).checkUserPass(username, password)) {
				return true;
			}
		}
		return false;
	}
}
