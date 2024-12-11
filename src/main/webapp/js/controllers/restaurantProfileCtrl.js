angular.module('JustEatApp')
.controller('restaurantProfileCtrl', ['restaurantsFactory', 'usersFactory', 'ordersFactory','$rootScope', '$routeParams','$location',
			function(restaurantsFactory, usersFactory, ordersFactory, $rootScope, $routeParams, $location){
    var restaurantProfileViewModel = this;
    restaurantProfileViewModel.restaurant={};
    restaurantProfileViewModel.currentUser={};
    restaurantProfileViewModel.categories=[];
    restaurantProfileViewModel.dishes=[];
    restaurantProfileViewModel.reviews=[];
    restaurantProfileViewModel.reviewed = false;
    restaurantProfileViewModel.dishesAmounts = [];
    restaurantProfileViewModel.defaultAmount = 1;
    //------AMPLIACIÓN 4-------------
    restaurantProfileViewModel.relatedRestaurantsByCategory=[];
    restaurantProfileViewModel.relatedRestaurantsByCity=[];
    restaurantProfileViewModel.relatedRestaurantsByPrice=[];
    restaurantProfileViewModel.relatedRestaurantsByGrade=[];
    
    restaurantProfileViewModel.functions = {
		readRestaurant : function(id) {
			restaurantsFactory.getRestaurant(id)
				.then(function(response){
					restaurantProfileViewModel.restaurant = response
					console.log("Reading restaurant with id: ", id," Response: ", response)
					// Métodos a llamar si el restaurante ha sido encontrado
					restaurantProfileViewModel.functions.readCurrentUser();
					restaurantProfileViewModel.functions.readRestaurantCategories($routeParams.ID);
				   	restaurantProfileViewModel.functions.readRestaurantDishes($routeParams.ID);
				   	restaurantProfileViewModel.functions.readRestaurantReviews($routeParams.ID);
					//------------AMPLIACIÓN----------------------
				   	restaurantProfileViewModel.functions.readRelatedRestaurantsByCategory(restaurantProfileViewModel.restaurant.id);
				   	restaurantProfileViewModel.functions.readRelatedRestaurantsByCity(restaurantProfileViewModel.restaurant.id);
				   	restaurantProfileViewModel.functions.readRelatedRestaurantsByPrice(restaurantProfileViewModel.restaurant.id);
				   	restaurantProfileViewModel.functions.readRelatedRestaurantsByGrade(restaurantProfileViewModel.restaurant.id);
				}, function(response){
					console.log("Error reading restaurant info");
					$location.path('/');
				})
		},
		readCurrentUser : function() {
			usersFactory.getUser()
				.then(function(response){
					restaurantProfileViewModel.currentUser = response
					console.log("Reading current user. Response: ", response)
				}, function(response){
					console.log("Error reading current user");
					$location.path('/');
				})
		},
		readRestaurantCategories : function(id) {
			restaurantsFactory.getRestaurantCategories(id)
				.then(function(response){
					restaurantProfileViewModel.categories = response
					console.log("Reading restaurant categories (id: ", id,") Response: ", response)
				}, function(response){
					console.log("Error reading restaurant categories info");
					$location.path('/');
				})
		},
		readRestaurantDishes : function(id) {
			restaurantsFactory.getRestaurantDishes(id)
				.then(function(response){
					restaurantProfileViewModel.dishes = response
					console.log("Reading restaurant dishes (id: ", id,") Response: ", response)
				}, function(response){
					console.log("Error reading restaurant dishes info");
					$location.path('/');
				})
		},
		readRestaurantReviews : function(id) {
			restaurantsFactory.getRestaurantReviews(id)
				.then(function(response){
					restaurantProfileViewModel.reviews = response
					console.log("Reading restaurant reviews (id: ", id,") Response: ", response)
					restaurantProfileViewModel.functions.userInReviews();
				}, function(response){
					console.log("Error reading restaurant reviews info");
					$location.path('/');
				})
		},
		userInReviews : function() {
			var elto = restaurantProfileViewModel.reviews.find(review => review.second.id === restaurantProfileViewModel.currentUser.id);
			if(elto !== undefined){
				restaurantProfileViewModel.reviewed = true;
			} else {
				restaurantProfileViewModel.reviewed = false;
			}
		},
		addDishToOrder : function(id, amount) {
			if(amount === undefined){
				amount = 1;
			}
			ordersFactory.postDishToOrder(id, amount)
				.then(function(response){
					console.log("Adding dish (id: ", id,", amount: ",amount,") to current order. Response: ", response)
					alert("¡Se ha añadido al pedido " + amount + " unidad(es) del plato seleccionado!");
				}, function(response){
					console.log("Error adding dish to order");
					$location.path('/');
				})
		},
		/*------------------------------------------------------------
		---------------------AMPLIACIÓN 4-------------------------------
		------------------------------------------------------------*/
		readRelatedRestaurantsByCategory : function(id) {
			restaurantsFactory.getRelatedRestaurantsByCategory(id)
				.then(function(response){
					restaurantProfileViewModel.relatedRestaurantsByCategory = response
					console.log("Reading related restaurants by category (restaurant with id : ", id,") Response: ", response)
				}, function(response){
					console.log("Error reading related restaurants by category");
				})
		},
		readRelatedRestaurantsByCity : function(id) {
			restaurantsFactory.getRelatedRestaurantsByCity(id)
				.then(function(response){
					restaurantProfileViewModel.relatedRestaurantsByCity = response
					console.log("Reading related restaurants by city (restaurant with id : ", id,") Response: ", response)
				}, function(response){
					console.log("Error reading related restaurants by city");
				})
		},
		readRelatedRestaurantsByPrice : function(id) {
			restaurantsFactory.getRelatedRestaurantsByPrice(id)
				.then(function(response){
					restaurantProfileViewModel.relatedRestaurantsByPrice = response
					console.log("Reading related restaurants by price (restaurant with id : ", id,") Response: ", response)
				}, function(response){
					console.log("Error reading related restaurants by price");
				})
		},
		readRelatedRestaurantsByGrade : function(id) {
			restaurantsFactory.getRelatedRestaurantsByGrade(id)
				.then(function(response){
					restaurantProfileViewModel.relatedRestaurantsByGrade = response
					console.log("Reading related restaurants by grade (restaurant with id : ", id,") Response: ", response)
				}, function(response){
					console.log("Error reading related restaurants by grade");
				})
		}
    }
	
	// Comprobación inicial al crearse el controlador (al entrar al template)
   	console.log("Entering restaurantProfileCtrl with $routeParams.ID=",$routeParams.ID);
   	restaurantProfileViewModel.functions.readRestaurant($routeParams.ID);
   	
	// Se aplican los estilos
	// Agrega los estilos específicos para esta vista
 	var head = document.getElementsByTagName('head')[0];
  	var link = document.createElement('link');
  	link.rel = 'stylesheet';
  	link.href = '../css/restaurant/restaurantProfile.css';
  	head.appendChild(link);
  	var link2 = document.createElement('link');
  	link2.rel = 'stylesheet';
  	link2.href = '../css/restaurant/restaurantCollection.css';
  	head.appendChild(link2);
  	// Remueve los estilos específicos cuando la vista cambia
  	$rootScope.$on('$routeChangeSuccess', function() {
    	if(link.parentElement === head && link2.parentElement === head){
			head.removeChild(link);
			head.removeChild(link2);
		}
  	});
	
}])