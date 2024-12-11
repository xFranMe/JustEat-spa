package es.unex.pi.dao;

import java.sql.Connection;
import java.util.List;

import es.unex.pi.model.Restaurant;


public interface RestaurantDAO {

	/**
	 * set the database connection in this DAO.
	 * 
	 * @param conn
	 *            database connection.
	 */
	public void setConnection(Connection conn);
	
	/**
	 * Gets a restaurant from the DB using id.
	 * 
	 * @param id
	 *            Restaurant Identifier.
	 * 
	 * @return Restaurant object with that id.
	 */
	public Restaurant get(long id);
	
	/**
	 * Gets a restaurant from the DB using name.
	 * 
	 * @param name
	 *            Restaurant name.
	 * 
	 * @return Restaurant object with that name.
	 */
	public Restaurant get(String name);
	
	/**
	 * Gets a restaurant from the DB using email.
	 * 
	 * @param email
	 *            Restaurant email.
	 * 
	 * @return Restaurant object with that email.
	 */
	public Restaurant getByEmail(String email);

	/**
	 * Gets all the notes from the database.
	 * 
	 * @return List of all the restaurants from the database.
	 */
	public List<Restaurant> getAll();
	
	/**
	 * Gets all the restaurants from the database that contain a text in the name.
	 * 
	 * @param search
	 *            Search string .
	 * 
	 * @return List of all the restaurants from the database that contain a text either in the name.
	 */	
	public List<Restaurant> getAllBySearchName(String search);


	/**
	 * Gets all the restaurants from the database that belong to a user.
	 * 
	 * @param idu
	 *            User identifier.
	 * 
	 * @return List of all the restaurants from the database that belong to a user
	 */	
	public List<Restaurant> getAllByUser(long idu);
	
	public List<Restaurant> getAllOrdered(int gradeOrder); // 0 - Mayor a menor, 1 - Menor a mayor
	
	public List<Restaurant> getCityRelated(Restaurant restaurant);
	
	public List<Restaurant> getPriceRelated(Restaurant restaurant);
	
	public List<Restaurant> getGradeRelated(Restaurant restaurant);
	
	public List<Restaurant> getAllByAvailability(int availability);
	
	public List<Restaurant> getAllByAvailabilityOrdered(int availability, int gradeOrder); // 0 - Mayor a menor, 1 - Menor a mayor

	/**
	 * Adds a restaurant to the database.
	 * 
	 * @param restaurant
	 *            Restaurant object with the restaurant details.
	 * 
	 * @return Restaurant identifier or -1 in case the operation failed.
	 */
	
	public long add(Restaurant restaurant);

	/**
	 * Updates an existing restaurant in the database.
	 * 
	 * @param restaurant
	 *            Restaurant object with the new details of the existing restaurant.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	public boolean update(Restaurant restaurant);
	
	public boolean updateGradesAverage(long id);

	/**
	 * Deletes a restaurant from the database.
	 * 
	 * @param id
	 *            Restaurant identifier.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	public boolean delete(long id);
}
