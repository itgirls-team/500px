package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("")
public class WelcomeServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Object obj = session.getAttribute("logged");
		boolean logged = (obj != null && ((boolean) obj));
		if (session.isNew() || !logged || request.getSession().getAttribute("user") == null) {
			request.getRequestDispatcher("index.html").forward(request, response);
		} else {
			request.getRequestDispatcher("main.jsp").forward(request, response);
		}
		return;
	}
}
