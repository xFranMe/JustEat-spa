angular.module('JustEatApp')
.controller('userProfileCtrl', ['usersFactory', 'restaurantsFactory', 'ordersFactory','$rootScope',
			function(usersFactory, restaurantsFactory, ordersFactory, $rootScope){
    var userProfileViewModel = this;
    userProfileViewModel.user={};
    userProfileViewModel.restaurants=[];
    userProfileViewModel.orders=[];
    
    userProfileViewModel.functions = {
		readUser : function() {
			usersFactory.getUser()
				.then(function(response){
					userProfileViewModel.user = response
					console.log("Obteniendo usuario con id: ", userProfileViewModel.user.id," Response: ", response)
					// Se recuperan los restaurantes y los pedidos del usuario
					userProfileViewModel.functions.readUserRestaurants();
					userProfileViewModel.functions.readUserOrders();
				}, function(response){
					console.log("Error al recuperar el usuario");
					$location.path('/');
				})
		},
		readUserRestaurants : function() {
			restaurantsFactory.getUserRestaurants()
				.then(function(response){
					userProfileViewModel.restaurants = response
					console.log("Obteniendo todos los restaurantes del usuario con id: ", userProfileViewModel.user.id," Response: ", response)
				}, function(response){
					console.log("Error al recuperar los restaurantes del usuario");
					$location.path('/');
				})
		},
		readUserOrders : function() {
			ordersFactory.getUserOrders()
				.then(function(response){
					userProfileViewModel.orders = response
					console.log("Obteniendo todos los pedidos del usuario con id: ", userProfileViewModel.user.id," Response: ", response)
				}, function(response){
					console.log("Error al recuperar los pedidos del usuario");
					$location.path('/');
				})
		}
    }
    // Se realiza esta llamada para cuando el controlador sea creado (se inicialice).
    // Esto hará que el objeto "user" esté cargado con los valores correctos
	userProfileViewModel.functions.readUser();
	
	
	// Agrega los estilos específicos para esta vista
 	var head = document.getElementsByTagName('head')[0];
  	var link = document.createElement('link');
  	link.rel = 'stylesheet';
  	link.href = '../css/user/userProfile.css';
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