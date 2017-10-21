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
		try {
			Album album = (Album) request.getAttribute("albumId");
			request.getSession().setAttribute("posts", PostDao.getInstance().getAllPostsFromAlbum(album));
			System.out.println(PostDao.getInstance().getAllPostsFromAlbum(album));
			request.getRequestDispatcher("posts.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
