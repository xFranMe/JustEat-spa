angular.module('JustEatApp')
.factory('restaurantsFactory',['$http', function($http){
	
	// The base URL needed for executing the web services
	var url = 'https://localhost:8443/JustEat_Act03/rest/restaurants/';
	var restaurantsInterface = {
    	getUserRestaurants : function(){
			url = url;
			return $http.get(url)
				.then(function(response){
					return response.data;
				});   
    	},
    	getRestaurant : function(id){
			var urlid = url + id;
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});   
    	},
    	deleteRestaurant : function(id){
			var urlid = url + id;
			return $http.delete(urlid)
				.then(function(response){
					return response.status;
				});
        },
        getRestaurantCategories : function(id){
			var urlid = url + id + '/categories';
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});   
    	},
        getRestaurantDishes : function(id){
			var urlid = url + id + '/dishes';
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});
        },
        getRestaurantReviews : function(id){
			var urlid = url + id + '/reviews';
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});
        },
        postRestaurant : function(restaurant, categoriesList){
			url = url;
			var restaurantWithCategories = {};
			restaurantWithCategories.restaurant = restaurant;
			restaurantWithCategories.categoriesIds = categoriesList;
			return $http.post(url, restaurantWithCategories)
				.then(function(response){
					return response;
				});
    	},
    	putRestaurant : function(restaurant, categoriesList){
			var urlid = url + restaurant.id;
			var restaurantWithCategories = {};
			restaurantWithCategories.restaurant = restaurant;
			restaurantWithCategories.categoriesIds = categoriesList;
			return $http.put(urlid, restaurantWithCategories)
				.then(function(response){
					return response;
				});
    	},
    	getRestaurantsByCategory : function(id){
			var urlid = url + 'category/'+id;
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});
        },
        getRestaurantsByFilter : function(query){
			var urlquery = url+'search'+query;
			return $http.get(urlquery)
				.then(function(response){
					console.log(response.data);
					return response.data;
				});
        },
        /*------------------------------------------------------------
		---------------------AMPLIACIÓN 4-------------------------------
		------------------------------------------------------------*/
        getRelatedRestaurantsByCategory : function(id){
			var urlid = url + id + '/relatedByCategory';
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});
        },
        getRelatedRestaurantsByCity : function(id){
			var urlid = url + id + '/relatedByCity';
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});
        },
        getRelatedRestaurantsByPrice : function(id){
			var urlid = url + id + '/relatedByPrice';
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});
        },
        getRelatedRestaurantsByGrade : function(id){
			var urlid = url + id + '/relatedByGrade';
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});
        }
    }
    return restaurantsInterface;
}])