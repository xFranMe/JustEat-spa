package es.unex.tests.dao;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.unex.pi.model.Dish;
import es.unex.pi.dao.DishDAO;
import es.unex.pi.dao.JDBCDishDAOImpl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestDishDAO {

	static DBConn dbConn;
	static DishDAO dishDAO;
	static Connection conn;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dbConn = new DBConn();
		conn = dbConn.create();
	    dishDAO = new JDBCDishDAOImpl();
		dishDAO.setConnection(conn);
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
		Dish dish00 = dishDAO.get(0);
		assertEquals(dish00.getId(),0);
		assertEquals(dish00.getName(),"Whopper");
		
		Dish dish01 = dishDAO.get(1);
		assertEquals(dish01.getId(),1);
		assertEquals(dish01.getName(),"Arroz frito");
		
		Dish dish02 = dishDAO.get(2);
		assertEquals(dish02.getId(),2);
		assertEquals(dish02.getName(),"Careta de cerdo con aire de la dehesa");

	}
	
	
	@Test
	public void test2Add(){
		Dish dish01 = new Dish();
		dish01.setName("newDish");
		dish01.setDescription("newDescription");
		
		long value = dishDAO.add(dish01);
		
		Dish dish02 = dishDAO.get("newDish");
		assertEquals(dish01.getName(),dish02.getName());
	}
	
	@Test
	public void test3Modify(){
		Dish dish01 = dishDAO.get("newDish");
		dish01.setName("newDishUpdated");
		dishDAO.update(dish01);
		
		Dish dish02 = dishDAO.get("newDishUpdated");		
		assertEquals(dish01.getName(),dish02.getName());
	}
	
	@Test
	public void test4Delete(){
		 Dish dish01 = dishDAO.get("newDishUpdated");
		 dishDAO.delete(dish01.getId());
		 
		 Dish dish02 = dishDAO.get("newDishUpdated");
 		 assertEquals(null, dish02);
 		 
 		dishDAO.getAll();
	}

}
