package es.unex.pi.model;

import java.util.Map;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Restaurant {

	private long id;
	private String name;
	private String address;
	private String telephone;
	private String city;
	private float minPrice;
	private float maxPrice;
	private String contactEmail;
	private float gradesAverage;
	private int bikeFriendly; // 1 = true, 0 = false
	private int available; // 1 = true, 0 = false
	private long idu;
	private String description;
	
	public Restaurant () {
		this.id = -1;
		this.gradesAverage = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public float getMinPrice() {
		return minPrice;
	}
	
	public void setMinPrice(float minPrice) {
		this.minPrice = minPrice;
	}
	
	public float getMaxPrice() {
		return maxPrice;
	}
	
	public void setMaxPrice(float maxPrice) {
		this.maxPrice = maxPrice;
	}
	
	public float getGradesAverage() {
		return gradesAverage;
	}
	
	public void setGradesAverage(float gradesAverage) {
		this.gradesAverage = gradesAverage;
	}
	
	public int getBikeFriendly() {
		return bikeFriendly;
	}
	
	public void setBikeFriendly(int bikeFriendly) {
		this.bikeFriendly = bikeFriendly;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	
	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getContactEmail() {
		return contactEmail;
	}
	
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	
	public int getAvailable() {
		return available;
	}
	
	public void setAvailable(int available) {
		this.available = available;
	}
	
	public long getIdu() {
		return idu;
	}
	
	public void setIdu(long idu) {
		this.idu = idu;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	private boolean validateCity(Map<String, String> messages){
		if(city.trim().isEmpty() || city == null) {
			messages.put("city_error", "Debe introducirse un valor para la ciudad");
			return false;
		}  
		
		if(!city.trim().matches("^([a-zA-Z\\u0080-\\u024F]+(?:(\\. )|-| |'))*[a-zA-Z\\u0080-\\u024F]*$")) {
			messages.put("city_error", "Localidad no válida: " + city.trim());
			return false;
		}
		
		return true;
	}

	private boolean validateContactEmail(Map<String, String> messages){
		if(contactEmail.trim().isEmpty() || contactEmail == null) {
			messages.put("email_error", "Debe introducirse un valor para el email");
			return false;
		}  
		
		if(!contactEmail.trim().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
			messages.put("email_error", "Email no válido: " + contactEmail.trim());
			return false;
		}
		
		return true;
	}
	
	private boolean validateMinPrice(Map<String, String> messages){
		if(minPrice < 0) {
			messages.put("minPrice_error", "El precio mínimo no puede ser inferior a 0.");
			return false;
		}  
		
		return true;
	}
	
	private boolean validateMaxPrice(Map<String, String> messages){
		if(maxPrice < minPrice) {
			messages.put("maxPrice_error", "El precio máximo no puede ser inferior al precio mínimo.");
			return false;
		}  
		
		return true;
	}
	
	private boolean validateGradesAverage(Map<String, String> messages){
		if(gradesAverage < 0) {
			messages.put("gradesAverage_error", "La valoración media no puede ser inferior a 0.");
			return false;
		}
		
		if(gradesAverage > 5) {
			messages.put("gradesAverage_error", "La valoración media no puede ser superior a 5.");
			return false;
		}
		
		return true;
	}
	
	private boolean validateTelephone(Map<String, String> messages) {
		if(telephone.trim().isEmpty() || telephone == null) {
			messages.put("telephone_error", "Debe introducirse un valor para el teléfono.");
			return false;
		}  
		
		if(!telephone.trim().matches("^\\d{9}$")) {
			messages.put("telephone_error", "El teléfono debe estar compuesto por 9 dígitos");
			return false;
		}
		
		return true;
	}
	
//	private boolean validateCategories(Map<String, String> messages, Map<Long, Long> restaurantCategories) {
//		if(restaurantCategories.isEmpty()) {
//			messages.put("category_error", "Se debe seleccionar una categoría como mínimo.");
//			return false;
//		} else {
//			return true;
//		}
//	}
	
	public boolean validate(Map<String, String> messages) {
		return validateCity(messages) & validateContactEmail(messages) & validateGradesAverage(messages) & validateMinPrice(messages) & validateMaxPrice(messages) & validateTelephone(messages);
	}
}
