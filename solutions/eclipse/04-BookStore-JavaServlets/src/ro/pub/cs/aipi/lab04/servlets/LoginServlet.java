package ro.pub.cs.aipi.lab04.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ro.pub.cs.aipi.lab04.businesslogic.UserManager;
import ro.pub.cs.aipi.lab04.general.Constants;
import ro.pub.cs.aipi.lab04.graphicuserinterface.LoginGraphicUserInterface;

public class LoginServlet extends HttpServlet {

	public final static long serialVersionUID = 20152015L;

	private UserManager userManager;

	private String username, password;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userManager = new UserManager();
	}

	@Override
	public void destroy() {
	}

	public boolean isLoginError(String username, String password) {
		return (username != null && !username.isEmpty() && password != null && !password.isEmpty()
				&& userManager.getType(username, password) == Constants.USER_NONE);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Enumeration<String> parameters = request.getParameterNames();
		boolean found = false;
		while (parameters.hasMoreElements()) {
			String parameter = (String) parameters.nextElement();
			if (parameter.equals(Constants.USERNAME)) {
				found = true;
				username = request.getParameter(parameter);
			}
			if (parameter.equals(Constants.PASSWORD)) {
				found = true;
				password = request.getParameter(parameter);
			}
		}
		if (!found) {
			username = "";
			password = "";
		}
		response.setContentType("text/html");
		try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
			int type = userManager.getType(username, password);
			if (type != Constants.USER_NONE) {
				HttpSession session = request.getSession(true);
				session.setAttribute(Constants.DISPLAY, userManager.getDisplay(username, password));
				RequestDispatcher dispatcher = null;
				switch (type) {
				case Constants.USER_ADMINISTRATOR:
					dispatcher = getServletContext()
							.getRequestDispatcher("/" + Constants.ADMINISTRATOR_SERVLET_PAGE_CONTEXT);
					break;
				case Constants.USER_CLIENT:
					dispatcher = getServletContext().getRequestDispatcher("/" + Constants.CLIENT_SERVLET_PAGE_CONTEXT);
					break;
				}
				if (dispatcher != null) {
					dispatcher.forward(request, response);
				}
			}

			LoginGraphicUserInterface.displayLoginGraphicUserInterface(isLoginError(username, password), printWriter);
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
