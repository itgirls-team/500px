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

@WebServlet("/albums")
public class AlbumServlet extends HttpServlet {
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
		try {
			request.getSession().setAttribute("user", UserDao.getInstance(connection).getUser("username"));
			request.getRequestDispatcher("album.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
