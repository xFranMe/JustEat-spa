package es.unex.pi.resources;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unex.pi.dao.DishDAO;
import es.unex.pi.dao.JDBCDishDAOImpl;
import es.unex.pi.dao.JDBCOrderDAOImpl;
import es.unex.pi.dao.JDBCOrderDishesDAOImpl;
import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.OrderDAO;
import es.unex.pi.dao.OrderDishesDAO;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.model.Dish;
import es.unex.pi.model.Order;
import es.unex.pi.model.OrderDishes;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.User;
import es.unex.pi.resources.exceptions.CustomBadRequestException;
import es.unex.pi.resources.exceptions.CustomNotFoundException;
import es.unex.pi.util.Triplet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/orders")
public class OrdersResource {
	
	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;
	
	private static final String ID_REGEXP = "[0-9]+";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Order> getUserOrdersJSON(@Context HttpServletRequest request) {	  
		List<Order> orders = null;
		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		OrderDAO orderDAO = new JDBCOrderDAOImpl();
		orderDAO.setConnection(conn);
	 
		// Se obtiene el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
	  
		orders = orderDAO.getAllFromUser(user.getId());
		
		return orders;
	}
	
	@GET
	@Path("/{orderid: "+ID_REGEXP+"}")
	@Produces(MediaType.APPLICATION_JSON)
	public Order getOrderJSON(@PathParam("orderid") long orderid, @Context HttpServletRequest request) {	  
		Order order = null;
		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		OrderDAO orderDAO = new JDBCOrderDAOImpl();
		orderDAO.setConnection(conn);
	 
		// Se obtiene el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
	  
		order = orderDAO.get(orderid);
		
		if(order == null) {
			// No existe el pedido solicitado
			throw new CustomNotFoundException("Order does not exists");
		} else {
			if(order.getIdu() != user.getId()) {
				// El pedido existe pero no pertenece al usuario
				throw new CustomBadRequestException("Order does not belong to user");
			} else {
				// Existe el pedido y pertenece al usuario
				return order;
			}
		}		
	}
	
