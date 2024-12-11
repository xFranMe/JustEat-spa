package es.unex.pi.model;

import java.util.Map;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
	
	private long id;
	private String name;
	private String surname;
	private String email;
	private String password;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	private boolean validateName(Map<String, String> messages){
		if(name.trim().isEmpty() || name == null) {
			messages.put("name_error", "Debe introducirse un valor para el nombre");
			return false;
		}  
		
		if(!name.trim().matches("[A-Za-záéíóúñÁÉÍÓÚ]{2,}([\\s][A-Za-záéíóúñÁÉÍÓÚ]{2,})*")) {
			messages.put("name_error", "Nombre no válido: " + name.trim());
			return false;
		}
		
		return true;
	}
	
	private boolean validateSurname(Map<String, String> messages){
		if(surname.trim().isEmpty() || surname == null) {
			messages.put("surname_error", "Debe introducirse un valor para el apellido");
			return false;
		}  
		
		if(!surname.trim().matches("[A-Za-záéíóúñÁÉÍÓÚ]{2,}([\\s][A-Za-záéíóúñÁÉÍÓÚ]{2,})*")) {
			messages.put("surname_error", "Apellido no válido: " + surname.trim());
			return false;
		}
		
		return true;
	}
	
	private boolean validatePassword(Map<String, String> messages){
		if(password.trim().isEmpty() || password == null) {
			messages.put("password_error", "Debe introducirse un valor para la contraseña");
			return false;
		}  
		
		if(!password.trim().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
			messages.put("password_error", "Contraseña no válida");
			return false;
		}
		
		return true;
	}
	
	private boolean validateEmail(Map<String, String> messages){
		if(email.trim().isEmpty() || email == null) {
			messages.put("email_error", "Debe introducirse un valor para el email");
			return false;
		}  
		
		if(!email.trim().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
			messages.put("email_error", "Email no válido: " + email.trim());
			return false;
		}
		
		return true;
	}
	
	public boolean validate(Map<String, String> messages) {
		// Se busca realizar todas las comprobaciones para mostrar al usuario todos los errores de golpe
		return validateEmail(messages) & validateName(messages) & validateSurname(messages) & validatePassword(messages);
	}
}
