package es.unex.pi.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import es.unex.pi.model.Category;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.RestaurantCategories;

public class JDBCRestaurantCategoriesDAOImpl implements RestaurantCategoriesDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCRestaurantCategoriesDAOImpl.class.getName());

	@Override
	public List<RestaurantCategories> getAll() {

		if (conn == null) return null;
						
		ArrayList<RestaurantCategories> restaurantCategoriesList = new ArrayList<RestaurantCategories>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM RestaurantCategories");
						
			while ( rs.next() ) {
				RestaurantCategories restaurantCategories = new RestaurantCategories();
				restaurantCategories.setIdr(rs.getInt("idr"));
				restaurantCategories.setIdct(rs.getInt("idct"));
						
				restaurantCategoriesList.add(restaurantCategories);
				logger.info("fetching all RestaurantCategories: "+restaurantCategories.getIdr()+" "+restaurantCategories.getIdct());
					
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return restaurantCategoriesList;
	}

	@Override
	public List<RestaurantCategories> getAllByCategory(long idct) {
		
		if (conn == null) return null;
						
		ArrayList<RestaurantCategories> restaurantCategoriesList = new ArrayList<RestaurantCategories>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM RestaurantCategories WHERE idct="+idct);

			while ( rs.next() ) {
				RestaurantCategories restaurantCategories = new RestaurantCategories();
				restaurantCategories.setIdr(rs.getInt("idr"));
				restaurantCategories.setIdct(rs.getInt("idct"));

				restaurantCategoriesList.add(restaurantCategories);
				logger.info("fetching all RestaurantCategories by idr: "+restaurantCategories.getIdr()+"->"+restaurantCategories.getIdct());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return restaurantCategoriesList;
	}
	
	@Override
	public List<RestaurantCategories> getAllByRestaurant(long idr) {
		
		if (conn == null) return null;
						
		ArrayList<RestaurantCategories> restaurantCategoriesList = new ArrayList<RestaurantCategories>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM RestaurantCategories WHERE Idr="+idr);

			while ( rs.next() ) {
				RestaurantCategories restaurantCategories = new RestaurantCategories();
				restaurantCategories.setIdr(rs.getInt("idr"));
				restaurantCategories.setIdct(rs.getInt("idct"));
							
				restaurantCategoriesList.add(restaurantCategories);
				logger.info("fetching all RestaurantCategories by idct: "+restaurantCategories.getIdct()+"-> "+restaurantCategories.getIdr());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return restaurantCategoriesList;
	}
	
	@Override
	public List<Category> getRestaurantCategories(long idr) {
		
		if (conn == null) return null;
						
		ArrayList<Category> restaurantCategoriesList = new ArrayList<Category>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Categories.name, Categories.id, Categories.description FROM RestaurantCategories JOIN Categories ON RestaurantCategories.idct = Categories.id WHERE Idr="+idr);

			while ( rs.next() ) {
				Category category = new Category();
				category.setName(rs.getString("name"));
				category.setId(rs.getInt("id"));
				category.setDescription(rs.getString("description"));
							
				restaurantCategoriesList.add(category);
				logger.info("fetching all Categories by idr: "+idr+"-> "+category.getName());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return restaurantCategoriesList;
	}
	
	@Override
	public List<Restaurant> getRestaurantsByCategory(long idct) {
		
		if (conn == null) return null;
						
		ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Restaurant.* FROM RestaurantCategories JOIN Restaurant ON RestaurantCategories.idr = Restaurant.id WHERE idct="+idct);

			while ( rs.next() ) {
				Restaurant restaurant = new Restaurant();
				restaurant.setId(rs.getInt("id"));
				restaurant.setName(rs.getString("name"));
				restaurant.setAddress(rs.getString("address"));
				restaurant.setTelephone(rs.getString("telephone"));
				restaurant.setCity(rs.getString("city"));
				restaurant.setIdu(rs.getInt("idu"));
				restaurant.setGradesAverage(rs.getFloat("gradesAverage"));
				restaurant.setMinPrice(rs.getFloat("minPrice"));
				restaurant.setContactEmail(rs.getString("contactemail"));
				restaurant.setDescription(rs.getString("description"));
				restaurant.setMaxPrice(rs.getFloat("maxPrice"));
				restaurant.setBikeFriendly(rs.getInt("bikeFriendly"));
				restaurant.setAvailable(rs.getInt("available"));
							
				restaurantList.add(restaurant);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return restaurantList;
	}
	
	
	@Override
	public RestaurantCategories get(long idr,long idct) {
		if (conn == null) return null;
		
		RestaurantCategories restaurantCategories = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM RestaurantCategories WHERE idr="+idr+" AND idct="+idct);			 
			if (!rs.next()) return null;
			restaurantCategories= new RestaurantCategories();
			restaurantCategories.setIdr(rs.getInt("idr"));
			restaurantCategories.setIdct(rs.getInt("idct"));

			logger.info("fetching RestaurantCategories by idr: "+restaurantCategories.getIdr()+"  and idct: "+restaurantCategories.getIdct());
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return restaurantCategories;
	}
	
	

	@Override
	public boolean add(RestaurantCategories restaurantCategories) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO RestaurantCategories (idr,idct) VALUES('"+
									restaurantCategories.getIdr()+"','"+
									restaurantCategories.getIdct()+"')");
						
				logger.info("creating RestaurantCategories:("+restaurantCategories.getIdr()+" "+restaurantCategories.getIdct());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}
	
	@Override
	public boolean addAll(Map<Long, Long> restaurantCategoriesMap, long idr) {
		boolean done = false;
		if (conn != null){
			
			for (Map.Entry<Long, Long> categoryEntry : restaurantCategoriesMap.entrySet()) {
				
				Statement stmt;
				try {
					stmt = conn.createStatement();
					stmt.executeUpdate("INSERT INTO RestaurantCategories (idr,idct) VALUES('"+
										idr+"','"+
										categoryEntry.getKey()+"')");
							
					logger.info("creating RestaurantCategories:("+idr+" "+categoryEntry.getKey());
					done= true;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return done;
	}
	

	@Override
	public boolean update(RestaurantCategories dbObject, RestaurantCategories newObject) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				
				stmt.executeUpdate("UPDATE RestaurantCategories SET idct="+newObject.getIdct()
				+" WHERE idr = "+dbObject.getIdr() + " AND idct = " + dbObject.getIdct());
				
				logger.info("updating RestaurantCategories:("+dbObject.getIdr()+" "+dbObject.getIdct());
				
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean delete(long idr, long idct) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM RestaurantCategories WHERE idr ="+idr+" AND idct="+idct);
				logger.info("deleting RestaurantCategories: "+idr+" , idct="+idct);
				done= true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return done;
	}
	
	@Override
	public boolean deleteAll(long idr) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM RestaurantCategories WHERE idr ="+idr);
				logger.info("deleting RestaurantCategories: "+idr);
				done= true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public void setConnection(Connection conn) {
		// TODO Auto-generated method stub
		this.conn = conn;
	}
	
}
