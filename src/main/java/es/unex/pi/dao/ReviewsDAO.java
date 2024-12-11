package es.unex.pi.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import es.unex.pi.model.Review;


public interface ReviewsDAO {

	/**
	 * set the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);

	/**
	 * Gets all the restaurant and the categories related to them from the database.
	 * 
	 * @return List of all the restaurant and the categories related to them from the database.
	 */
	
	public List<Review> getAll();

	/**
	 *Gets all the Review that are related to a user.
	 * 
	 * @param idu
	 *            User identifier
	 * 
	 * @return List of all the Review related to a user.
	 */
	public List<Review> getAllByUser(long idu);
	
	/**
	 * Gets all the Review that contains an specific restaurant.
	 * 
	 * @param idr
	 *            Restaurant Identifier
	 * 
	 * @return List of all the Review that contains an specific restaurant
	 */
	public List<Review> getAllByRestaurant(long idr);

	/**
	 * Gets a Review from the DB using idr and idu.
	 * 
	 * @param idr 
	 *            restaurant identifier.
	 *            
	 * @param idu
	 *            User Identifier
	 * 
	 * @return Review with that idr and idu.
	 */
	
	public Review get(long idr,long idu);
	
	public Map<Long, String> getUsernamesByRestaurant(long idr);

	/**
	 * Adds an Review to the database.
	 * 
	 * @param Review
	 *            Review object with the details of the relation between the restaurant and the user.
	 * 
	 * @return restaurant identifier or -1 in case the operation failed.
	 */
	
	public boolean add(Review Review);

	/**
	 * Updates an existing Review in the database.
	 * 
	 * @param Review
	 *            Review object with the new details of the existing relation between the restaurant and the user. 
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	
	public boolean update(Review Review);

	/**
	 * Deletes an Review from the database.
	 * 
	 * @param idr
	 *            Restaurant identifier.
	 *            
	 * @param idu
	 *            User Identifier
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	
	public boolean delete(long idr, long idu);
}