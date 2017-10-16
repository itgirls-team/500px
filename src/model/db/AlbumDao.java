package model.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import model.Album;
import model.Post;
import model.Tag;

public class AlbumDao {

	private static final String UPLOAD_ALBUM = "INSERT INTO albums (category, date_upload, user_id ) VALUES (?,?,?)";
	private static final String SELECT_ALBUMS_BY_USER = "SELECT album_id,category,user_id FROM albums WHERE user_id = ?";
	private static final String SELECT_TAGS_FROM_POST = "SELECT t.title FROM post_tag AS p JOIN tags AS t USING (tag_id) WHERE p.post_id = ? ";
	private static final String SELECT_POST_FROM_ALBUM = "SELECT post_id,image,counts_likes,counts_dislikes,description FROM posts WHERE album_id = ?";
	
	private static Connection con = DBManager.getInstance().getConnection();
	private static AlbumDao instance;

	private AlbumDao() {
	}

	public static synchronized AlbumDao getInstance() {
		if (instance == null) {
			instance = new AlbumDao();
		}
		return instance;
	}

	// uploadAlbum
	public void uploadAlbum(Album a) throws SQLException {
		con.setAutoCommit(false);
		PreparedStatement ps = con.prepareStatement(UPLOAD_ALBUM, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, a.getCategory());
		ps.setDate(2, Date.valueOf(LocalDate.now()));
		ps.setLong(3, a.getUser());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		a.setId(rs.getLong(1));
		con.commit();
		con.setAutoCommit(true);
	}

	public void uploadAlbum(String category, int user_id) throws SQLException {
		PreparedStatement ps = con.prepareStatement(UPLOAD_ALBUM, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, category);
		ps.setDate(2, Date.valueOf(LocalDate.now()));
		ps.setInt(3, user_id);
		ps.executeUpdate();
	}

	// getAllAlbumFromUser
	public HashSet<Album> getAllAlbumFromUser(long user_id) throws SQLException {
		PreparedStatement ps = con.prepareStatement(SELECT_ALBUMS_BY_USER);
		ps.setLong(1, user_id);
		ResultSet rs = ps.executeQuery();
		HashSet<Album> albums = new HashSet<>();
		while (rs.next()) {
			HashSet<Post> posts = new HashSet<>();
			PreparedStatement ps_posts = con.prepareStatement(SELECT_POST_FROM_ALBUM);
			ps_posts.setLong(1, rs.getLong("album_id"));
			ResultSet rs1 = ps_posts.executeQuery();
			while (rs1.next()) {
				HashSet<Tag> tags = new HashSet<>();
				PreparedStatement ps_tags = con.prepareStatement(SELECT_TAGS_FROM_POST);
				ps_tags.setLong(1, rs1.getLong("post_id"));
				ResultSet rs2 = ps_tags.executeQuery();
				while (rs2.next()) {
					tags.add(new Tag(rs2.getString("title")));
				}
				posts.add(new Post(rs1.getLong("post_id"), rs1.getString("image"), rs1.getInt("counts_likes"),
						rs1.getInt("counts_dislikes"), rs1.getString("description"),tags , rs.getInt("album_id")));
			}
			albums.add(new Album(rs.getLong("album_id"), rs.getString("category"), rs.getInt("user_id")));
		}
		return albums;
	}
	

	public static void main(String[] args) throws SQLException {
		// AlbumDao.getInstance().uploadAlbum(new Album("animals",1));
		// AlbumDao.getInstance().uploadAlbum("people",1);
		HashSet<Album> albums = AlbumDao.getInstance().getAllAlbumFromUser(1);
		for (Album a : albums) {
			System.out.println(a);
		}
	}

}
