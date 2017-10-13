package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Tag;

public class TagDAO {

	private static TagDAO instance;
	private TagDAO(){}
	
	public static synchronized TagDAO getInstance(){
		if(instance == null){
			instance = new TagDAO();
		}
		return instance;
	}
	
	public boolean existsTag(Tag t) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		Statement stmt = con.createStatement();
		String sql = "SELECT COUNT(*) FROM videos WHERE tag_id = '" + t.getId() + "';";
		ResultSet rs = stmt.executeQuery(sql);

		rs.next();
		int count = rs.getInt(1);

		if (count == 1) {
			return true;
		}
		return false;
	}

}
