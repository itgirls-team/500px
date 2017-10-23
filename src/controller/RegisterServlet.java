package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.User;
import model.db.DbManager;
import model.db.UserDao;

@WebServlet("/register")
@MultipartConfig
public class RegisterServlet extends HttpServlet {

	public static final String AVATAR_URL = "C:/pictures/";
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
		String confirmPassword = request.getParameter("confpassword");
		String email = request.getParameter("email");
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("lastname");
		String description = request.getParameter("description");
		Part avatarPart = request.getPart("avatar");
		InputStream fis = avatarPart.getInputStream();
		File myFile = new File(AVATAR_URL + userName + ".jpg");
		if (!myFile.exists()) {
			myFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(myFile);
		int b = fis.read();
		while (b != -1) {
			fos.write(b);
			b = fis.read();
		}
		fis.close();
		fos.close();
		String avatarUrl = userName + ".jpg";

		String validationMessage = validateRegisterData(userName, password, confirmPassword, email, firstName,
				lastName);

		// Registration of user
		if (validationMessage.equals(REG_SUCC_MSG)) {
			try {
				UserDao.getInstance(connection).insertUser(userName, password, email, firstName, lastName, description,
						avatarUrl);
				User user = new User(userName, password, email, firstName, lastName, description, avatarUrl);
				request.getSession().setAttribute("user", user);
				request.getRequestDispatcher("main.jsp").forward(request, response);
			} catch (SQLException e) {
				System.out.println("Problem with the database. Could not execute query!");
				request.setAttribute("error", "Problem with the database. Could not execute query!");
				request.getRequestDispatcher("login.jsp").forward(request, response);
				return;
			}
		} else {
			request.setAttribute("error", validationMessage);
			request.getRequestDispatcher("register.jsp").forward(request, response);
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

	private boolean confirmPasswords(String password, String confPassword) {
		if (confPassword.equals(password)) {
			return true;
		}
		return false;
	}

	public static boolean validateFirstName(String firstName) {
		return firstName.matches("[A-Z][a-zA-Z]*");
	}

	public static boolean validateLastName(String lastName) {
		return lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
	}

	private String validateRegisterData(String userName, String password, String confPassword, String email,
			String firstName, String lastName) {

		if (userName == null || password == null || confPassword == null || email == null || firstName == null
				|| lastName == null || userName.isEmpty() || password.isEmpty() || confPassword.isEmpty()
				|| email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
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
		if (!confirmPasswords(password, confPassword)) {
			return "Confirm password is different than the password!";
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
