package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.Post;
import model.Tag;
import model.db.PostDao;

@WebServlet("/UploadPost")
public class UploadPost extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String POSTS_URL = "C:/pictures/";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("uploadPost.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String description = request.getParameter("description");
		
		String[] inputTags = request.getParameter("tags").split(",");
		
		Set<Tag> tags = new HashSet<>();
		for (String string : inputTags) {
			tags.add(new Tag(string));
		}
	    
		Part postPart = request.getPart("image");
		InputStream fis = postPart.getInputStream();
		File myFile = new File(POSTS_URL + description + ".jpg");
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

		String postUrl = description + ".jpg";
		try {
			Post p = new Post(postUrl, description, tags);
			PostDao.getInstance().uploadPost(p);
			request.getSession().setAttribute("post", p);
			request.getRequestDispatcher("post.jsp").forward(request, response);
		} catch (SQLException e) {
			System.out.println("Error with upload post!");
		}
	}

}
