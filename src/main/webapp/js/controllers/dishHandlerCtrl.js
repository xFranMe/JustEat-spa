angular.module('JustEatApp')
.controller('dishHandlerCtrl', ['dishesFactory','restaurantsFactory', 'usersFactory', '$routeParams','$location', '$rootScope',
			function(dishesFactory, restaurantsFactory, usersFactory, $routeParams, $location, $rootScope){
	var dishHandlerViewModel = this;
	dishHandlerViewModel.user = {};
	dishHandlerViewModel.dish = {};
	dishHandlerViewModel.dishFixedName;
	dishHandlerViewModel.restaurant = {};
	dishHandlerViewModel.validationErrors = {};
   	
   	
   dishHandlerViewModel.functions = {
   		where : function(route){
			return $location.path() == route;   			
   		},
   		readUser : function(id) {
			usersFactory.getUser()
				.then(function(response){
					dishHandlerViewModel.user = response
					console.log("Reading user with id: ", dishHandlerViewModel.user.id," Response: ", response)
					if(dishHandlerViewModel.functions.where('/insertDish/'+id)){
						// Esto es insertDish/{id restaurante}
						dishHandlerViewModel.functions.readRestaurant(id);
					} else {
						// Esto es editDish/{id plato}
						dishHandlerViewModel.functions.readDish(id);
					}
				}, function(response){
					console.log("Error reading the user");
					$location.path('/');
				})
		},
   		readRestaurant : function(id) {
			restaurantsFactory.getRestaurant(id)
				.then(function(response){
					dishHandlerViewModel.restaurant = response;
					console.log("Reading restaurant with id: ", id," Response: ", response);
    				// Si el usuario no es el dueño del restaurante, se redirige
    				if(dishHandlerViewModel.user.id != dishHandlerViewModel.restaurant.idu){
						console.log("User is not the restaurant owner. You can not add a dish to this restaurant or edit an actual dish from this restaurant.");
						$location.path('/userProfile');
					} else {
						// El restaurante SÍ es del usuario, por lo que se asigna su ID al nuevo plato
						dishHandlerViewModel.dish.idr = dishHandlerViewModel.restaurant.id;
					}
    			}, function(response){
    				console.log("Error reading restaurant data");
    				// Se redirige a la página principal si no se encuentra el restaurante
    				$location.path('/userProfile');
    			})
		},
		readDish : function(id) {
			dishesFactory.getDish(id)
				.then(function(response){
					dishHandlerViewModel.dish = response;
					dishHandlerViewModel.dishFixedName = response.name;
					console.log("Reading dish with id: ", id," Response: ", response);
    				// Obtenida la información del plato, se recupera la información de su restaurante
    				// Esto para comprobar que el usuario sea el dueño del restaurante que sirve el plato
    				dishHandlerViewModel.functions.readRestaurant(dishHandlerViewModel.dish.idr);
    			}, function(response){
    				console.log("Error reading dish data");
    				// Se redirige a la página principal si no se encuentra el plato
    				$location.path('/userProfile');
    			})
		},
		createDish : function() {
			dishesFactory.postDish(dishHandlerViewModel.dish)
				.then(function(response){
					console.log("Creating dish. Response: ", response);
					// Se redirige a la página del restaurante
					$location.path('/restaurantProfile/'+dishHandlerViewModel.restaurant.id);
    			}, function(response){
    				console.log("Error creating dish");
    				dishHandlerViewModel.validationErrors = response.data;
    				// No se redirige a ningún lado para que el usuario pueda modificar los datos del plato que quiere crear
    			})			
		},
		updateDish : function() {
			dishesFactory.putDish(dishHandlerViewModel.dish)
				.then(function(response){
					console.log("Updating dish with id: ", dishHandlerViewModel.dish.id, "Response: ", response);
					// Se redirige a la página del restaurante
					$location.path('/restaurantProfile/'+dishHandlerViewModel.restaurant.id);
    			}, function(response){
    				console.log("Error updating dish");
    				dishHandlerViewModel.validationErrors = response.data;
    				// No se redirige a ningún lado para que el usuario pueda modificar los datos del plato que quiere editar
    			})			
		},
		/* Handler para la acción del form*/
		dishHandlerSwitcher : function(){
			console.log($location.path());
			if(dishHandlerViewModel.functions.where('/insertDish/'+$routeParams.ID)){
				dishHandlerViewModel.functions.createDish();
			} else {
				dishHandlerViewModel.functions.updateDish();
			}
		}
	}
	
	// Comprobación inicial al crearse el controlador (al entrar al template)
   	console.log("Entering dishHandlerCtrl with $routeParams.ID=",$routeParams.ID);
   	dishHandlerViewModel.functions.readUser($routeParams.ID);
   	
	// Agrega los estilos específicos para esta vista
 	var head = document.getElementsByTagName('head')[0];
 	var link = document.createElement('link');
  	link.rel = 'stylesheet';
  	link.href = '../css/dish/editDish.css';
  	head.appendChild(link);
  	var link2 = document.createElement('link');
  	link2.rel = 'stylesheet';
  	link2.href = '../css/general/form.css';
  	head.appendChild(link2);
  	// Remueve los estilos específicos cuando la vista cambia
  	$rootScope.$on('$routeChangeSuccess', function() {
    	if(link.parentElement === head && link2.parentElement === head){
			head.removeChild(link);
			head.removeChild(link2);
		}
  	});
}]);