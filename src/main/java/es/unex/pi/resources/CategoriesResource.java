package es.unex.pi.resources;

import java.sql.Connection;
import java.util.List;

import es.unex.pi.dao.CategoryDAO;
import es.unex.pi.dao.JDBCCategoryDAOImpl;
import es.unex.pi.model.Category;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

@Path("/categories")
public class CategoriesResource {
	
	@Context
	ServletContext sc;
	@Context
	UriInfo uriInfo;
	
	private static final String ID_REGEXP = "[0-9]+";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Category> getCategoriesJSON(@Context HttpServletRequest request) {	  
		List<Category> categories = null;
		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		CategoryDAO categoryDAO = new JDBCCategoryDAOImpl();
		categoryDAO.setConnection(conn);
	  
		// Se recuperan TODAS las categorías existentes
		categories = categoryDAO.getAll();
		
		return categories;
	}
	
	@GET
	@Path("/{categoryid: "+ID_REGEXP+"}")
	@Produces(MediaType.APPLICATION_JSON)
	public Category getCategoryJSON(@PathParam("categoryid") long categoryid,
									@Context HttpServletRequest request) {	  
		Category category = null;
		    
		// Se recupera la conexión a la BD
		Connection conn = (Connection) sc.getAttribute("dbConn");
		CategoryDAO categoryDAO = new JDBCCategoryDAOImpl();
		categoryDAO.setConnection(conn);
	  
		// Se recuperan la categoría
		category = categoryDAO.get(categoryid);
		
		return category;
	}

}