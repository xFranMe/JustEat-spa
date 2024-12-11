package es.unex.pi.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import es.unex.pi.model.Category;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.RestaurantCategories;


public interface RestaurantCategoriesDAO {

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
	
	public List<RestaurantCategories> getAll();

	/**
	 *Gets all the RestaurantCategory that are related to a category.
	 * 
	 * @param idct
	 *            Category identifier
	 * 
	 * @return List of all the RestaurantCategory related to a category.
	 */
	public List<RestaurantCategories> getAllByCategory(long idct);
	
	/**
	 * Gets all the RestaurantCategory that contains an specific restaurant.
	 * 
	 * @param idr
	 *            Restaurant Identifier
	 * 
	 * @return List of all the RestaurantCategory that contains an specific restaurant
	 */
	public List<RestaurantCategories> getAllByRestaurant(long idr);
	
	public List<Category> getRestaurantCategories(long idr);
	
	public List<Restaurant> getRestaurantsByCategory(long idct);

	/**
	 * Gets a RestaurantCategory from the DB using idr and idct.
	 * 
	 * @param idr 
	 *            restaurant identifier.
	 *            
	 * @param idct
	 *            Category Identifier
	 * 
	 * @return RestaurantCategory with that idr and idct.
	 */
	
	public RestaurantCategories get(long idr,long idct);

	/**
	 * Adds an RestaurantCategory to the database.
	 * 
	 * @param RestaurantCategory
	 *            RestaurantCategory object with the details of the relation between the restaurant and the category.
	 * 
	 * @return restaurant identifier or -1 in case the operation failed.
	 */
	
	public boolean add(RestaurantCategories RestaurantCategory);
	
	public boolean addAll(Map<Long, Long> restaurantCategories, long idr);

	/**
	 * Updates an existing RestaurantCategory in the database.
	 * 
	 * @param dbObject
	 *            RestaurantCategory object that is going to be updated in the database 
	 * @param newObject
	 *            RestaurantCategory object with the new details of the existing relation between the restaurant and the category. 
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	
	public boolean update(RestaurantCategories dbObject, RestaurantCategories newObject);

	/**
	 * Deletes an RestaurantCategory from the database.
	 * 
	 * @param idr
	 *            Restaurant identifier.
	 *            
	 * @param idct
	 *            Category Identifier
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	
	public boolean delete(long idr, long idct);
	
	public boolean deleteAll(long idr);
}