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
 * Servlet implementation class SignUpServlet
 */
@WebServlet(urlPatterns = {"/SignUpServlet.do" })
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Este método se ejecuta al ser redireccionado a la página de registro.
	 * Despacha la request a EditUser.jsp para rellenar el formulario de registro.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se despacha a EditUser.jsp para acceder al formulario de registro
		RequestDispatcher view = request.getRequestDispatcher("WEB-INF/user/EditUser.jsp");
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Este método se ejecuta cuando un nuevo usuario va a registrarse.
	 * Comprueba la validez de las credenciales introducidas. Si existe ya un usuario con el email introducido, se informa del error y no se completa el registro.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		// En caso de que el usuario vuelva al registro con el botón Back y vuelva a introducir credenciales (válidas o no), se elimina el usuario previamente loggeado (si lo hay)
		HttpSession session = request.getSession();
		if((User)session.getAttribute("user") != null) {
			session.removeAttribute("user");
		}
				
		// Se recuperan los datos introducidos por el usuario para crear un nuevo objeto
		User user = new User();
		user.setName(request.getParameter("name"));
		user.setSurname(request.getParameter("surname"));
		user.setEmail(request.getParameter("email"));
		user.setPassword(request.getParameter("password"));
		
		// Se comprueba la validez de los datos introducidos
		Map<String, String> messages = new HashMap<String, String>();
		if (user.validate(messages)) {
			// Se establece la conexión con la BD
			Connection conn = (Connection) getServletContext().getAttribute("dbConn");
			UserDAO userDAO = new JDBCUserDAOImpl();
			userDAO.setConnection(conn);
			// Se comprueba si un usuario ya está registrado con el mismo email
			if(userDAO.getByEmail(user.getEmail()) != null) {
				// Ya hay un usuario registrado con el mismo email. Se añade un mensaje de error y se despacha
				messages.put("user_already_exists", "El email introducido no está disponible.");
				// Se almacenan los errores en la request
				request.setAttribute("messages", messages);
				// Se mantiene la información introducida para no hacer que el usuario vuelva a escribirlo todo
				request.setAttribute("user", user);
				// Se despacha a EditUser.jsp
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/user/EditUser.jsp");
				view.forward(request,response);
			} else { // Los datos son válidos y no existe el usuario
				// Se almacena el nuevo usuario en la base de datos
				userDAO.add(user);
				// Se recupera de la BD el usuario recién insertado para traer el ID generado (necesario para operaciones Update y Delete)
				user = userDAO.getByEmail(user.getEmail());
				// Se almacena el usuario en la sesión (ya con el ID)
				session.setAttribute("user", user);
				// Se redirecciona al servlet SearchAndCategories.do
				response.sendRedirect("pages/index.html");
			}
		} else { // Datos no válidos
			// Se almacenan los errores en la request
			request.setAttribute("messages", messages);
			// Se mantiene la información introducida para no hacer que el usuario vuelva a escribirlo todo
			request.setAttribute("user", user);
			// Se despacha a EditUser.jsp
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/user/EditUser.jsp");
			view.forward(request,response);
		}
	}
}
