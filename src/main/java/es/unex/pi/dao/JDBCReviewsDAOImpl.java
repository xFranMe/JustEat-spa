package es.unex.pi.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import es.unex.pi.model.Review;

public class JDBCReviewsDAOImpl implements ReviewsDAO {

	private Connection conn;
	private static final Logger logger = Logger.getLogger(JDBCReviewsDAOImpl.class.getName());

	@Override
	public List<Review> getAll() {

		if (conn == null) return null;
						
		ArrayList<Review> reviewList = new ArrayList<Review>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Reviews");
						
			while ( rs.next() ) {
				Review review = new Review();
				review.setIdr(rs.getInt("idr"));
				review.setIdu(rs.getInt("idu"));
				review.setReview(rs.getString("review"));
				review.setGrade(rs.getInt("grade"));
						
				reviewList.add(review);
				logger.info("fetching all Review: "+review.getIdr()+" "+review.getIdu());
					
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reviewList;
	}

	@Override
	public List<Review> getAllByUser(long idu) {
		
		if (conn == null) return null;
						
		ArrayList<Review> reviewList = new ArrayList<Review>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Reviews WHERE idu="+idu);

			while ( rs.next() ) {
				Review review = new Review();
				review.setIdr(rs.getInt("idr"));
				review.setIdu(rs.getInt("idu"));
				review.setReview(rs.getString("review"));
				review.setGrade(rs.getInt("grade"));

				reviewList.add(review);
				logger.info("fetching all Review by idr: "+review.getIdr()+"->"+review.getIdu());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reviewList;
	}
	
	@Override
	public List<Review> getAllByRestaurant(long idr) {
		
		if (conn == null) return null;
						
		ArrayList<Review> reviewList = new ArrayList<Review>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Reviews WHERE Idr="+idr);

			while ( rs.next() ) {
				Review review = new Review();
				review.setIdr(rs.getInt("idr"));
				review.setIdu(rs.getInt("idu"));
				review.setReview(rs.getString("review"));
				review.setGrade(rs.getInt("grade"));
							
				reviewList.add(review);
				logger.info("fetching all Review by idu: "+review.getIdu()+"-> "+review.getIdr());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reviewList;
	}
	
	
	@Override
	public Review get(long idr,long idu) {
		if (conn == null) return null;
		
		Review review = null;		
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Reviews WHERE Idr="+idr+" AND idu="+idu);			 
			if (!rs.next()) return null;
			review= new Review();
			review.setIdr(rs.getInt("idr"));
			review.setIdu(rs.getInt("idu"));
			review.setReview(rs.getString("review"));
			review.setGrade(rs.getInt("grade"));

			logger.info("fetching Review by idr: "+review.getIdr()+"  and idu: "+review.getIdu());
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return review;
	}
	
	@Override
	public Map<Long, String> getUsernamesByRestaurant(long idr) {
		
		if (conn == null) return null;
						
		Map<Long, String> users = new HashMap<>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Reviews JOIN Users ON Reviews.idu = Users.id WHERE Idr="+idr);

			while (rs.next() ) {
				users.put(rs.getLong("idu"), rs.getString("name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}

	@Override
	public boolean add(Review review) {
		boolean done = false;
		if (conn != null){
			
			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO Reviews (idr,idu,review,grade) VALUES("+
									review.getIdr()+","+
									review.getIdu()+",'"+
									review.getReview()+"',"+
									review.getGrade()+")");
						
				logger.info("creating Review:("+review.getIdr()+" "+review.getIdu());
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean update(Review review) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				String consulta = "UPDATE Reviews SET review = '" + review.getReview() + "', grade = " + review.getGrade() 
				+" WHERE idr = "+review.getIdr() + " AND idu = " + review.getIdu();
				System.out.println(consulta);

				stmt.executeUpdate(consulta);
				
				logger.info("updating Review:("+review.getIdr()+" "+review.getIdu());
				
				done= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return done;
	}

	@Override
	public boolean delete(long idr, long idu) {
		boolean done = false;
		if (conn != null){

			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate("DELETE FROM Reviews WHERE idr ="+idr+" AND idu="+idu);
				logger.info("deleting Review: "+idr+" , idu="+idu);
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
