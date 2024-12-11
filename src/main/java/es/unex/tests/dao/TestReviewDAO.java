package es.unex.tests.dao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.Connection;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.unex.pi.dao.ReviewsDAO;
import es.unex.pi.dao.JDBCReviewsDAOImpl;
import es.unex.pi.model.Review;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestReviewDAO {

	static DBConn dbConn;
	static ReviewsDAO reviewsDAO;
	static Connection conn;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dbConn = new DBConn();
		conn = dbConn.create();
	    reviewsDAO = new JDBCReviewsDAOImpl();
		reviewsDAO.setConnection(conn);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		dbConn.destroy(conn);
	}

	@Before
	public void setUpBeforeMethod() throws Exception {
	
	}

	@Test
	public void test1BaseData() {
		
		List<Review> reviewsList = reviewsDAO.getAll();
		
		Review review = reviewsDAO.get(0,2);
		
		assertEquals(review.getIdr(),0);
		assertEquals(review.getIdu(),2);
		
		assertEquals(reviewsList.get(0).getIdr(),review.getIdr());			
			
	}
	
	@Test
	public void test2BaseDataByUser() {
		
		List<Review> reviewsList = reviewsDAO.getAllByUser(4);
		for(Review reviews: reviewsList)			
			assertEquals(reviews.getIdu(),4);			
	}
	
	@Test
	public void test3BaseDataByRestaurant() {
		
		List<Review> reviewsList = reviewsDAO.getAllByRestaurant(0);
		for(Review reviews: reviewsList)			
			assertEquals(reviews.getIdr(),0);			
	}
	
	@Test
	public void test4Add(){
		Review review01 = new Review();
		review01.setIdr(4);
		review01.setIdu(1);
		review01.setReview("NewReviewComment");
		review01.setGrade(5);
		
		reviewsDAO.add(review01);
		
		Review review02 = reviewsDAO.get(4,1);
		
		assertEquals(4,review02.getIdr());
		assertEquals(1,review02.getIdu());
				
	}
	
	
	@Test
	public void test5Modify(){
		
		Review review01 = reviewsDAO.get(3,0);
		String oldReview = review01.getReview();
		int oldGrade = review01.getGrade();
		
		review01.setReview("NewReview");
		review01.setGrade(5);
		reviewsDAO.update(review01);
		
		Review review02 = reviewsDAO.get(3,0);
		assertEquals("NewReview",review02.getReview());
		assertEquals(5,review02.getGrade());

		review01.setReview(oldReview);
		review01.setGrade(oldGrade);		
		reviewsDAO.update(review01);

		review02 = reviewsDAO.get(3,0);
		assertEquals(oldReview,review02.getReview());
		assertEquals(oldGrade,review02.getGrade());

	
	}
	
	@Test
	public void test6Delete(){
		 
		reviewsDAO.delete(4,1);
		List<Review> reviewsList = reviewsDAO.getAll();
		 
		 Review reviews01 = new Review();
		 reviews01.setIdr(3);
		 reviews01.setIdu(1);
		 		 
		for(Review reviews: reviewsList) {
				assertNotEquals(reviews,reviews01);
		}
		 
	}

}
