package es.unex.pi.resources;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import es.unex.pi.dao.DishDAO;
import es.unex.pi.dao.JDBCDishDAOImpl;
import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.model.Dish;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.User;
import es.unex.pi.resources.exceptions.CustomBadRequestException;
import es.unex.pi.resources.exceptions.CustomNotFoundException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/dishes")
public class DishesResource {
	
	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;
	
	private static final String ID_REGEXP = "[0-9]+";
	
	@GET
	@Path("/{dishid: "+ID_REGEXP+"}")
	@Produces(MediaType.APPLICATION_JSON)
	public Dish getDishJSON(@PathParam("dishid") long dishid,
							@Context HttpServletRequest request) {	  
		Dish dish = null;
		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		DishDAO dishDAO = new JDBCDishDAOImpl();
		dishDAO.setConnection(conn);
	  
		dish = dishDAO.get(dishid);
		
		if(dish != null) {
			return dish;
		} else {
			throw new CustomNotFoundException("Error: Dish not found");
		}
	}
	
	@POST	  	  
	@Consumes(MediaType.APPLICATION_JSON)
	public Response post(Dish newDish, @Context HttpServletRequest request) throws Exception {	
		
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		DishDAO dishDAO = new JDBCDishDAOImpl();
		dishDAO.setConnection(conn);	  	 
	
		// Se recupera el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// Se recupera el restaurante al que pertenece el nuevo plato
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		Restaurant restaurant = restaurantDAO.get(newDish.getIdr());
		
		Map<String, String> messages = new HashMap<String, String>();

		if(restaurant.getIdu() != user.getId()) {
			// El restaurante NO pertenece al usuario, por lo que se lanza excepción
			throw new CustomBadRequestException("Restaurant does not exist or restaurant does not belong to user. Impossible to create new dish.");
		} else {
			if(dishDAO.get(newDish.getId()) != null) {
				// Ya existe un plato con el mismo ID, por lo que se lanza excepción
				throw new CustomBadRequestException("A dish with the same ID already exists.");
			} else {
				if(dishDAO.getByNameAndRestaurantId(newDish.getName(), newDish.getIdr()) != null) {
					// Ya existe otro plato en el mismo restaurante que tiene el mismo nombre
					messages.put("error", "Ya existe un plato con el mismo nombre en el restaurante. Por favor, introduce otro nombre.");
					throw new CustomBadRequestException(messages);
				} else {
					// El plato es válido y se está creando en un resaurante del usuario
					// Se añade a la BD
					Long id = dishDAO.add(newDish);
					// Se retorna la respuesta
					Response response;
					
					response = Response //return 201
							   .created(
								uriInfo.getAbsolutePathBuilder()
								       .path(Long.toString(id))
									   .build())
							   .contentLocation(
								uriInfo.getAbsolutePathBuilder()
								       .path(Long.toString(id))
								       .build())
								.build();
					return response;
				}
			}
		}
	}
	
	@PUT
	@Path("/{dishid: "+ID_REGEXP+"}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put(Dish dishUpdated,
						@PathParam("dishid") long dishid,
						@Context HttpServletRequest request) throws Exception{
		
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		DishDAO dishDAO = new JDBCDishDAOImpl();
		dishDAO.setConnection(conn);
		
		// Se recupera el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
				
		Map<String, String> messages = new HashMap<String, String>();
		
		// Se recupera la información actual del plato que se quiere actualizar
		Dish dish = dishDAO.get(dishUpdated.getId());
				
		if(dish == null) {
			// No existe el plato que se quiere actualizar
			throw new CustomNotFoundException("Dish does not exist");
		} else {
			if(dish.getId() != dishid) {
				// El ID del plato que se quiere actualizar no coincide con el ID pasado en la request
				throw new CustomBadRequestException("Error in ID");
			} else {
				// Se recupera el restaurante al que pertenece el plato a actualizar
				RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
				restaurantDAO.setConnection(conn);
				Restaurant restaurant = restaurantDAO.get(dishUpdated.getIdr());
				if(restaurant.getIdu() != user.getId()) {
					throw new CustomBadRequestException("Restaurant does not exist or restaurant does not belong to user. Impossible to update dish.");
				} else {
					if(dishDAO.getByNameAndRestaurantId(dishUpdated.getName(), dishUpdated.getIdr()) == null || dishUpdated.getName().equals(dish.getName())) {
						// El plato es válido y se está actualizando en un resaurante del usuario en el que no existe otro plato con el mismo nombre
						// Se actualiza en la BD
						dishDAO.update(dishUpdated);
						Response response = Response.noContent().build();
						return response;
					} else {
						// Ya existe otro plato en el mismo restaurante que tiene el mismo nombre
						messages.put("error", "Ya existe un plato con el mismo nombre en el restaurante. Por favor, introduce otro nombre.");
						throw new CustomBadRequestException(messages);
					}
				}
			}
		}
	}
	
	@DELETE
	@Path("/{dishid: "+ID_REGEXP+"}")  
	public Response deleteDish(@PathParam("dishid") long dishid,
		  					  @Context HttpServletRequest request) {
	  
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		DishDAO dishDao = new JDBCDishDAOImpl();
		dishDao.setConnection(conn);
		
		// Se recupera el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// Se obtiene el plato que se desea eliminar
		Dish dish = dishDao.get(dishid);
		
		if(dish != null) {
			// Se obtiene el restaurante al que pertenece el plato
			RestaurantDAO restaurantDao = new JDBCRestaurantDAOImpl();
			restaurantDao.setConnection(conn);
			Restaurant restaurant = restaurantDao.get(dish.getIdr());
			if(restaurant.getIdu() == user.getId()) {
				// Se elimina el plato
				dishDao.delete(dishid);
				return Response.noContent().build(); //204 no content
			} else {
				throw new CustomBadRequestException("Error deleting dish: you are not the owner of the restaurant to which the dish belongs");
			}
		} else {
			throw new CustomBadRequestException("Error deleting dish: dish does not exist");
		}
	}

}
