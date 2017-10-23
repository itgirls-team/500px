package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.Album;
import model.User;
import model.db.AlbumDao;
import model.db.DbManager;
import model.db.UserDao;

@WebServlet("/CreateAlbum")
public class CreateAlbum extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String ALBUM_PICTURE_URL = "C:/pictures/";
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("createAlbum.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		String category = request.getParameter("category");
		Part postPart = request.getPart("picture");
		InputStream fis = postPart.getInputStream();
		File myFile = new File(ALBUM_PICTURE_URL + category + ".jpg");
		if (!myFile.exists()) {
			myFile.createNewFile();
		}
		
		FileOutputStream fos = new FileOutputStream(myFile);
		int b = fis.read();
		while (b != -1) {
			fos.write(b);
			b = fis.read();
		}
		fis.close();
		fos.close();

		String albumUrl = category + ".jpg";
		try {
			Album a = new Album(category, albumUrl);
			AlbumDao.getInstance().createAlbum(a);;
			request.getSession().setAttribute("album", a);
			request.getRequestDispatcher("album.jsp").forward(request, response);
		} catch (SQLException e) {
			System.out.println("Error with create album!");
		}
	}
	
}
