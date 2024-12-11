package es.unex.pi.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.unex.pi.model.Order;


public class JDBCOrderDAOImpl implements OrderDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCOrderDAOImpl.class.getName());
	
	@Override
	public Order get(long id) {
		if (conn == null) return null;
		
		Order order = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM orders WHERE id ="+id);			 
			if (!rs.next()) return null; 
			order  = new Order();	 
			order.setId(rs.getInt("id"));
			order.setIdu(rs.getInt("idu"));
			order.setTotalPrice(rs.getFloat("totalPrice"));
			
			logger.info("fetching Order by id: "+id+" -> "+order.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}
	
	
	@Override
	public Order get(long idu,float totalPrice){
		if (conn == null) return null;
		
		Order order = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM orders WHERE idu ="+idu + " AND totalPrice =" + totalPrice);			 
			if (!rs.next()) return null; 
			order  = new Order();	 
			order.setId(rs.getInt("id"));
			order.setIdu(rs.getInt("idu"));
			order.setTotalPrice(rs.getFloat("totalPrice"));
			
			logger.info("fetching Order id: "+order.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}
	
	@Override
	public Order getLastOrderFromUser(long idu){
		if (conn == null) return null;
		
		Order order = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM orders WHERE idu ="+idu+" ORDER BY id DESC LIMIT 1");			 
			if (!rs.next()) return null; 
			order  = new Order();	 
			order.setId(rs.getInt("id"));
			order.setIdu(rs.getInt("idu"));
			order.setTotalPrice(rs.getFloat("totalPrice"));
			
			logger.info("fetching Order id: "+order.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}

	
	public List<Order> getAll() {

		if (conn == null) return null;
		
		ArrayList<Order> orderes = new ArrayList<Order>();
		try {
			Statement stmt;
			ResultSet rs;
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Orders");
			while ( rs.next() ) {
				Order order = new Order();
				order.setId(rs.getInt("id"));
				order.setIdu(rs.getInt("idu"));
				order.setTotalPrice(rs.getFloat("totalPrice"));
				orderes.add(order);
				logger.info("fetching Orders: "+order.getId());
								
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return orderes;
	}
	
	public List<Order> getAllFromUser(long idu) {

		if (conn == null) return null;
		
		ArrayList<Order> orderes = new ArrayList<Order>();
		try {
			Statement stmt;
			ResultSet rs;
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Orders WHERE idu ="+idu);
			while ( rs.next() ) {
				Order order = new Order();
				order.setId(rs.getInt("id"));
				order.setIdu(rs.getInt("idu"));
				order.setTotalPrice(rs.getFloat("totalPrice"));
				orderes.add(order);
				logger.info("fetching Orders: "+order.getId());	
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return orderes;
	}

	@Override
	public long add(Order order) {
		long id=-1;
		long lastid=-1;
		if (conn != null){

			Statement stmt;
			
			
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_sequence WHERE name ='orders'");			 
				if (!rs.next()) return -1; 
				lastid=rs.getInt("seq");
								
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO Orders (idu,totalPrice) VALUES(" + order.getIdu() +", " + order.getTotalPrice()+ ")");
				
								
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_sequence WHERE name ='orders'");			 
				if (!rs.next()) return -1; 
				id=rs.getInt("seq");
				if (id<=lastid) return -1;
											
				logger.info("CREATING Order("+id+"): ");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return id;
	}

	@Override
	public boolean update(Order order) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				
				stmt.executeUpdate("UPDATE Orders SET id = " + order.getId() +", idu= " + order.getIdu()+ ", totalPrice= " + order.getTotalPrice() 
				+ " WHERE id = "+order.getId());
				
				logger.info("updating Order: "+order.getId());
				
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
				stmt.executeUpdate("DELETE FROM Orders WHERE id = " + id);
				
				logger.info("deleting Order: "+id);
				
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
