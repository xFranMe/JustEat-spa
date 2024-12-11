package es.unex.pi.dao;

import java.sql.Connection;
import java.util.List;

import es.unex.pi.model.User;

public interface UserDAO {

	/**
	 * Sets the database connection in this DAO.
	 * 
	 * @param conn database connection.
	 */
	public void setConnection(Connection conn);

	/**
	 * Gets an user from the DB using idu.
	 * 
	 * @param id User Identifier.
	 * 
	 * @return User object with that id.
	 */
	public User get(long id);

	/**
	 * Gets an user from the DB using name.
	 * 
	 * @param name Name of the user.
	 * 
	 * @return User object with that name.
	 */
	public User get(String name);
	
	/**
	 * Gets an user from the DB using email and password.
	 * 
	 * @param email Email of the user.
	 * @param password Password of the user.
	 * 
	 * @return User object with that email and password.
	 */
	public User get(String email, String password);
	
	/**
	 * Gets an user from the DB using email.
	 * 
	 * @param email Email of the user.
	 * 
	 * @return User object with that email.
	 */
	public User getByEmail(String email);

	/**
	 * Gets all the users from the database.
	 * 
	 * @return List of all the users from the database.
	 */
	public List<User> getAll();

	/**
	 * Adds an user to the database.
	 * 
	 * @param user User object with the user details.
	 * 
	 * @return User identifier or -1 in case the operation failed.
	 */
	public long add(User user);

	/**
	 * Updates an existing user in the database.
	 * 
	 * @param user User object with the new details of the existing user.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	public boolean update(User user);

	/**
	 * Deletes an user from the database.
	 * 
	 * @param idu User identifier.
	 * 
	 * @return True if the operation was made and False if the operation failed.
	 */
	public boolean delete(long idu);
}
