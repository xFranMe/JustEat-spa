angular.module('JustEatApp')
.controller('searchHandlerCtrl', ['restaurantsFactory', 'categoriesFactory', '$routeParams','$location', '$rootScope',
			function(restaurantsFactory, categoriesFactory, $routeParams, $location, $rootScope){
	var searchHandlerViewModel = this;
	searchHandlerViewModel.category = {};
	searchHandlerViewModel.restaurantsByCategory = [];
	searchHandlerViewModel.restaurantsByFilter = {};
   	
   	searchHandlerViewModel.functions = {
   		where : function(route){
			return $location.path() == route;   			
   		},
   		readCategory : function(id) {
			categoriesFactory.getCategory(id)
				.then(function(response){
					searchHandlerViewModel.category = response;
					console.log("Reading category with id: ", id," Response: ", response);
					// Se recuperan los restaurantes con la categoría
					searchHandlerViewModel.functions.readRestaurantsByCategory(searchHandlerViewModel.category.id);    				
    			}, function(response){
    				console.log("Error reading restaurant by category");
    				// Se redirige a la página principal si no se encuentran los restaurantes
    				$location.path('/');
    			})
		},		
   		readRestaurantsByCategory : function(id) {
			restaurantsFactory.getRestaurantsByCategory(id)
				.then(function(response){
					searchHandlerViewModel.restaurantsByCategory = response;
					console.log("Reading restaurant with category id: ", id," Response: ", response);    				
    			}, function(response){
    				console.log("Error reading restaurant by category");
    				// Se redirige a la página principal si no se encuentran los restaurantes
    				$location.path('/');
    			})
		},
		readRestaurantsByFilter : function(query) {
			restaurantsFactory.getRestaurantsByFilter(query)
				.then(function(response){
					searchHandlerViewModel.restaurantsByFilter = response;
					console.log("Restaurants filtered successfully");
    			}, function(response){
    				console.log("Error reading restaurants by filter");
    				// Se redirige a la página principal
    				$location.path('/');
    			})
		}
	}
	
	// Comprobación inicial al crearse el controlador (al entrar al template)
   	if($routeParams.ID != undefined){
		// Se realiza la búsqueda por categoría. Primero se obtiene la categoría.
		console.log("Entering searchHandlerCtrl with ID =",$routeParams.ID);
		searchHandlerViewModel.functions.readCategory($routeParams.ID);
	} else {
		// Se realiza la búsqueda por filtros
		console.log("Entering searchHandlerCtrl with query =",$routeParams.query);
		searchHandlerViewModel.functions.readRestaurantsByFilter($routeParams.query);
	}
   	
	// Agrega los estilos específicos para esta vista
 	var head = document.getElementsByTagName('head')[0];
 	var link = document.createElement('link');
  	link.rel = 'stylesheet';
  	link.href = '../css/search/restaurantList.css';
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
}]);