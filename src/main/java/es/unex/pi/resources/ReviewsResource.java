package es.unex.pi.resources;

import java.sql.Connection;

import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.JDBCReviewsDAOImpl;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.dao.ReviewsDAO;
import es.unex.pi.model.Review;
import es.unex.pi.model.User;
import es.unex.pi.resources.exceptions.CustomBadRequestException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/reviews")
public class ReviewsResource {
	
	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;
	
	private static final String ID_REGEXP = "[0-9]+";
	
	@POST	  	  
	@Consumes(MediaType.APPLICATION_JSON)
	public Response post(Review newReview, @Context HttpServletRequest request) throws Exception {	
		
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		ReviewsDAO reviewsDAO = new JDBCReviewsDAOImpl();
		reviewsDAO.setConnection(conn);	  	 
		
		// Se recupera el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		
		Response res;
		
		if(newReview.getIdu() != user.getId()) {
			throw new CustomBadRequestException("Review does not belong to user");
		} else if(reviewsDAO.get(newReview.getIdr(), newReview.getIdu()) != null) {
			throw new CustomBadRequestException("User already reviewd the restaurant");
		} else if(restaurantDAO.get(newReview.getIdr()) == null) {
			throw new CustomBadRequestException("Restaurant does not exist");	
		} else {
			// Se añade la nueva review a la BD
			reviewsDAO.add(newReview);
			// Se actualiza la nota media del restaurante
			restaurantDAO.updateGradesAverage(newReview.getIdr());
			// Se devuelve la respuesta
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
	
	@GET
	@Path("/{restaurantid: "+ID_REGEXP+"}")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean getUserReviewsJSON(@PathParam("restaurantid") long restaurantid,
											@Context HttpServletRequest request) {	  
		Review review = null;
		
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		ReviewsDAO reviewsDAO = new JDBCReviewsDAOImpl();
		reviewsDAO.setConnection(conn);
	 
		// Se obtiene el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
	  
		// Se comprueba si el usuario ha hecho una review al restaurante cuyo ID ha sido pasado
		review = reviewsDAO.get(restaurantid, user.getId());
		
		return review != null;
	}

}
