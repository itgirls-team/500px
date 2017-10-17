package model.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import model.Post;
import model.Tag;

public class TagDao {

	public static final String INSERT_POST_TAG = "INSERT INTO post_tag (post_id, tag_id) VALUES (?, ?)";
	public static final String SELECT_TAG = "SELECT * FROM tags WHERE title = ?";
	public static final String SELECT_TITLE_OF_TAG = "SELECT title FROM tags WHERE title = ?";
	public static final String INSERT_TAG = "INSERT INTO tags (title) VALUES (?)";

	private static TagDao instance;
	private static Connection con = DbManager.getInstance().getConnection();

	private TagDao() {
	}

	public static synchronized TagDao getInstance() {
		if (instance == null) {
			instance = new TagDao();
		}
		return instance;
	}

	// insert into common table
	public synchronized void insertPostTags(Post p) throws SQLException {
		Set<Tag> tags = p.getTagsOfPost();

		String insert_into_post_tag = INSERT_POST_TAG;

		for (Tag tag : tags) {
			insertTag(tag);
			tag = getTag(tag.getTitle());
			PreparedStatement post_tag = con.prepareStatement(insert_into_post_tag);
			post_tag.setLong(1, p.getId());
			post_tag.setLong(2, tag.getId());
			post_tag.executeUpdate();
		}
	}

	// insertTag
	public synchronized void insertTag(Tag t) throws SQLException {
		if (!existTag(t)) {
			PreparedStatement ps = con.prepareStatement(INSERT_TAG);
			ps.setString(1, t.getTitle());
			ps.executeUpdate();
		}
	}

	// getTag
	public Tag getTag(String tag) throws SQLException {
		PreparedStatement ps = con.prepareStatement(SELECT_TAG);
		ps.setString(1, tag);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return new Tag(rs.getLong("tag_id"), rs.getString("title"));
	}

	// is exist
	public boolean existTag(Tag t) throws SQLException {
		PreparedStatement ps = con.prepareStatement(SELECT_TITLE_OF_TAG);
		ps.setString(1, t.getTitle());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws SQLException {
		// TagDao.getInstance().insertTag(new Tag("angry"));
		// Tag tag = TagDao.getInstance().getTag("angry");
		// System.out.println(tag);
		// System.out.println(TagDao.getInstance().existTag(new Tag("angry")));
	}

}
