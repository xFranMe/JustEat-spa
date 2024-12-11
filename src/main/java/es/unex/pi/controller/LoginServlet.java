package es.unex.pi.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(urlPatterns = {"/LoginServlet.do" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Este método se ejecuta al ser redireccionado a la página de login.
	 * Despacha la request a Login.jsp para completar el formulario pertinente.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se despacha la request a Login.jsp para acceder al formulario de login
		RequestDispatcher view = request.getRequestDispatcher("Login.jsp");
		view.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Este método se ejecuta cuando el usuario quiere iniciar sesión con las credenciales introducidas en el formulario.
	 * Se comprueba la validez de dichas credenciales, dejando continuar al usuario si la cuenta existe o informando del error si no existe.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// En caso de que el usuario vuelva al login con el botón Back y vuelva a introducir credenciales (válidas o no), se elimina el usuario previamente loggeado (si lo hay)
		HttpSession session = request.getSession();
		if((User)session.getAttribute("user") != null) {
			session.removeAttribute("user");
		}
						
		// Se recuperan las credenciales introducidas por el usuario en el formulario de login
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		// Se establece la conexión con la base de datos
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);
		
		// Se recupera el objeto User que coincida con las credenciales introducidas (en caso de que no exista se devuelve NULL)
		User user = userDAO.get(email, password);
		
		if(user != null) { // El usuario existe, por lo que se puede iniciar sesión e ir a la página principal (SearchAndCategoriesServlet.do)
			session.setAttribute("user", user); 
			response.sendRedirect("pages/index.html");
		} else { // El usuario no existe, por lo que no se puede iniciar sesión y se debe notificar al usuario de ello
			// Se añade un mensaje de error a la request indicando que el email o la contraseña no coinciden
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "El email o la contraseña no coinciden. Por favor, introduce de nuevo los datos.");
			request.setAttribute("messages", messages);
			// Se añaden a la request las credenciales que habían sido introducidas
			request.setAttribute("email", email);
			request.setAttribute("password", password);
			// Se despacha la request con el error a Login.jsp
			RequestDispatcher view = request.getRequestDispatcher("Login.jsp");
			view.forward(request,response);
		}
	}
}
