package model.db;

public class AlbumDAO {

	private static AlbumDAO instance;
	private AlbumDAO(){}
	
	public static synchronized AlbumDAO getInstance(){
		if(instance == null){
			instance = new AlbumDAO();
		}
		return instance;
	}
	
	//getAllAlbum
	//getAlbum
	//insertAlbum
	
	
	
}
