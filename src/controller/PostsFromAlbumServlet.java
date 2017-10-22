package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Album;
import model.db.PostDao;

@WebServlet("/posts")
public class PostsFromAlbumServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long albumId = Long.parseLong(request.getParameter("albumId"));
		try {
			request.getSession().setAttribute("posts", PostDao.getInstance().getAllPostsFromAlbum(albumId));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("posts.jsp").forward(request, response);
	}

}
