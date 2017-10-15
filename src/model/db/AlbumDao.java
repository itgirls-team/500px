package model.db;

public class AlbumDao {

	private static AlbumDao instance;
	private AlbumDao(){}
	
	public static synchronized AlbumDao getInstance(){
		if(instance == null){
			instance = new AlbumDao();
		}
		return instance;
	}
	
	//getAllAlbum
	//getAlbum
	//insertAlbum
	
	
	
}
