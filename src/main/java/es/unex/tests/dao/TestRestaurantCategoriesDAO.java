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

import es.unex.pi.dao.RestaurantCategoriesDAO;
import es.unex.pi.dao.JDBCRestaurantCategoriesDAOImpl;
import es.unex.pi.model.RestaurantCategories;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestRestaurantCategoriesDAO {

	static DBConn dbConn;
	static RestaurantCategoriesDAO RestaurantsCategoriesDAO;
	static Connection conn;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dbConn = new DBConn();
		conn = dbConn.create();
	    RestaurantsCategoriesDAO = new JDBCRestaurantCategoriesDAOImpl();
		RestaurantsCategoriesDAO.setConnection(conn);
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
		
		List<RestaurantCategories> RestaurantsCategoriesList = RestaurantsCategoriesDAO.getAll();
		
		RestaurantCategories RestaurantsCategories = RestaurantsCategoriesDAO.get(0,4);
		
		assertEquals(RestaurantsCategories.getIdr(),0);
		assertEquals(RestaurantsCategories.getIdct(),4);
		
		assertEquals(RestaurantsCategoriesList.get(0).getIdr(),RestaurantsCategories.getIdr());			
			
	}
	
	@Test
	public void test2BaseDataByCategory() {
		
		List<RestaurantCategories> RestaurantsCategoriesList = RestaurantsCategoriesDAO.getAllByCategory(4);
		for(RestaurantCategories RestaurantsCategories: RestaurantsCategoriesList)			
			assertEquals(RestaurantsCategories.getIdct(),4);			
	}
	
	@Test
	public void test3BaseDataByRestaurant() {
		
		List<RestaurantCategories> RestaurantsCategoriesList = RestaurantsCategoriesDAO.getAllByRestaurant(0);
		for(RestaurantCategories RestaurantsCategories: RestaurantsCategoriesList)			
			assertEquals(RestaurantsCategories.getIdr(),0);			
	}
	
	@Test
	public void test4Add(){
		RestaurantCategories restaurantCategories01 = new RestaurantCategories();
		restaurantCategories01.setIdr(3);
		restaurantCategories01.setIdct(0);
		RestaurantsCategoriesDAO.add(restaurantCategories01);
		
		RestaurantCategories restaurantCategories02 = RestaurantsCategoriesDAO.get(3,0);
		
		assertEquals(3,restaurantCategories02.getIdr());
		assertEquals(0,restaurantCategories02.getIdct());
				
	}
	
	
	@Test
	public void test5Modify(){
		
		RestaurantCategories restaurantCategories01 = RestaurantsCategoriesDAO.get(3,0);
		RestaurantCategories restaurantCategories02 = RestaurantsCategoriesDAO.get(3,0);
		restaurantCategories02.setIdct(1);
		RestaurantsCategoriesDAO.update(restaurantCategories01,restaurantCategories02);
		
		RestaurantCategories restaurantCategories03 = RestaurantsCategoriesDAO.get(3,1);
		assertEquals(3,restaurantCategories03.getIdr());
		assertEquals(1,restaurantCategories03.getIdct());
	}
	
	@Test
	public void test6Delete(){
		 
		RestaurantsCategoriesDAO.delete(3,1);
		List<RestaurantCategories> RestaurantsCategoriesList = RestaurantsCategoriesDAO.getAll();
		 
		 RestaurantCategories RestaurantsCategories01 = new RestaurantCategories();
		 RestaurantsCategories01.setIdr(3);
		 RestaurantsCategories01.setIdct(1);
		 		 
		for(RestaurantCategories RestaurantsCategories: RestaurantsCategoriesList) {
				assertNotEquals(RestaurantsCategories,RestaurantsCategories01);
		}
		 
	}

}
