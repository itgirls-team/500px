package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("uploadPost.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Part avatarPart = request.getPart("image");
		InputStream fis = avatarPart.getInputStream();
		File myFile = new File(POSTS_URL+"234.jpg");
		if(!myFile.exists()){
			myFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(myFile);
		int b = fis.read();
		while(b != -1){
			fos.write(b);
			b = fis.read();
		}
		fis.close();
		fos.close();
		String avatarUrl = "234.jpg";
		String[] inputTags = request.getParameter("tags").split("\\s+");
		HashSet<Tag> tags = new HashSet<>();
		for (String string : inputTags) {
			tags.add(new Tag(string));
		}
		
		long albumId=Long.parseLong(request.getParameter("albumId"));
		String description=request.getParameter("description");
		Post post=new Post(avatarUrl,description,tags,albumId);
		try {
			PostDao.getInstance().uploadPost(post);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("posts.jsp").forward(request, response);
	}

}
