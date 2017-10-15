package model.db;

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
	
	//isexsists
	//insertTag
	//getPostsByTag
	

}
