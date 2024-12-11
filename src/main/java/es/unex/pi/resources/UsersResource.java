package es.unex.pi.resources;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.User;
import es.unex.pi.resources.exceptions.CustomBadRequestException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/users")
public class UsersResource {

	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;
	
	//private static final String USER_ID_REGEXP = "[0-9]+";
	//private static final String USER_NAME_REGEXP = "[A-Za-záéíóúñÁÉÍÓÚ]{2,}([\\s][A-Za-záéíóúñÁÉÍÓÚ]{2,})*";
	 
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserJSON(@Context HttpServletRequest request) {	  
		// Se obtiene la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);
		
		// Se recupera el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// Se modifica la contraseña para que no sea visible
		user.setPassword("");
		
		return user;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put(User userUpdated, @Context HttpServletRequest request) {	  
		
		// Se obtiene la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);
		
		// Se recupera el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		Response response = null;
		
		// Si el usuario que se intenta actualizar no coincide con el de la sesión, se lanza excepción
		if(userUpdated.getId() != user.getId()) {
			throw new CustomBadRequestException("Error in user ID (update)");
		} else {
			Map<String, String> messages = new HashMap<String, String>();
			if(userUpdated.validate(messages)) {
				if(!userUpdated.getEmail().equals(user.getEmail()) && userDAO.getByEmail(userUpdated.getEmail()) != null) {
					// Ya existe un usuario con el mismo email
					messages.put("error", "El email introducido no está disponible");
					throw new CustomBadRequestException(messages);				
				} else {
					// Datos válidos y no existe otro usuario con el mismo email
					userDAO.update(userUpdated);
					// Se almacena el usuario en la sesión
					session.setAttribute("user", userUpdated);
					response = Response.noContent().build();
					return response;
				}
			} else {
				// Datos no válidos
				throw new CustomBadRequestException(messages);
			}
		}
	}
	
	@DELETE
	public Response deleteUser(@Context HttpServletRequest request) {	  
		// Se obtiene la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);
		
		// Se recupera el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// Se elimina el usuario de la BD 
		userDAO.delete(user.getId());
		// Se invalida la sesión
		session.invalidate();
		
		return Response.noContent().build(); //204 no content		
	}
	
//	@GET
//	@Path("/{username: "+USER_NAME_REGEXP+"}")	
//	@Produces(MediaType.APPLICATION_JSON)
//	public User getUserJSON(@PathParam("username") String username, 
//							 @Context HttpServletRequest request) {
//		
//		// El formato del nombre de usuario ya se comprueba con la RegExp
//		
//		User user = null;
//	  
//		// Se obtiene la conexión a la BD
//		Connection conn = (Connection) sc.getAttribute("dbConn");
//		UserDAO userDAO = new JDBCUserDAOImpl();
//		userDAO.setConnection(conn);
//	 
//		// Si el usuario existe, se retorna
//		// Si el usuario no existe, se devuelve una CustomNotFoundException informando de ello
//		user = userDAO.get(username);
//		if(user != null) {
//			return user;
//		} else {
//			throw new CustomNotFoundException("El usuario con nombre "+username+"no se ha encontrado");
//		}
//	}
	 
}
