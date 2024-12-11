angular.module('JustEatApp')
.controller('deleteConfirmationHandlerCtrl', ['usersFactory','restaurantsFactory', 'dishesFactory', 'ordersFactory','$routeParams','$location', '$rootScope', '$window',
			function(usersFactory, restaurantsFactory, dishesFactory, ordersFactory, $routeParams, $location, $rootScope, $window){
	var deleteConfirmationHandlerViewModel = this;
   	deleteConfirmationHandlerViewModel.id;
   	deleteConfirmationHandlerViewModel.user = {};
   	deleteConfirmationHandlerViewModel.restaurant = {};
   	deleteConfirmationHandlerViewModel.dish = {};
   	deleteConfirmationHandlerViewModel.functions = {
   		where : function(route){
			return $location.path() == route;   			
   		},
   		readUser : function() {
			usersFactory.getUser()
				.then(function(response){
					deleteConfirmationHandlerViewModel.user = response
					console.log("Reading user with id: ", deleteConfirmationHandlerViewModel.user.id," Response: ", response)
					// Una vez se tiene la información del usuario, se procede a recuperar el resto de información necesaria
					if ($routeParams.ID != undefined){
						deleteConfirmationHandlerViewModel.id = $routeParams.ID;
						if(deleteConfirmationHandlerViewModel.functions.where('/deleteRestaurant/'+deleteConfirmationHandlerViewModel.id)) {
							deleteConfirmationHandlerViewModel.functions.readRestaurant(deleteConfirmationHandlerViewModel.id);
						} else {
							// Eliminar plato de restaurante o de pedido
							deleteConfirmationHandlerViewModel.functions.readDish(deleteConfirmationHandlerViewModel.id);
						}
					}
				}, function(response){
					console.log("Error reading user data");
					$location.path('/');
				})
		},
   		readRestaurant : function(id) {
			restaurantsFactory.getRestaurant(id)
				.then(function(response){
					deleteConfirmationHandlerViewModel.restaurant = response;
					console.log("Reading restaurant with id: ", id," Response: ", response);
    				// Se comprueba que el restaurante sea del usuario
    				// En caso negativo, se hace redirección (un usuario no puede eliminar un restaurante -o un plato de un restaurante- que no sea suyo)
    				if(deleteConfirmationHandlerViewModel.user.id != deleteConfirmationHandlerViewModel.restaurant.idu){
						console.log("User is not the restaurant owner. You can not delete the restaurant or a dish from this restaurant");
						$location.path('/userProfile');
					}
    			}, function(response){
    				console.log("Error reading restaurant data");
    				// Se redirige a la página principal si no se encuentra el restaurante
    				$location.path('/');
    			})
		},
		readDish : function(id) {
			dishesFactory.getDish(id)
				.then(function(response){
					deleteConfirmationHandlerViewModel.dish = response;
					console.log("Reading dish with id: ", id," Response: ", response);
					// Si se quiere borrar el plato del restaurante, hay que recuperar dicho restaurante para ver si es o no del usuario
					if(deleteConfirmationHandlerViewModel.functions.where('/deleteDish/'+deleteConfirmationHandlerViewModel.id)){
						// Se lee el restaurante al que pertenece el plato
	    				// En este método se determinará si el plato pertenece a un restaurante propiedad del usuario. En caso negativo, se redirigirá.
	    				deleteConfirmationHandlerViewModel.functions.readRestaurant(deleteConfirmationHandlerViewModel.dish.idr);
					}
    			}, function(response){
    				console.log("Error reading dish data");
    				// Se redirige a la página principal si no se encuentra el plato
    				$location.path('/');
    			})
		},
   		deleteUser : function() {
			usersFactory.deleteUser()
				.then(function(response){
					console.log("Deleting current user. Response: ", response);
    			}, function(response){
    				console.log("Error deleting current user");
    			})			
		},
		deleteRestaurant : function(id) {
			restaurantsFactory.deleteRestaurant(id)
				.then(function(response){
					console.log("Deleting restaurant with id: ", id, "Response: ", response);
    			}, function(response){
    				console.log("Error deleting restaurant");
    			})			
		},
		deleteDish : function(id) {
			dishesFactory.deleteDish(id)
				.then(function(response){
					console.log("Deleting dish with id: ", id, "Response: ", response);
    			}, function(response){
    				console.log("Error deleting dish");
    			})			
		},
		deleteDishFromOrder : function(id) {
			ordersFactory.deleteDishFromOrder(id)
				.then(function(response){
					console.log("Deleting dish with id: ", id, " from current order. Response: ", response);
    			}, function(response){
    				console.log("Error deleting dish from current order");
    			})			
		},
		deleteConfirmationHandlerSwitcher : function(){
			console.log($location.path());
			if(deleteConfirmationHandlerViewModel.functions.where('/deleteUser')){
				deleteConfirmationHandlerViewModel.functions.deleteUser();
				$window.location.href = '../LoginServlet.do';
			} else if (deleteConfirmationHandlerViewModel.functions.where('/deleteRestaurant/'+deleteConfirmationHandlerViewModel.id)){
				deleteConfirmationHandlerViewModel.functions.deleteRestaurant(deleteConfirmationHandlerViewModel.id);
				$location.path('/userProfile');
			} else if (deleteConfirmationHandlerViewModel.functions.where('/deleteDish/'+deleteConfirmationHandlerViewModel.id)){
				deleteConfirmationHandlerViewModel.functions.deleteDish(deleteConfirmationHandlerViewModel.id);
				$location.path('/restaurantProfile/'+deleteConfirmationHandlerViewModel.restaurant.id);
			} else {
				// Esto sería /deleteDishFromOrder/{ID}
				deleteConfirmationHandlerViewModel.functions.deleteDishFromOrder(deleteConfirmationHandlerViewModel.id);
				// Se vuelve al resumen del pedido
				$location.path('/resumeOrder');
			}
		}
	}
	
	// Comprobación inicial al crearse el controlador (al entrar al template)
	// Se obtiene primero el usuario y, en función de la URL, se obtendrá también el restaurante o el plato en cuestión
   	console.log("Entering deleteConfirmationHandlerCtrl with $routeParams.ID=",$routeParams.ID);
   	deleteConfirmationHandlerViewModel.functions.readUser();
   	
   	// Este código se ha movido dentro de readUser para asegurar que se tiene toda la info del usuario antes de realizar otras operaciones
   	/*if ($routeParams.ID != undefined){
		deleteConfirmationHandlerViewModel.id = $routeParams.ID;
		if(deleteConfirmationHandlerViewModel.functions.where('/deleteRestaurant/'+deleteConfirmationHandlerViewModel.id)) {
			deleteConfirmationHandlerViewModel.functions.readRestaurant(deleteConfirmationHandlerViewModel.id);
		} else {
			deleteConfirmationHandlerViewModel.functions.readDish(deleteConfirmationHandlerViewModel.id);
		}
	}*/
   	
	// Agrega los estilos específicos para esta vista
 	var head = document.getElementsByTagName('head')[0];
  	var link = document.createElement('link');
  	link.rel = 'stylesheet';
  	link.href = '../css/general/deleteConfirmation.css';
  	head.appendChild(link);
  	// Remueve los estilos específicos cuando la vista cambia
  	$rootScope.$on('$routeChangeSuccess', function() {
    	if(link.parentElement === head){
			head.removeChild(link);
		}
  	});
}]);