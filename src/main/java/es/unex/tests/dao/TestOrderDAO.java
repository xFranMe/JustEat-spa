package es.unex.tests.dao;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.unex.pi.model.Order;
import es.unex.pi.dao.OrderDAO;
import es.unex.pi.dao.JDBCOrderDAOImpl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestOrderDAO {

	static DBConn dbConn;
	static OrderDAO orderDAO;
	static Connection conn;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dbConn = new DBConn();
		conn = dbConn.create();
	    orderDAO = new JDBCOrderDAOImpl();
		orderDAO.setConnection(conn);
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
		Order order00 = orderDAO.get(0);
		assertEquals(order00.getId(),0);
		assertEquals(order00.getTotalPrice(),100);
		
		Order order01 = orderDAO.get(1);
		assertEquals(order01.getId(),1);
		assertEquals(order01.getTotalPrice(),14);
		
		Order order02 = orderDAO.get(2);
		assertEquals(order02.getId(),2);
		assertEquals(order02.getTotalPrice(),5);

	}
	
	
	@Test
	public void test2Add(){
		Order order01 = new Order();
		order01.setTotalPrice(1000);
		order01.setIdu(1);
		
		
		long value = orderDAO.add(order01);
		
		Order order02 = orderDAO.get(order01.getIdu(),order01.getTotalPrice());
		assertEquals(order01.getIdu(),order02.getIdu());
		assertEquals(order01.getTotalPrice(),order02.getTotalPrice());
		
	}
	
	@Test
	public void test3Modify(){
		Order order01 = orderDAO.get(1);
		float previousPrice = order01.getTotalPrice();
		
		order01.setTotalPrice(2000);
		orderDAO.update(order01);
		
		Order order02 = orderDAO.get(1);		
		assertEquals(order01.getTotalPrice(),order02.getTotalPrice());

		order01.setTotalPrice(previousPrice);
		orderDAO.update(order01);
		
		order02 = orderDAO.get(1);		
		assertEquals(order01.getTotalPrice(),order02.getTotalPrice());
	
	}

	
	@Test
	public void test4Delete(){
		 Order order01 = orderDAO.get(1,1000);
		 orderDAO.delete(order01.getId());
		 
		 Order order02 = orderDAO.get(1,1000);
 		 assertEquals(null, order02);
 		 
 		orderDAO.getAll();
	}

}
