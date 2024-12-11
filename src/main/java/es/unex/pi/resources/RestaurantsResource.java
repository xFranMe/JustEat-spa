package es.unex.pi.resources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unex.pi.dao.DishDAO;
import es.unex.pi.dao.JDBCDishDAOImpl;
import es.unex.pi.dao.JDBCRestaurantCategoriesDAOImpl;
import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.JDBCReviewsDAOImpl;
import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.RestaurantCategoriesDAO;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.dao.ReviewsDAO;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.Category;
import es.unex.pi.model.Dish;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.RestaurantCategories;
import es.unex.pi.model.RestaurantWithCategories;
import es.unex.pi.model.Review;
import es.unex.pi.model.User;
import es.unex.pi.resources.exceptions.CustomBadRequestException;
import es.unex.pi.resources.exceptions.CustomNotFoundException;
import es.unex.pi.util.LevenshteinSearch;
import es.unex.pi.util.Triplet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/restaurants")
public class RestaurantsResource {
	
	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;
	
	private static final String ID_REGEXP = "[0-9]+";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Restaurant> getUserRestaurantsJSON(@Context HttpServletRequest request) {	  
		List<Restaurant> restaurants = null;
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
	 
		// Se obtiene el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
	  
		restaurants = restaurantDAO.getAllByUser(user.getId());
		
		return restaurants;
	}
	
	@GET
	@Path("/{restaurantid: "+ID_REGEXP+"}")
	@Produces(MediaType.APPLICATION_JSON)
	public Restaurant getRestaurantJSON(@PathParam("restaurantid") long restaurantid,
										@Context HttpServletRequest request) {	  
		Restaurant restaurant = null;
		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		restaurant = restaurantDAO.get(restaurantid);
		
		if(restaurant != null) {
			return restaurant;
		} else {
			throw new CustomNotFoundException("Error: Restaurant not found");
		}
	}
	
	@GET
	@Path("/{restaurantid: "+ID_REGEXP+"}/categories")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Category> getRestaurantCategoriesJSON(@PathParam("restaurantid") long restaurantid,
										@Context HttpServletRequest request) {	  
		Restaurant restaurant = null;
		List<Category> categories = null;
		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
	  
		// Se recupera el resturante
		restaurant = restaurantDAO.get(restaurantid);
		
		if(restaurant != null) {
			// Se recuperan las categorías del restaurante
			RestaurantCategoriesDAO restaurantCategoriesDAO = new JDBCRestaurantCategoriesDAOImpl();
			restaurantCategoriesDAO.setConnection(conn);
			categories = restaurantCategoriesDAO.getRestaurantCategories(restaurant.getId());
			return categories;
		} else {
			throw new CustomNotFoundException("Error: Restaurant not found");
		}
	}
	
	@GET
	@Path("/{restaurantid: "+ID_REGEXP+"}/dishes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Dish> getRestaurantDishesJSON(@PathParam("restaurantid") long restaurantid,
										@Context HttpServletRequest request) {	  
		Restaurant restaurant = null;
		List<Dish> dishes = null;
		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
	  
		// Se recupera el resturante
		restaurant = restaurantDAO.get(restaurantid);
		
		if(restaurant != null) {
			// Se recuperan los platos del restaurante
			DishDAO dishDAO = new JDBCDishDAOImpl();
			dishDAO.setConnection(conn);
			dishes = dishDAO.getByRestaurantId(restaurant.getId());
			return dishes;
		} else {
			throw new CustomNotFoundException("Error: Restaurant not found");
		}
	}
	
	@GET
	@Path("/{restaurantid: "+ID_REGEXP+"}/reviews")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Triplet<Review, User, Integer>> getRestaurantReviewsJSON(@PathParam("restaurantid") long restaurantid,
																		@Context HttpServletRequest request) {	  
		Restaurant restaurant = null;
		List<Triplet<Review, User, Integer>> completeReviews = new ArrayList<>();
		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
	  
		// Se recupera el resturante
		restaurant = restaurantDAO.get(restaurantid);
		
		if(restaurant != null) {
			// Se recuperan las reviews del restaurante
			ReviewsDAO reviewsDAO = new JDBCReviewsDAOImpl();
			reviewsDAO.setConnection(conn);
			List<Review> reviews = reviewsDAO.getAllByRestaurant(restaurant.getId());
			// Se van a necesitar los usuarios que han hecho esas reviews
			UserDAO userDAO = new JDBCUserDAOImpl();
			userDAO.setConnection(conn);
			User user;
			Triplet<Review, User, Integer> triplet;
			for (Review review : reviews) {
				user = userDAO.get(review.getIdu());
				user.setPassword("*********");
				triplet = new Triplet<Review, User, Integer>(review, user, 0);
				completeReviews.add(triplet);
			}
			return completeReviews;
		} else {
			throw new CustomNotFoundException("Error: Restaurant not found");
		}
	}
	
