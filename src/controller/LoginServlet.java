package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username != null && !username.isEmpty()) {
			// login
			response.sendRedirect("main.html");
			request.getSession().setAttribute("logged", true);
			response.getWriter().append("Welcome, " + username);
		} else {
			// redirect to error page or to login.html again with popup for
			// error
			response.getWriter().append("Wrong username or password. Please try again!");
		}
	}
}