	@GET
	@Path("/{orderid: "+ID_REGEXP+"}/dishes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Triplet<OrderDishes, Dish, Restaurant>> getOrderDishesJSON(@PathParam("orderid") long orderid, @Context HttpServletRequest request) {	  
				    
		// Se obtiene el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		OrderDAO orderDAO = new JDBCOrderDAOImpl();
		orderDAO.setConnection(conn);
		
		// Se recupera el pedido 
		Order order = orderDAO.get(orderid);
		
		if(order == null) {
			// No existe el pedido solicitado
			throw new CustomNotFoundException("Order does not exists");
		}
		
		if(order.getIdu() != user.getId()) {
			// El pedido existe pero no pertenece al usuario
			throw new CustomBadRequestException("Order does not belong to user");
		}
		
		// El pedido existe y pertenece al usuario
		
		// Lista para ir guardando los datos del plato, cantidad y su restaurante
		List<Triplet<OrderDishes, Dish, Restaurant>> dishesList = new ArrayList<>();

		// Se recuperan todos los objetos OrderDishes del pedido
		OrderDishesDAO orderDishesDAO = new JDBCOrderDishesDAOImpl();
		orderDishesDAO.setConnection(conn);
		List<OrderDishes> orderDishesList = orderDishesDAO.getAllByOrder(order.getId());
		
		// A partir de los anteriores objetos, se recupera la información de los platos y sus restaurantes
		// En caso de que restaurantes y/o platos hayan sido borrados, se mostrará en el pedido como eliminados
		DishDAO dishDAO = new JDBCDishDAOImpl();
		dishDAO.setConnection(conn);
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		Dish dish;
		Restaurant restaurant;
		Triplet<OrderDishes, Dish, Restaurant> triplet;
		for (OrderDishes orderDishes : orderDishesList) {
			dish = dishDAO.get(orderDishes.getIddi());
			if(dish == null) { // Si el plato ha sido borrado no se puede acceder al restaurante
				dish = new Dish();
				dish.setName("Plato eliminado");
				restaurant = new Restaurant();
				restaurant.setName("La información del plato no está disponible");
			} else {
				restaurant = restaurantDAO.get(dish.getIdr());
			}
			triplet = new Triplet<OrderDishes, Dish, Restaurant>(orderDishes, dish, restaurant);
			dishesList.add(triplet);
		}
		return dishesList;		
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("/cart")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Triplet<OrderDishes, Dish, Restaurant>> getCurrentOrderDishesJSON(@Context HttpServletRequest request) {	  
	 
		Map<Long, Triplet<OrderDishes, Dish, Restaurant>> orderDishesMap = new HashMap<>();
		
		// Se obtiene de la sesión el mapa de platos del pedido actual
		HttpSession session = request.getSession();
		orderDishesMap = (Map<Long, Triplet<OrderDishes, Dish, Restaurant>>) session.getAttribute("orderDishesList");
		
		if(orderDishesMap == null) {
			return null;
		} else {
			// Se transforma el mapa en una lista y se retorna
			List<Triplet<OrderDishes, Dish, Restaurant>> dishesList = new ArrayList<>(orderDishesMap.values());
			return dishesList;
		}
	}
	
	@SuppressWarnings("unchecked")
	@DELETE
	@Path("/cart/{dishid: "+ID_REGEXP+"}")
	public Response deleteDishFromOrder(@PathParam("dishid") long dishid, @Context HttpServletRequest request) {	  
		
		Map<Long, Triplet<OrderDishes, Dish, Restaurant>> orderDishesMap = new HashMap<>();
		
		// Se obtiene de la sesión el mapa de platos del pedido actual
		HttpSession session = request.getSession();
		orderDishesMap = (Map<Long, Triplet<OrderDishes, Dish, Restaurant>>) session.getAttribute("orderDishesList");
		
		// Se elimina el plato del pedido
		orderDishesMap.remove(dishid);
		
		// Se guarda el nuevo estado del mapa en la sesión
		session.setAttribute("orderDishesList", orderDishesMap);
		
		return Response.noContent().build(); //204 no content		
	}
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/cart/{dishid: "+ID_REGEXP+"}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDishToOrder(OrderDishes orderDishes, @PathParam("dishid") long dishid, @Context HttpServletRequest request) throws Exception {	
		
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		DishDAO dishDAO = new JDBCDishDAOImpl();
		dishDAO.setConnection(conn);
		
		// Se recupera el plato de la BD
		Dish dish = dishDAO.get(dishid);
		
		// Se comprueba que exista el plato
		if(dish == null) {
			throw new CustomNotFoundException("Dish to add not found");
		}
		
		// Se comprueba que la cantidad seleccionada sea la adecuada
		if(orderDishes.getAmount() < 1 || orderDishes.getAmount() > 100) {
			throw new CustomBadRequestException("Not the correct amount selected.");
		}
		
		// El plato sí existe en la BD, por lo que se añade al pedido actual con toda la info requerida
		
		// Se recupera también la información del restaurante al que pertenece el plato
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		Restaurant restaurant = restaurantDAO.get(dish.getIdr());
		
		// Se recupera de la sesión la lista de platos (es un mapa, para agilizar búsquedas) que ya se tienen en el pedido
		// Si no existe aún el mapa de platos, se crea
		HttpSession session = request.getSession();
		Map<Long, Triplet<OrderDishes, Dish, Restaurant>> orderDishesMap = (Map<Long, Triplet<OrderDishes, Dish, Restaurant>>) session.getAttribute("orderDishesList");
		if(orderDishesMap == null) {
			// Se crea el mapa
			orderDishesMap = new HashMap<>();
		}
		
		// Hay que buscar si ya tiene una entrada con el ID del plato que se quiere añadir
		Triplet<OrderDishes, Dish, Restaurant> triplet = orderDishesMap.get(dish.getId());
		if(triplet != null) {
			// Ya existía el plato en el pedido, por lo que simplemente se actualiza la cantidad
			int currentAmount = triplet.getFirst().getAmount();
			triplet.getFirst().setAmount(currentAmount + orderDishes.getAmount());
		} else {
			// No existe el plato en el pedido, por lo que se crea el nuevo triplet
			// Se necesita OrderDishes, Dish y Restaurant (ya tenemos dish y restaurant)
			OrderDishes newOrderDishes = new OrderDishes();
			newOrderDishes.setIddi(dish.getId());
			newOrderDishes.setAmount(orderDishes.getAmount());
			triplet = new Triplet<OrderDishes, Dish, Restaurant>(newOrderDishes, dish, restaurant);
		}
		
		// Se añade/actualiza la entrada
		orderDishesMap.put(dish.getId(), triplet);
		
		// Se añade a la sesión el mapa una vez ya está actualizado
		session.setAttribute("orderDishesList", orderDishesMap);
		
		// Se retorna la respuesta
		Response res;
		
		res = Response //return 201
				   .created(
					uriInfo.getAbsolutePathBuilder()
						   .build())
				   .contentLocation(
					uriInfo.getAbsolutePathBuilder()
					       .build())
					.build();
		return res;		
	}
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/cart")
	public Response postOrder(@Context HttpServletRequest request) throws Exception {	
		
		// Se recupera de la sesión el mapa con todos los platos del pedido
		HttpSession session = request.getSession();
		Map<Long, Triplet<OrderDishes, Dish, Restaurant>> orderDishesMap = (Map<Long, Triplet<OrderDishes, Dish, Restaurant>>) session.getAttribute("orderDishesList");
		// Se recupera de la sesión el usuario que realiza el pedido (el que está logeado)
		User user = (User) session.getAttribute("user");
		
		// Si no se ha añadido nada aún al pedido se lanza una excepción
		if(orderDishesMap == null || orderDishesMap.isEmpty()) {
			throw new CustomBadRequestException("No order is ready to be added yet. Add any dish first.");
		}
		
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		
		// Se recorre la lista de platos del mapa y se calcula el precio total del pedido
		float totalPrice = 0;
		for (var entry : orderDishesMap.entrySet()) {
		    totalPrice = totalPrice + (entry.getValue().getFirst().getAmount() * entry.getValue().getSecond().getPrice());
		}
		
		// Se crea el objeto pedido pertinente
		Order order = new Order(); // El ID del pedido lo generará la BD
		order.setIdu(user.getId());
		order.setTotalPrice(totalPrice);
		
		// Se añade el pedido a la BD
		OrderDAO orderDAO = new JDBCOrderDAOImpl();
		orderDAO.setConnection(conn);
		long orderid = orderDAO.add(order); // Se obtiene el ID del pedido recién creado
		
		// Se recorre la lista de platos del mapa y se van añadiendo a la BD
		OrderDishesDAO orderDishesDAO = new JDBCOrderDishesDAOImpl();
		orderDishesDAO.setConnection(conn);
		OrderDishes orderDishes;
		for (var entry : orderDishesMap.entrySet()) {
		    orderDishes = entry.getValue().getFirst();
		    orderDishes.setIdo(orderid); // Aquí se añade el ID del pedido
		    orderDishesDAO.add(orderDishes);
		}
		
		// Se borra el mapa de platos del pedido de la sesión
		session.removeAttribute("orderDishesList");
		
		// Se retorna la respuesta
		Response res;
		
		res = Response //return 201
				   .created(
					uriInfo.getAbsolutePathBuilder()
						   .build())
				   .contentLocation(
					uriInfo.getAbsolutePathBuilder()
					       .build())
					.build();
		return res;		
	}

}
