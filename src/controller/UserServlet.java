package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.db.DbManager;
import model.db.UserDao;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
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

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String msg;
		String username = req.getParameter("username");

		try {
			msg = UserDao.getInstance(connection).deleteUser(username);
		} catch (SQLException e) {
			msg = "User could not be deleted.Problem with the DB connection.";
		}
		resp.getWriter().append(msg);
	}

}
