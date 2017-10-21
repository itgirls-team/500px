package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getSession().getAttribute("user") != null) {
			request.getSession().removeAttribute("user");
			request.getSession().setAttribute("logged", false);
		}
		request.getSession().invalidate();
		request.getRequestDispatcher("index.html").forward(request, response);

	}

}
