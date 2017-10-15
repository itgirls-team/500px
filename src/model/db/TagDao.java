package model.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Tag;

public class TagDao {

	private static TagDao instance;
	private TagDao(){}
	
	public static synchronized TagDao getInstance(){
		if(instance == null){
			instance = new TagDao();
		}
		return instance;
	}
	
	//isexsists
	//insertTag
	//getPostsByTag
	

}