	@DELETE
	@Path("/{restaurantid: "+ID_REGEXP+"}")  
	public Response deleteRestaurant(@PathParam("restaurantid") long restaurantid,
		  					  @Context HttpServletRequest request) {
	  
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		RestaurantDAO restaurantDao = new JDBCRestaurantDAOImpl();
		restaurantDao.setConnection(conn);
		
		// Se recupera el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// Se obtiene el restaurante que se desea eliminar
		Restaurant restaurant = restaurantDao.get(restaurantid);
		
		if(restaurant != null) {
			if(restaurant.getIdu() == user.getId()) {
				// Se elimina el restaurante
				restaurantDao.delete(restaurantid);
				return Response.noContent().build(); //204 no content
			} else {
				throw new CustomBadRequestException("Error deleting restaurant: you are not the owner");
			}
		} else {
			throw new CustomNotFoundException("Error deleting restaurant: restaurant does not exist");
		}
	}
	
	@GET
	@Path("/category/{categoryid: "+ID_REGEXP+"}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Restaurant> getRestaurantsByCategoryJSON(@PathParam("categoryid") long categoryid,
														 @Context HttpServletRequest request) {	  
		List<Restaurant> restaurants = null;
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		RestaurantCategoriesDAO restaurantCategoriesDAO = new JDBCRestaurantCategoriesDAOImpl();
		restaurantCategoriesDAO.setConnection(conn);
	  
		// Se obtiene la lista de restaurantes con la categoría pasada
		restaurants = restaurantCategoriesDAO.getRestaurantsByCategory(categoryid);
		
		return restaurants;
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, List<Restaurant>> getRestaurantsByFilterJSON(@QueryParam("searchText") String searchText,
														@QueryParam("available") @DefaultValue("-1") String available,
														@QueryParam("order") @DefaultValue("0") String order,
														 @Context HttpServletRequest request) {	  
		
		if(searchText == null || available == null || order == null) {
			throw new CustomBadRequestException("Bad filters provided");
		}
		
		if(!order.matches("0|1")) {
			throw new CustomBadRequestException("No correct order value provided");
		}
		
		Map<String, List<Restaurant>> restaurantListsMap = new HashMap<>();
		
		// Se recupera la conexión con la BD y se establece a través de RestaurantDAO
		Connection connection = (Connection) sc.getAttribute("dbConn");
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(connection);
		
		// Se recuperan los restaurantes de la BD en función de la disponibilidad seleccionada y el orden
		List<Restaurant> allRestaurants = new ArrayList<>();
		switch (available) {
		case "0":
		case "1":
			allRestaurants = restaurantDAO.getAllByAvailabilityOrdered(Integer.parseInt(available), Integer.parseInt(order));
			break;
		case "-1":
			allRestaurants = restaurantDAO.getAllOrdered(Integer.parseInt(order));
			break;
		default: // Si se ha modificado la URL y el valor de disponibilidad es cualquier otro, se lanza excepción
			throw new CustomBadRequestException("No correct available value provided");
		}
		
		if(searchText.isEmpty() || searchText.isBlank() || searchText.equals("null")) { // Si no se introducido ningún valor en la barra de búsqueda, no se filtra la lista allRestaurants obtenida y se devuelve tal cual
			restaurantListsMap.put("noSearchTextList", allRestaurants);
			return restaurantListsMap;
		} else {
			List<Restaurant> listCityAddress = new ArrayList<>();
			List<Restaurant> listName = new ArrayList<>();
			List<Restaurant> listDescription = new ArrayList<>();
			for (Restaurant restaurant : allRestaurants) {
				// Se recuperan los retaurantes coincidentes por ciudad o dirección: tolerancia a fallo de 3 caracteres o inclusión de la cadena
				if ((LevenshteinSearch.distance(restaurant.getCity().toLowerCase(), searchText.toLowerCase()) <= 3 || restaurant.getCity().toLowerCase().contains(searchText.toLowerCase()) || searchText.toLowerCase().contains(restaurant.getCity().toLowerCase()))
					|| (LevenshteinSearch.distance(restaurant.getAddress().toLowerCase(), searchText.toLowerCase()) <= 3 || restaurant.getAddress().toLowerCase().contains(searchText.toLowerCase()))) {
	                listCityAddress.add(restaurant);
	            }
				// Se recuperan los retaurantes coincidentes por nombre: tolerancia a fallo de 3 caracteres o inclusión de la cadena
				if (LevenshteinSearch.distance(restaurant.getName().toLowerCase(), searchText.toLowerCase()) <= 3 || restaurant.getName().toLowerCase().contains(searchText.toLowerCase()) || searchText.toLowerCase().contains(restaurant.getName().toLowerCase())) {
	                listName.add(restaurant);
	            }
				// Se recuperan los retaurantes coincidentes por descripción: tolerancia a fallo de 7 caracteres
				if (LevenshteinSearch.distance(restaurant.getDescription().toLowerCase(), searchText.toLowerCase()) <= 7) {
	                listDescription.add(restaurant);
	            }		
			}
			
			// Se añaden las listas al mapa
			restaurantListsMap.put("listCityAddress", listCityAddress);
			restaurantListsMap.put("listName", listName);
			restaurantListsMap.put("listDescription", listDescription);
			
			return restaurantListsMap;
		}			
	}
	
	@POST	  	  
	@Consumes(MediaType.APPLICATION_JSON)
	public Response post(RestaurantWithCategories restaurantWithCategories, @Context HttpServletRequest request) throws Exception {	
		
		// Se obtiene el restaurante
		Restaurant restaurant = restaurantWithCategories.getRestaurant();
		
		// Se obtiene la lista de IDs de categorías
		List<Long> categoriesIds = restaurantWithCategories.getCategoriesIds();
		
		Map<String, String> messages = new HashMap<String, String>();
		
		// Si no hay categorías se devuelve un error
		if(categoriesIds == null || categoriesIds.size() == 0) {
			messages.put("error", "Se debe seleccionar, como mínimo, una categoría.");
			throw new CustomBadRequestException(messages);
		}
		
		// Se recupera el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// Se asigna al nuevo restaurante el ID de usuario
		restaurant.setIdu(user.getId());
		
		// Se recupera la conexión con la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");	  	 

		// Se comprueba la validez de los datos introducidos
		if (!restaurant.validate(messages)) {
			// Hay errores en los datos introducidos
			throw new CustomBadRequestException(messages);
		} else { 
			// Datos válidos
			// Se comprueba si ya existe un restaurante con el mismo nombre
			RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
			restaurantDAO.setConnection(conn);						
			if(restaurantDAO.get(restaurant.getName()) != null) {
				// Ya hay un restaurante con el mismo nombre. Se añade un mensaje de error y lanza excepción
				messages.put("name_already_exists", "Ya existe un restaurante con el mismo nombre.");	
				throw new CustomBadRequestException(messages);
			} else if(restaurantDAO.getByEmail(restaurant.getContactEmail()) != null) { // Se comprueba si el email se repite
					// Ya hay un restaurante con el mismo email. Se añade un mensaje de error y se lanza excepción
					messages.put("email_already_exists", "Ya existe un restaurante con el mismo email.");
					throw new CustomBadRequestException(messages);
			} else { // Los datos son válidos y no existe un restaurante con el mismo nombre o email
					// Se almacena el nuevo restaurante en la base de datos
					long newId = restaurantDAO.add(restaurant);
					// Se almacenan las categorías del restaurante en la base de datos
					// Se crea un mapa de objetos RestaurantCategories para ello
					Map<Long, Long> restaurantCategoriesMap = new HashMap<>();
					for (Long categoryId : categoriesIds) {
						restaurantCategoriesMap.put(categoryId, categoryId);
					}
					RestaurantCategoriesDAO restaurantCategoriesDAO = new JDBCRestaurantCategoriesDAOImpl();
					restaurantCategoriesDAO.setConnection(conn);
					restaurantCategoriesDAO.addAll(restaurantCategoriesMap, newId);
			
					// Se devuelve la respuesta
					Response response = Response //return 201 and Location
							   .created(
								uriInfo.getAbsolutePathBuilder()
									   .path(Long.toString(newId))
									   .build())
							   .contentLocation(
								uriInfo.getAbsolutePathBuilder()
								       .path(Long.toString(newId))
								       .build())
								.build();
					return response;
			}
		}
	}
	
	@PUT
	@Path("/{restaurantid: "+ID_REGEXP+"}") 
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put(RestaurantWithCategories restaurantWithCategories, 
			@PathParam("restaurantid") long restaurantid,
			@Context HttpServletRequest request) throws Exception {
		
		// Se recupera el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// Se obtiene el restaurante
		Restaurant restaurantUpdated = restaurantWithCategories.getRestaurant();
		
		// Se obtiene la lista de IDs de categorías
		List<Long> categoriesIds = restaurantWithCategories.getCategoriesIds();
		
		Map<String, String> messages = new HashMap<String, String>();
		
		// Si no hay categorías se devuelve un error
		if(categoriesIds == null || categoriesIds.size() == 0) {
			messages.put("error", "Se debe seleccionar, como mínimo, una categoría.");
			throw new CustomBadRequestException(messages);
		}
		
		// Se recupera la conexión con la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		
		// Se recupera el restaurante que se quiere actualizar
		Restaurant restaurant = restaurantDAO.get(restaurantUpdated.getId());
		
		if(restaurant == null) {
			// No existe el restaurante que se quiere actualizar
			throw new CustomNotFoundException("Restaurant does not exist");
		}
		
		if(restaurant.getId() != restaurantid) {
			// El ID del restaurante que se quiere actualizar no coincide con el ID pasado en la request
			throw new CustomBadRequestException("Error in ID");
		}
		
		if(restaurant.getIdu() != user.getId() || restaurant.getIdu() != restaurantUpdated.getIdu()) {
			// El restaurante no es del usuario o este ha sido modificado
			throw new CustomBadRequestException("Restaurant does not belong to user");
		} 
		
		// El restaurante existe, pertenece al usuario y el ID de la url coincide con el ID pasado en el objeto de la request
		// Se pasa a comprobar a ver si sus datos son válidos
		if (!restaurantUpdated.validate(messages)) {
			// Hay errores en los datos introducidos
			throw new CustomBadRequestException(messages);
		} else { 
			// Datos válidos
			// Se comprueba si ya existe un restaurante con el mismo nombre o email y que no sea él mismo
			Restaurant restaurantSameName = new Restaurant();
			restaurantSameName = restaurantDAO.get(restaurantUpdated.getName());
			Restaurant restaurantSameEmail = new Restaurant();
			restaurantSameEmail = restaurantDAO.getByEmail(restaurantUpdated.getContactEmail());
			if(restaurantSameName != null && !(restaurantSameName.getId() == restaurantUpdated.getId())) {
				// Ya hay un restaurante con el mismo nombre. Se añade un mensaje de error y se despacha
				messages.put("name_already_exists", "Ya existe un restaurante con el mismo nombre.");	
				throw new CustomBadRequestException(messages);
			} else if(restaurantSameEmail != null && !(restaurantSameEmail.getId() == restaurantUpdated.getId())) { // Se comprueba si el email se repite
					// Ya hay un restaurante con el mismo email. Se añade un mensaje de error y se despacha
					messages.put("email_already_exists", "Ya existe un restaurante con el mismo email.");
					System.out.println("AAAAAAAAAAAAAAAAAAAA");
					throw new CustomBadRequestException(messages);
			} else { // Los datos son válidos y no existe un restaurante con el mismo nombre
					// Se actualiza el restaurante en la base de datos
				System.out.println("BBBBBBBBBBBBBBBBBB");
					restaurantDAO.update(restaurantUpdated);
					// Se actualizan las categorías del restaurante en la base de datos: se eliminan los registros existentes para insertar los nuevos
					RestaurantCategoriesDAO restaurantCategoriesDAO = new JDBCRestaurantCategoriesDAOImpl();
					restaurantCategoriesDAO.setConnection(conn);
					restaurantCategoriesDAO.deleteAll(restaurantUpdated.getId());
					// Se crea el mapa de las categorías
					Map<Long, Long> restaurantCategoriesMap = new HashMap<>();
					for (Long categoryId : categoriesIds) {
						restaurantCategoriesMap.put(categoryId, categoryId);
					}
					restaurantCategoriesDAO.addAll(restaurantCategoriesMap, restaurantUpdated.getId());

					// Se devuelve la respuesta
					Response response = Response.noContent().build();
					return response;
			}
		}
	}
	
	/*------------------------------------------------------------
	---------------------AMPLIACIÓN 4-------------------------------
	------------------------------------------------------------*/
	
	@GET
	@Path("/{restaurantid: "+ID_REGEXP+"}/relatedByCategory")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Restaurant> getRestaurantsRelatedByCategoryJSON(@PathParam("restaurantid") long restaurantid,
																@Context HttpServletRequest request) {	  		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		
		Restaurant restaurant = restaurantDAO.get(restaurantid);
	 
		if(restaurant != null) {
			
			RestaurantCategoriesDAO restaurantCategoriesDAO = new JDBCRestaurantCategoriesDAOImpl();
			restaurantCategoriesDAO.setConnection(conn);
			
			// Se recuperan los restaurantes relacionados por categorías
			// Se usa un mapa para no duplicar restaurantes
			Map<Long, Restaurant> similarCategoriesMap = new HashMap<>();
			for (RestaurantCategories restaurantCategories : restaurantCategoriesDAO.getAllByRestaurant(restaurant.getId())) {
				for (RestaurantCategories restaurantCategories2 : restaurantCategoriesDAO.getAllByCategory(restaurantCategories.getIdct())) {
					similarCategoriesMap.put(restaurantCategories2.getIdr(), restaurantDAO.get(restaurantCategories2.getIdr()));
				}
			}
			// Se elimina del mapa el propio restaurante
			similarCategoriesMap.remove(restaurant.getId()); 
			
			// Se transforma el mapa a una lista y se devuelve
			List<Restaurant> restaurants = new ArrayList<>(similarCategoriesMap.values());
			
			return restaurants;
		} else {
			throw new CustomNotFoundException("Error reading restaurants related by category: restaurant does not exist");
		}
	}
	
	@GET
	@Path("/{restaurantid: "+ID_REGEXP+"}/relatedByCity")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Restaurant> getRestaurantsRelatedByCityJSON(@PathParam("restaurantid") long restaurantid,
															@Context HttpServletRequest request) {	  
		List<Restaurant> restaurants = null;
		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		
		Restaurant restaurant = restaurantDAO.get(restaurantid);
	 
		if(restaurant != null) {
			restaurants = restaurantDAO.getCityRelated(restaurant);
			return restaurants;
		} else {
			throw new CustomNotFoundException("Error reading restaurants related by city: restaurant does not exist");
		}
	}
	
	@GET
	@Path("/{restaurantid: "+ID_REGEXP+"}/relatedByPrice")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Restaurant> getRestaurantsRelatedByPriceJSON(@PathParam("restaurantid") long restaurantid,
															@Context HttpServletRequest request) {	  
		List<Restaurant> restaurants = null;
		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		
		Restaurant restaurant = restaurantDAO.get(restaurantid);
	 
		if(restaurant != null) {
			restaurants = restaurantDAO.getPriceRelated(restaurant);
			return restaurants;
		} else {
			throw new CustomNotFoundException("Error reading restaurants related by price: restaurant does not exist");
		}
	}
	
	@GET
	@Path("/{restaurantid: "+ID_REGEXP+"}/relatedByGrade")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Restaurant> getRestaurantsRelatedByGradeJSON(@PathParam("restaurantid") long restaurantid,
															@Context HttpServletRequest request) {	  
		List<Restaurant> restaurants = null;
		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		
		Restaurant restaurant = restaurantDAO.get(restaurantid);
	 
		if(restaurant != null) {
			restaurants = restaurantDAO.getGradeRelated(restaurant);
			return restaurants;
		} else {
			throw new CustomNotFoundException("Error reading restaurants related by grade: restaurant does not exist");
		}
	}

}
