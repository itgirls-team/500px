package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Post;

public class PostDAO {

	private static PostDAO instance;
	private PostDAO(){}
	
	public static synchronized PostDAO getInstance(){
		if(instance == null){
			instance = new PostDAO();
		}
		return instance;
	}
	
	//insertPostInDB
	public void insertPost(Post p) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"INSERT into posts (image, counts_like, counts_dislike, date_upload, description, album_id) VALUES(?,?,?,?,?,?);",
				Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, p.getPath());
		ps.setInt(2, p.getCountsOfLikes());
		ps.setInt(3, p.getCountsOfDislikes());
		ps.setDate(4, java.sql.Date.valueOf(p.getDateOfUpload().toLocalDate()));
		ps.setString(5, p.getDescription());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		p.setPostId(rs.getInt(1));
	}
	
	//deletePostInDB
	//getAllPostFromAlbum
	//getPost

	
	
}
