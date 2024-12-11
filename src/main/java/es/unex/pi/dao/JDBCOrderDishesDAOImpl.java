package es.unex.pi.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.unex.pi.model.OrderDishes;

public class JDBCOrderDishesDAOImpl implements OrderDishesDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCOrderDishesDAOImpl.class.getName());

	@Override
	public List<OrderDishes> getAll() {

		if (conn == null) return null;
						
		ArrayList<OrderDishes> OrderDishesList = new ArrayList<OrderDishes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrderDishes");
						
			while ( rs.next() ) {
				OrderDishes orderDishes = new OrderDishes();
				orderDishes.setIdo(rs.getInt("ido"));
				orderDishes.setIddi(rs.getInt("iddi"));
				orderDishes.setAmount(rs.getInt("amount"));
						
				OrderDishesList.add(orderDishes);
				logger.info("fetching all OrderDishes: "+orderDishes.getIdo()+" "+orderDishes.getIddi());
					
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return OrderDishesList;
	}

	@Override
	public List<OrderDishes> getAllByDish(long iddi) {
		
		if (conn == null) return null;
						
		ArrayList<OrderDishes> OrderDishesList = new ArrayList<OrderDishes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrderDishes WHERE iddi="+iddi);

			while ( rs.next() ) {
				OrderDishes orderDishes = new OrderDishes();
				orderDishes.setIdo(rs.getInt("ido"));
				orderDishes.setIddi(rs.getInt("iddi"));
				orderDishes.setAmount(rs.getInt("amount"));

				OrderDishesList.add(orderDishes);
				logger.info("fetching all OrderDishes by ido: "+orderDishes.getIdo()+"->"+orderDishes.getIddi());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return OrderDishesList;
	}
	
	@Override
	public List<OrderDishes> getAllByOrder(long ido) {
		
		if (conn == null) return null;
						
		ArrayList<OrderDishes> OrderDishesList = new ArrayList<OrderDishes>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrderDishes WHERE ido="+ido);

			while ( rs.next() ) {
				OrderDishes orderDishes = new OrderDishes();
				orderDishes.setIdo(rs.getInt("ido"));
				orderDishes.setIddi(rs.getInt("iddi"));
				orderDishes.setAmount(rs.getInt("amount"));
							
				OrderDishesList.add(orderDishes);
				logger.info("fetching all OrderDishes by iddi: "+orderDishes.getIddi()+"-> "+orderDishes.getIdo());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return OrderDishesList;
	}
	
	
	@Override
	public OrderDishes get(long ido,long iddi) {
		if (conn == null) return null;
		
		OrderDishes orderDishes = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrderDishes WHERE Ido="+ido+" AND iddi="+iddi);			 
			if (!rs.next()) return null;
			orderDishes= new OrderDishes();
			orderDishes.setIdo(rs.getInt("ido"));
			orderDishes.setIddi(rs.getInt("iddi"));
			orderDishes.setAmount(rs.getInt("amount"));

			logger.info("fetching OrderDishes by ido: "+orderDishes.getIdo()+"  and iddi: "+orderDishes.getIddi());
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return orderDishes;
	}
	
	

	@Override
	public boolean add(OrderDishes orderDishes) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO OrderDishes (ido,iddi,amount) VALUES('"+
									orderDishes.getIdo()+"','"+
									orderDishes.getIddi()+"','"+orderDishes.getAmount()+"')");
						
				logger.info("creating OrderDishes:("+orderDishes.getIdo()+" "+orderDishes.getIddi());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean update(OrderDishes dbOrderDishes, OrderDishes newOrderDishes) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				
				stmt.executeUpdate("UPDATE OrderDishes SET ido = " + newOrderDishes.getIdo() + ", iddi= "+newOrderDishes.getIddi()
				+" WHERE ido = "+dbOrderDishes.getIdo() + " AND iddi = " + dbOrderDishes.getIddi());
				
				logger.info("updating OrderDishes:("+dbOrderDishes.getIdo()+" "+dbOrderDishes.getIddi());
				
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean delete(long ido, long iddi) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM OrderDishes WHERE ido ="+ido+" AND iddi="+iddi);
				logger.info("deleting OrderDishes: "+ido+" , iddi="+iddi);
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
