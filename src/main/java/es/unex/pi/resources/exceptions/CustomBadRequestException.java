package es.unex.pi.resources.exceptions;

import java.util.Map;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class CustomBadRequestException extends WebApplicationException {

	private static final long serialVersionUID = -402752772426499120L;

	public CustomBadRequestException() {
		super(Response
				  .status(Response.Status.BAD_REQUEST)
				  .build());
	}

	public CustomBadRequestException(String message) {
		super(Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"status\" : \"404\", \"userMessage\" : \""+message+"\"}")
				.type("application/json")
				.build());
	}
	
	public CustomBadRequestException(Map<String, String> messages) {
		super(Response
				.status(Response.Status.BAD_REQUEST)
				.entity(messages)
				.type("application/json")
				.build());
	}
}
