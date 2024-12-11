angular.module('JustEatApp')
.controller('orderHandlerCtrl', ['ordersFactory', 'usersFactory','$routeParams','$location', '$rootScope',
			function(ordersFactory, usersFactory, $routeParams, $location, $rootScope){
	var orderHandlerViewModel = this;
	orderHandlerViewModel.user = {};
	orderHandlerViewModel.order = {};
	orderHandlerViewModel.dishes = []; // Array de Triplet<OrderDish, Dish, Restaurant>
	orderHandlerViewModel.totalBill = 0;
   	
   	orderHandlerViewModel.functions = {
   		where : function(route){
			return $location.path() == route;   			
   		},
   		/*readUser : function() {
			usersFactory.getUser()
				.then(function(response){
					orderHandlerViewModel.user = response
					console.log("Reading user with id: ", orderHandlerViewModel.user.id," Response: ", response)
					// Se obtiene el pedido solicitado
					orderHandlerViewModel.functions.readOrder($routeParams.ID);
					//orderHandlerViewModel.functions.readOrderDishes($routeParams.ID);
				}, function(response){
					console.log("Error reading the user");
					$location.path('/');
				})
		},*/
   		readOrder : function(id){
		  ordersFactory.getOrder(id)
			.then(function(response){
				orderHandlerViewModel.order = response;
				console.log("Reading order with id: ", id," Response: ", response);
				// En la API se comprueba si el pedido pertenece al usuario. En caso de que no, este cuerpo no se ejecuta
				// El pedido SÍ es del usuario, por lo que se recuperan sus platos
				orderHandlerViewModel.functions.readOrderDishes(orderHandlerViewModel.order.id);
				// Se actualiza el totalBill
				orderHandlerViewModel.totalBill = orderHandlerViewModel.order.totalPrice;
			}, function(response){
				// El pedido no pertenece al usuario
				console.log("Error reading order data");
				// Se redirige al perfil de usuario
				$location.path('/userProfile');
			}) 
		},
   		readOrderDishes : function(id){
			ordersFactory.getOrderDishes(id)
				.then(function(response){
					orderHandlerViewModel.dishes = response;
					console.log("Reading dishes from order with id: ", id," Response: ", response);
    			}, function(response){
    				console.log("Error reading order dishes");
    				// Se redirige al perfil de usuario
    				$location.path('/userProfile');
    			})  
		},
   		readCurrentOrderDishes : function(){
			ordersFactory.getCurrentOrderDishes()
				.then(function(response){
					orderHandlerViewModel.dishes = response;
					console.log("Reading dishes from current order. Response: ", response);
					// Se calcula el precio total del pedido
					orderHandlerViewModel.functions.calculateTotalBill();
    			}, function(response){
    				console.log("Error reading current order dishes");
    				// Se redirige al perfil de usuario
    				$location.path('/userProfile');
    			})
		},
		calculateTotalBill : function(){
			for (let i = 0; i < orderHandlerViewModel.dishes.length; i++) {
  				orderHandlerViewModel.totalBill = orderHandlerViewModel.totalBill + (orderHandlerViewModel.dishes[i].first.amount * orderHandlerViewModel.dishes[i].second.price);
				
			}
		},
		submitOrder : function(){
			ordersFactory.postOrder()
				.then(function(response){
						console.log("Adding current order. Response: ", response);
						alert("¡Pedido realizado satisfactoriamente!");
						// Se redirige al perfil de usuario
						$location.path('/userProfile');
				}, function(response){
					console.log("Error adding current order");
					// Se redirige a la página principal
					$location.path('/');
				})
		}
   		
   		/*readRestaurant : function(id) {
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
		},*/
		
	}
	
	// Comprobación inicial al crearse el controlador (al entrar al template)
   	console.log("Entering orderHandlerCtrl with $routeParams.ID=",$routeParams.ID);
   	if($routeParams.ID == undefined){
		// Se muestra el pedido actual
		orderHandlerViewModel.functions.readCurrentOrderDishes();
	} else {
		// Se muestra un pedido ya realizado
		orderHandlerViewModel.functions.readOrder($routeParams.ID);
	}
   	
	// Agrega los estilos específicos para esta vista
 	var head = document.getElementsByTagName('head')[0];
 	var link = document.createElement('link');
  	link.rel = 'stylesheet';
  	link.href = '../css/order/orderResume.css';
  	head.appendChild(link);
  	// Remueve los estilos específicos cuando la vista cambia
  	$rootScope.$on('$routeChangeSuccess', function() {
    	if(link.parentElement === head){
			head.removeChild(link);
		}
  	});
}]);