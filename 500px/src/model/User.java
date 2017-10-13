package model;

import java.time.LocalDate;
import java.util.Set;

public class User {

	private long id;
	private String userName;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private String description;
	private String profilePicture;
	private LocalDate registerDate;
	private Set<User> followers;
	private Set<User> following;
	// private Set<Album> albumsOfUser;

	public User(String userName, String firstName, String lastName, String password, String email, String description,
			String profilePicture) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.description = description;
		this.profilePicture = profilePicture;
	}

	public User(long id, String userName, String firstName, String lastName, String password, String email,
			String description, String profilePicture, LocalDate registerDate) {
		this(userName, firstName, lastName, password, email, description, profilePicture);
		this.id = id;
		this.registerDate = registerDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public LocalDate getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(LocalDate registerDate) {
		this.registerDate = registerDate;
	}

	public long getId() {
		return id;
	}

}
