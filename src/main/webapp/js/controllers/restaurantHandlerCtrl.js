angular.module('JustEatApp')
.controller('restaurantHandlerCtrl', ['usersFactory','restaurantsFactory', 'categoriesFactory', '$routeParams','$location', '$rootScope', '$scope',
			function(usersFactory, restaurantsFactory, categoriesFactory, $routeParams, $location, $rootScope, $scope){
	var restaurantHandlerViewModel = this;
   	restaurantHandlerViewModel.restaurant = {};
   	restaurantHandlerViewModel.restaurantFixedName;
   	restaurantHandlerViewModel.user = {};
   	restaurantHandlerViewModel.restaurantCategories = [];
   	restaurantHandlerViewModel.allCategories = [];
   	restaurantHandlerViewModel.selectedCategories = [];
   	restaurantHandlerViewModel.validationErros = {};
   	
   	restaurantHandlerViewModel.functions = {
   		where : function(route){
			return $location.path() == route;   			
   		},
   		readRestaurant : function(id) {
			restaurantsFactory.getRestaurant(id)
				.then(function(response){
					restaurantHandlerViewModel.restaurant = response;
					restaurantHandlerViewModel.restaurantFixedName = response.name;
					console.log("Reading restaurant with id: ", id," Response: ", response);
    				// Si el usuario no es el dueño del restaurante, se redirige
    				if(restaurantHandlerViewModel.user.id != restaurantHandlerViewModel.restaurant.idu){
						console.log("User is not the restaurant owner. You can not edit this restaurant.");
						$location.path('/userProfile');
					} else {
						// El restaurante SÍ es del usuario, por lo que se recuperan también sus categorías
						restaurantHandlerViewModel.functions.readRestaurantCategories(restaurantHandlerViewModel.restaurant.id);
					}
    			}, function(response){
    				console.log("Error reading restaurant data");
    				// Se redirige a la página principal si no se encuentra el restaurante
    				$location.path('/userProfile');
    			})
		},
   		readUser : function() {
			usersFactory.getUser()
				.then(function(response){
					restaurantHandlerViewModel.user = response
					console.log("Reading user with id: ", restaurantHandlerViewModel.user.id," Response: ", response)
					// Una vez se tiene la información del usuario, se procede a recuperar el resto de información necesaria
					if ($routeParams.ID != undefined){
						// Esto es editRestaurant/ID, por lo que se necesita la información del restaurante para que aparezca en el formulario de edición
						restaurantHandlerViewModel.functions.readRestaurant($routeParams.ID);
					}
				}, function(response){
					console.log("Error reading the user");
					$location.path('/');
				})
		},
		readRestaurantCategories : function(id) {
			restaurantsFactory.getRestaurantCategories(id)
				.then(function(response){
					restaurantHandlerViewModel.restaurantCategories = response;
					restaurantHandlerViewModel.functions.fillCategories();
					console.log("Reading restaurant categories (id: ", id,") Response: ", response)
				}, function(response){
					console.log("Error reading restaurant categories info");
					$location.path('/');
				})
		},
		readAllCategories : function(id) {
			categoriesFactory.getAllCategories()
				.then(function(response){
					restaurantHandlerViewModel.allCategories = response
					console.log("Reading all categories. Response: ", response)
				}, function(response){
					console.log("Error reading all categories");
					$location.path('/');
				})
		},
		createRestaurant : function(categoriesList) {
			restaurantsFactory.postRestaurant(restaurantHandlerViewModel.restaurant, categoriesList)
				.then(function(response){
					console.log("Creating restaurant. Response: ", response);
					// Restaurante creado, se redirige al perfil de usuario
					$location.path('/userProfile');
    			}, function(response){
					restaurantHandlerViewModel.validationErrors = response.data;
    				console.log("Error creating restaurant");
    			})			
		},
		updateRestaurant : function(categoriesList) {
			restaurantsFactory.putRestaurant(restaurantHandlerViewModel.restaurant, categoriesList)
				.then(function(response){
					console.log("Updating restaurant with id: ", restaurantHandlerViewModel.restaurant.id, "Response: ", response);
    				// Restaurante actualizado, se redirige al perfil del restaurante
    				$location.path('/restaurantProfile/'+restaurantHandlerViewModel.restaurant.id);
    			}, function(response){
					restaurantHandlerViewModel.validationErrors = response.data;
    				console.log("Error updating restaurant");
    			})			
		},
		fillCategories : function() {
	        var index = 0;
	        for (let step = 0; step < restaurantHandlerViewModel.allCategories.length; step++) {
	        	if (index < restaurantHandlerViewModel.restaurantCategories.length) {
	            	if (restaurantHandlerViewModel.allCategories[step].id === restaurantHandlerViewModel.restaurantCategories[index].id) {
	              		restaurantHandlerViewModel.selectedCategories[step] = step;
	              		index++;
	           		} else {
	              		restaurantHandlerViewModel.selectedCategories[step] = -1;
	            	}
	          	} else { restaurantHandlerViewModel.selectedCategories[step] = -1; }
	        }
	     },
		/* Validaciones */ 
		validatePriceRange: function() {
			if (restaurantHandlerViewModel.restaurant.maxPrice < restaurantHandlerViewModel.restaurant.minPrice) {						
				$scope.restaurantForm.minPrice.$setValidity('range', false);
				$scope.restaurantForm.maxPrice.$setValidity('range', false);
			} else {						
				$scope.restaurantForm.minPrice.$setValidity('range', true);
				$scope.restaurantForm.maxPrice.$setValidity('range', true);
			}
		},
		/* Handler para la acción del form*/
		restaurantHandlerSwitcher : function(){
			console.log($location.path());
			// Se procesa la lista de categorías seleccionadas antes de hacer nada (gestionar el tema de los -1 y empty tras desmarcar checkbox)
			var categoriesList = [];
			for (var i = 0; i < restaurantHandlerViewModel.selectedCategories.length; i++) {
			  if(restaurantHandlerViewModel.selectedCategories[i] != -1 && restaurantHandlerViewModel.selectedCategories[i] != undefined ){
				  categoriesList.push(restaurantHandlerViewModel.selectedCategories[i]);
			  }
			}			
			if(restaurantHandlerViewModel.functions.where('/insertRestaurant')){
				restaurantHandlerViewModel.functions.createRestaurant(categoriesList);
			} else {
				restaurantHandlerViewModel.functions.updateRestaurant(categoriesList);
			} 
		}
	}
	
	// Comprobación inicial al crearse el controlador (al entrar al template)
   	console.log("Entering restaurantHandlerCtrl with $routeParams.ID=",$routeParams.ID);
   	restaurantHandlerViewModel.functions.readUser();
   	restaurantHandlerViewModel.functions.readAllCategories();
   	
   	// Este código se ha movido dentro de readUser para asegurar que se tiene toda la info del usuario antes de realizar otras operaciones
   	/*if ($routeParams.ID != undefined){
		// Esto es editRestaurant/ID, por lo que se necesita la información del restaurante para que aparezca en el formulario de edición
		restaurantHandlerViewModel.functions.readRestaurant($routeParams.ID);
	}*/
   	
	// Agrega los estilos específicos para esta vista
 	var head = document.getElementsByTagName('head')[0];
 	var link = document.createElement('link');
  	link.rel = 'stylesheet';
  	link.href = '../css/general/form.css';
  	head.appendChild(link);
  	var link2 = document.createElement('link');
  	link2.rel = 'stylesheet';
  	link2.href = '../css/restaurant/editRestaurant.css';
  	head.appendChild(link2);
  	// Remueve los estilos específicos cuando la vista cambia
  	$rootScope.$on('$routeChangeSuccess', function() {
    	if(link.parentElement === head && link2.parentElement === head){
			head.removeChild(link);
			head.removeChild(link2);
		}
  	});
}]);