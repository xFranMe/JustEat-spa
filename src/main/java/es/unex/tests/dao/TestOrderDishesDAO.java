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

import es.unex.pi.dao.OrderDishesDAO;
import es.unex.pi.dao.JDBCOrderDishesDAOImpl;
import es.unex.pi.model.OrderDishes;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestOrderDishesDAO {

	static DBConn dbConn;
	static OrderDishesDAO OrderDishesDAO;
	static Connection conn;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dbConn = new DBConn();
		conn = dbConn.create();
	    OrderDishesDAO = new JDBCOrderDishesDAOImpl();
		OrderDishesDAO.setConnection(conn);
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
		
		List<OrderDishes> OrderDishesList = OrderDishesDAO.getAll();
		
		OrderDishes OrderDishes = OrderDishesDAO.get(0,0);
		
		assertEquals(OrderDishes.getIdo(),0);
		assertEquals(OrderDishes.getIddi(),0);
		
		assertEquals(OrderDishesList.get(0).getIdo(),OrderDishes.getIdo());			
			
	}
	
	@Test
	public void test2BaseDataByDish() {
		
		List<OrderDishes> OrderDishesList = OrderDishesDAO.getAllByDish(2);
		for(OrderDishes OrderDishes: OrderDishesList)			
			assertEquals(OrderDishes.getIddi(),2);			
	}
	
	@Test
	public void test3BaseDataByOrder() {
		
		List<OrderDishes> OrderDishesList = OrderDishesDAO.getAllByOrder(1);
		for(OrderDishes OrderDishes: OrderDishesList)			
			assertEquals(OrderDishes.getIdo(),1);			
	}
	
	@Test
	public void test4Add(){
		OrderDishes orderDishes01 = new OrderDishes();
		orderDishes01.setIdo(2);
		orderDishes01.setIddi(1);
		OrderDishesDAO.add(orderDishes01);
		
		OrderDishes orderDishes02 = OrderDishesDAO.get(2,1);
		
		assertEquals(2,orderDishes02.getIdo());
		assertEquals(1,orderDishes02.getIddi());
				
	}
	
	
	@Test
	public void test5Modify(){
		
		OrderDishes orderDishes01 = OrderDishesDAO.get(2,1);
		OrderDishes orderDishes02 = OrderDishesDAO.get(2,1);
		orderDishes02.setIddi(0);
		OrderDishesDAO.update(orderDishes01,orderDishes02);
		
		OrderDishes orderDishes03 = OrderDishesDAO.get(2,0);
		assertEquals(2,orderDishes03.getIdo());
		assertEquals(0,orderDishes03.getIddi());
	}
	
	@Test
	public void test6Delete(){
		 
		OrderDishesDAO.delete(2,0);
		List<OrderDishes> OrderDishesList = OrderDishesDAO.getAll();
		 
		 OrderDishes OrderDishes01 = new OrderDishes();
		 OrderDishes01.setIdo(2);
		 OrderDishes01.setIddi(0);
		 		 
		for(OrderDishes OrderDishes: OrderDishesList) {
				assertNotEquals(OrderDishes,OrderDishes01);
		}
		 
	}

}
