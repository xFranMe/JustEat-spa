package es.unex.pi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.unex.pi.model.Restaurant;

public class JDBCRestaurantDAOImpl implements RestaurantDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCRestaurantDAOImpl.class.getName());
	
	@Override
	public Restaurant get(long id) {
		if (conn == null) return null;
		
		Restaurant restaurant = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM restaurant WHERE id ="+id);			 
			if (!rs.next()) return null; 
			restaurant  = new Restaurant();	 
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

			logger.info("fetching restaurant: "+restaurant.getId());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return restaurant;
	}
	
	@Override
	public Restaurant get(String name) {
		if (conn == null) return null;
		
		Restaurant restaurant = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM restaurant WHERE name ='"+name+"'");			 
			if (!rs.next()) return null; 
			restaurant  = new Restaurant();	 
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

			logger.info("fetching restaurant: "+restaurant.getId());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return restaurant;
	}
	
	@Override
	public Restaurant getByEmail(String email) {
		if (conn == null) return null;
		
		Restaurant restaurant = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM restaurant WHERE contactemail ='"+email+"'");			 
			if (!rs.next()) return null; 
			restaurant  = new Restaurant();	 
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

			logger.info("fetching restaurant: "+restaurant.getId());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return restaurant;
	}
	
	public List<Restaurant> getAll() {

		if (conn == null) return null;
		
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
			Statement stmt;
			ResultSet rs;
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM restaurant");
			while ( rs.next() ) {
				Restaurant restaurant = new Restaurant();
				restaurant  = new Restaurant();	 
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
				
				restaurants.add(restaurant);
				logger.info("fetching restaurant: "+restaurant.getId());							
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return restaurants;
	}
	
	public List<Restaurant> getAllBySearchName(String search) {
		search = search.toUpperCase();
		if (conn == null)
			return null;

		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM restaurant WHERE UPPER(name) LIKE '%" + search + "%'");

			while (rs.next()) {
				Restaurant restaurant = new Restaurant();
				
				restaurant  = new Restaurant();	 
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
				
				restaurants.add(restaurant);
				
				logger.info("fetching restaurant: "+restaurant.getId());
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return restaurants;
	}
	

	
	public List<Restaurant> getAllByUser(long idu) {
		
		if (conn == null)
			return null;

		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM restaurant WHERE idu = "+ idu);

			while (rs.next()) {
				Restaurant restaurant = new Restaurant();
				restaurant  = new Restaurant();	 
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
				restaurants.add(restaurant);
		
				logger.info("fetching restaurants: "+restaurant.getId());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return restaurants;
	}
	
	public List<Restaurant> getAllOrdered(int gradeOrder) {

		if (conn == null) return null;
		
		String order;
		if (gradeOrder == 0) {
			order = "DESC";
		} else {
			order = "ASC";
		}
		
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
			Statement stmt;
			ResultSet rs;
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM restaurant ORDER BY gradesAverage "+order);
			while ( rs.next() ) {
				Restaurant restaurant = new Restaurant();
				restaurant  = new Restaurant();	 
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
				
				restaurants.add(restaurant);
				logger.info("fetching restaurant: "+restaurant.getId());							
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return restaurants;
	}
	
	public List<Restaurant> getCityRelated(Restaurant restaurant) {
			
		if (conn == null)
			return null;

		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM restaurant WHERE city = '"+restaurant.getCity()+"' AND id <> "+restaurant.getId());

			while (rs.next()) {
				Restaurant restaurantAux = new Restaurant();
				restaurantAux  = new Restaurant();	 
				restaurantAux.setId(rs.getInt("id"));
				restaurantAux.setName(rs.getString("name"));
				restaurantAux.setAddress(rs.getString("address"));
				restaurantAux.setTelephone(rs.getString("telephone"));
				restaurantAux.setCity(rs.getString("city"));
				restaurantAux.setIdu(rs.getInt("idu"));
				restaurantAux.setGradesAverage(rs.getFloat("gradesAverage"));
				restaurantAux.setMinPrice(rs.getFloat("minPrice"));
				restaurantAux.setContactEmail(rs.getString("contactemail"));
				restaurantAux.setDescription(rs.getString("description"));
				restaurantAux.setMaxPrice(rs.getFloat("maxPrice"));
				restaurantAux.setBikeFriendly(rs.getInt("bikeFriendly"));
				restaurantAux.setAvailable(rs.getInt("available"));
				restaurants.add(restaurantAux);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return restaurants;
	}

	public List<Restaurant> getPriceRelated(Restaurant restaurant) {
		
		if (conn == null)
			return null;
	
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM restaurant WHERE (minPrice BETWEEN "+(restaurant.getMinPrice()-1)+" AND "+(restaurant.getMinPrice()+1)+") AND (maxPrice BETWEEN "+(restaurant.getMaxPrice()-1)+" AND "+(restaurant.getMaxPrice()+1)+") AND id <> "+restaurant.getId());
	
			while (rs.next()) {
				Restaurant restaurantAux = new Restaurant();
				restaurantAux  = new Restaurant();	 
				restaurantAux.setId(rs.getInt("id"));
				restaurantAux.setName(rs.getString("name"));
				restaurantAux.setAddress(rs.getString("address"));
				restaurantAux.setTelephone(rs.getString("telephone"));
				restaurantAux.setCity(rs.getString("city"));
				restaurantAux.setIdu(rs.getInt("idu"));
				restaurantAux.setGradesAverage(rs.getFloat("gradesAverage"));
				restaurantAux.setMinPrice(rs.getFloat("minPrice"));
				restaurantAux.setContactEmail(rs.getString("contactemail"));
				restaurantAux.setDescription(rs.getString("description"));
				restaurantAux.setMaxPrice(rs.getFloat("maxPrice"));
				restaurantAux.setBikeFriendly(rs.getInt("bikeFriendly"));
				restaurantAux.setAvailable(rs.getInt("available"));
				restaurants.add(restaurantAux);
			}
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return restaurants;
	}
	
	public List<Restaurant> getGradeRelated(Restaurant restaurant) {
		
		if (conn == null)
			return null;

		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM restaurant WHERE (gradesAverage BETWEEN "+(restaurant.getGradesAverage()-0.5)+" AND "+(restaurant.getGradesAverage()+0.5)+") AND id <> "+restaurant.getId());

			while (rs.next()) {
				Restaurant restaurantAux = new Restaurant();
				restaurantAux  = new Restaurant();	 
				restaurantAux.setId(rs.getInt("id"));
				restaurantAux.setName(rs.getString("name"));
				restaurantAux.setAddress(rs.getString("address"));
				restaurantAux.setTelephone(rs.getString("telephone"));
				restaurantAux.setCity(rs.getString("city"));
				restaurantAux.setIdu(rs.getInt("idu"));
				restaurantAux.setGradesAverage(rs.getFloat("gradesAverage"));
				restaurantAux.setMinPrice(rs.getFloat("minPrice"));
				restaurantAux.setContactEmail(rs.getString("contactemail"));
				restaurantAux.setDescription(rs.getString("description"));
				restaurantAux.setMaxPrice(rs.getFloat("maxPrice"));
				restaurantAux.setBikeFriendly(rs.getInt("bikeFriendly"));
				restaurantAux.setAvailable(rs.getInt("available"));
				restaurants.add(restaurantAux);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return restaurants;
	}
	
	public List<Restaurant> getAllByAvailability(int availability) {
		if (conn == null)
			return null;

		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM restaurant WHERE available ==" + availability);

			while (rs.next()) {
				Restaurant restaurant = new Restaurant();
				
				restaurant  = new Restaurant();	 
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
				
				restaurants.add(restaurant);
				
				logger.info("fetching restaurant: "+restaurant.getId());
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return restaurants;
	}
	
	public List<Restaurant> getAllByAvailabilityOrdered(int availability, int gradeOrder) {
		if (conn == null)
			return null;
		
		String order;
		if(gradeOrder == 0) {
			order = "DESC";
		} else {
			order = "ASC";
		}

		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM restaurant WHERE available ==" + availability + " ORDER BY gradesAverage "+order);

			while (rs.next()) {
				Restaurant restaurant = new Restaurant();
				
				restaurant  = new Restaurant();	 
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
				
				restaurants.add(restaurant);
				
				logger.info("fetching restaurant: "+restaurant.getId());
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return restaurants;
	}
	
	
	@Override
	public long add(Restaurant restaurant) {
		long id=-1;
		long lastid=-1;
		if (conn != null){

			Statement stmt;
			
			
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_sequence WHERE name ='restaurant'");			 
				if (!rs.next()) return -1; 
				lastid=rs.getInt("seq");
								
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO restaurant (name,address,telephone,idu,gradesAverage,city,minPrice,contactemail,description,maxPrice,bikeFriendly,available) VALUES('"
									+restaurant.getName()+"','"+restaurant.getAddress()+"','" + restaurant.getTelephone() + "'," 
									+ restaurant.getIdu() + "," + restaurant.getGradesAverage()+",'"+ restaurant.getCity() +"',"+ restaurant.getMinPrice() +",'" + restaurant.getContactEmail() + "','" + restaurant.getDescription() + "'," 
									+ restaurant.getMaxPrice() + "," + restaurant.getBikeFriendly() +"," + restaurant.getAvailable() + ")");
				
								
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_sequence WHERE name ='restaurant'");			 
				if (!rs.next()) return -1; 
				id=rs.getInt("seq");
				if (id<=lastid) return -1;
											
				logger.info("CREATING restaurant("+id+"): "+restaurant.getName());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return id;
	}

	@Override
	public boolean update(Restaurant restaurant) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("UPDATE restaurant SET name='"+restaurant.getName()
				+"', address='"+restaurant.getAddress()
				+"', telephone='"+restaurant.getTelephone()
				+"', idu="+restaurant.getIdu()
				+", city='"+restaurant.getCity()
				+"', minPrice="+restaurant.getMinPrice()
				+", contactemail='"+restaurant.getContactEmail()
				+"', description='"+restaurant.getDescription()
				+"', maxPrice="+restaurant.getMaxPrice()
				+", bikeFriendly="+restaurant.getBikeFriendly()
				+", available="+restaurant.getAvailable()
				+" WHERE id = "+restaurant.getId());
				logger.info("updating restaurant: "+restaurant.getId());
						
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;

	}
	
	@Override
	public boolean updateGradesAverage(long id) {
		boolean done = false;
		if (conn != null){
			
			PreparedStatement query; 
			 
			try { 
			 
				query = conn.prepareStatement("UPDATE restaurant SET gradesAverage = (SELECT ROUND(AVG(grade),1) FROM reviews WHERE idr = ?)  WHERE id = ?;"); 
				query.clearParameters(); 
			 
				query.setLong(1, id); 
				query.setLong(2, id); 
			 
				int result = query.executeUpdate(); 
			 
				if (result > 0) { 
					done = true; 
				} 
			 
			 } catch (SQLException e) { 
				e.printStackTrace(); 
			 }  
		}
		return done;
	}

	@Override
	public boolean delete(long id) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM restaurant WHERE id ="+id);
				logger.info("deleting restaurant: "+id);
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
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
