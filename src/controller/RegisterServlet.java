package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.db.DbManager;
import model.db.UserDao;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	private static final int MINIMUM_PASSWORD_LENGTH = 7;
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
			.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	private static final Pattern VALID_USERNAME = Pattern.compile("^[a-zA-Z0-9._-]{3,}$", Pattern.CASE_INSENSITIVE);
	private static final String REG_SUCC_MSG = "Registration successful";
	private Connection connection;

	@Override
	public void init() throws ServletException {
		// open connections
		super.init();
		connection = DbManager.getInstance().getConnection();
	}

	@Override
	public void destroy() {
		// close connections
		super.destroy();
		DbManager.getInstance().closeConnection();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("lastname");
		String description = request.getParameter("description");

		String validationMessage = validateRegisterData(userName, password, email, firstName, lastName);

		// Registration of user
		if (validationMessage.equals(REG_SUCC_MSG)) {
			// TODO add profile picture
			try {
				UserDao.getInstance(connection).insertUser(userName, password, email, firstName, lastName, description,
						"image.jpg");
			} catch (SQLException e) {
				System.out.println("Problem with the database. Could not execute query!");
				response.getWriter().append("Problem with the database. Could not execute query!");
				return;
			}
			response.getWriter().append("You have registered successfully. Please login with your account!");

		} else {
			response.getWriter().append(validationMessage);
		}
	}

	private boolean validateEmail(String email) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
		return matcher.find();
	}

	private boolean validateUsername(String userName) {
		Matcher matcher = VALID_USERNAME.matcher(userName);
		return matcher.find();
	}

	public static boolean validateFirstName(String firstName) {
		return firstName.matches("[A-Z][a-zA-Z]*");
	}

	public static boolean validateLastName(String lastName) {
		return lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
	}

	private String validateRegisterData(String userName, String password, String email, String firstName,
			String lastName) {

		if (userName == null || password == null || email == null || firstName == null || lastName == null
				|| userName.isEmpty() || password.isEmpty() || email.isEmpty() || firstName.isEmpty()
				|| lastName.isEmpty()) {
			return "Please fill all the required fields!";
		}
		if (password.length() < MINIMUM_PASSWORD_LENGTH) {
			return "Password must be longer than 7 symbols!";
		}
		if (!validateEmail(email)) {
			return "Email is invalid!";
		}
		if (!validateUsername(userName)) {
			return "UserName contains forbidden symbols!";
		}
		if (!validateFirstName(firstName)) {
			return "Invalid First name!";
		}
		if (!validateLastName(lastName)) {
			return "Invalid Last name!";
		}
		try {
			if (UserDao.getInstance(connection).existUser(userName)) {
				return "This username already exists!";
			}
		} catch (SQLException e) {
			return "There is problem with the prepared statement";
		}
		return REG_SUCC_MSG;
	}
}
