package es.unex.pi.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RestaurantWithCategories {
	
	private Restaurant restaurant;
	private List<Long> categoriesIds;
	
	public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	public List<Long> getCategoriesIds() {
		return categoriesIds;
	}
	public void setCategoriesIds(List<Long> categoriesIds) {
		this.categoriesIds = categoriesIds;
	}

}
