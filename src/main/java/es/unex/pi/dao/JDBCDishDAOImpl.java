package es.unex.pi.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.unex.pi.model.Dish;


public class JDBCDishDAOImpl implements DishDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCDishDAOImpl.class.getName());
	
	@Override
	public Dish get(long id) {
		if (conn == null) return null;
		
		Dish dish = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM dishes WHERE id ="+id);			 
			if (!rs.next()) return null; 
			dish = new Dish();	 
			dish.setId(rs.getInt("id"));
			dish.setIdr(rs.getInt("idr"));
			dish.setName(rs.getString("name"));
			dish.setPrice(rs.getFloat("price"));
			dish.setDescription(rs.getString("description"));
			
			logger.info("fetching Dish by id: "+id+" -> "+dish.getId()+" "+dish.getName()+" "+dish.getDescription());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dish;
	}
	
	
	@Override
	public Dish get(String name) {
		if (conn == null) return null;
		
		Dish dish = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Dishes WHERE name = '"+name+"'");			 
			if (!rs.next()) return null; 
			dish  = new Dish();	 
			dish.setId(rs.getInt("id"));
			dish.setName(rs.getString("name"));
			dish.setPrice(rs.getFloat("price"));
			dish.setDescription(rs.getString("description"));
			dish.setIdr(rs.getInt("idr"));
			
			logger.info("fetching Dish by name: "+dish.getId()+" "+dish.getName()+" "+dish.getDescription());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dish;
	}
	
	
	
	public List<Dish> getAll() {

		if (conn == null) return null;
		
		ArrayList<Dish> dishes = new ArrayList<Dish>();
		try {
			Statement stmt;
			ResultSet rs;
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Dishes");
			while ( rs.next() ) {
				Dish dish = new Dish();
				dish.setId(rs.getInt("id"));
				dish.setName(rs.getString("name"));
				dish.setPrice(rs.getFloat("price"));
				dish.setDescription(rs.getString("description"));
				dish.setIdr(rs.getInt("idr"));
				
				dishes.add(dish);
				logger.info("fetching Dishes: "+dish.getId()+" "+dish.getName()+" "+dish.getDescription());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dishes;
	}
	
	public List<Dish> getAllBySearchName(String search) {
		search = search.toUpperCase();
		if (conn == null)
			return null;

		ArrayList<Dish> dishes = new ArrayList<Dish>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Dishes WHERE UPPER(name) LIKE '%" + search + "%'");

			while (rs.next()) {
				Dish dish = new Dish();
				
				dish.setId(rs.getInt("id"));
				dish.setName(rs.getString("name"));
				dish.setPrice(rs.getFloat("price"));
				dish.setDescription(rs.getString("description"));
				dish.setIdr(rs.getInt("idr"));
				
				dishes.add(dish);
				
				logger.info("fetching dishs by text in the name: "+search+": "+dish.getId()+" "+dish.getName()+" "+dish.getDescription());
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dishes;
	}
	
	public List<Dish> getByRestaurantId(long idr) {
		if (conn == null)
			return null;

		ArrayList<Dish> dishes = new ArrayList<Dish>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Dishes WHERE idr='"+idr+"'");

			while (rs.next()) {
				Dish dish = new Dish();
				
				dish.setId(rs.getInt("id"));
				dish.setName(rs.getString("name"));
				dish.setPrice(rs.getFloat("price"));
				dish.setDescription(rs.getString("description"));
				dish.setIdr(rs.getInt("idr"));
				
				dishes.add(dish);
				
				logger.info("fetching dishs by restaurant id: "+idr+": "+dish.getId()+" "+dish.getName()+" "+dish.getDescription());
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dishes;
	}
	
	@Override
	public Dish getByNameAndRestaurantId(String name, long idr) {
		if (conn == null) return null;
		
		Dish dish = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Dishes WHERE name = '"+name+"' AND idr = "+idr);			 
			if (!rs.next()) return null; 
			dish  = new Dish();	 
			dish.setId(rs.getInt("id"));
			dish.setName(rs.getString("name"));
			dish.setPrice(rs.getFloat("price"));
			dish.setDescription(rs.getString("description"));
			dish.setIdr(rs.getInt("idr"));
			
			logger.info("fetching Dish by name: "+dish.getId()+" "+dish.getName()+" "+dish.getDescription());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dish;
	}

	@Override
	public long add(Dish dish) {
		long id=-1;
		long lastid=-1;
		if (conn != null){

			Statement stmt;
			
			
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_sequence WHERE name ='dishes'");			 
				if (!rs.next()) return -1; 
				lastid=rs.getInt("seq");
								
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO Dishes (name,price,description,idr) VALUES('"
									+dish.getName()+"', " + dish.getPrice() +", '" + dish.getDescription() +"', " + dish.getIdr()+ ")");
				
								
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_sequence WHERE name ='dishes'");			 
				if (!rs.next()) return -1; 
				id=rs.getInt("seq");
				if (id<=lastid) return -1;
											
				logger.info("CREATING Dish("+id+"): "+dish.getName());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return id;
	}

	@Override
	public boolean update(Dish dish) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				
				stmt.executeUpdate("UPDATE Dishes SET name='"+dish.getName()+"', price = " + dish.getPrice() +", description = '" + dish.getDescription() +"', idr= " + dish.getIdr()+ " WHERE id = "+dish.getId());
				
				logger.info("updating Dish: "+dish.getId()+" "+dish.getName()+" "+dish.getDescription());
				
				done= true;
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
				stmt.executeUpdate("DELETE FROM Dishes WHERE id ="+id);
				
				logger.info("deleting Dish: "+id);
				
				done= true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	
}
