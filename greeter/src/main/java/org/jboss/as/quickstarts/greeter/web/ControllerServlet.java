package org.jboss.as.quickstarts.greeter.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/greet-controller")
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private GreetController greeter;
	
	public ControllerServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		greeter.setUsername(request.getParameter("username"));
		greeter.greet();
//		response.getOutputStream().println(greeter.getGreeting());
		response.getOutputStream().println(greeter.getGreeting().toUpperCase());
	}

}
