angular.module('JustEatApp')
.controller('editUserCtrl', ['usersFactory', '$rootScope', '$location', 
			function(usersFactory, $rootScope, $location){
    var editUserViewModel = this;
    editUserViewModel.user={};
    editUserViewModel.validationErrors = {};
    
    editUserViewModel.functions = {
		readUser : function() {
			usersFactory.getUser()
				.then(function(response){
					editUserViewModel.user = response
					console.log("Obteniendo usuario con id: ", editUserViewModel.user.id," Response: ", response)
				}, function(response){
					console.log("Error al recuperar el usuario");
					$location.path('/');
				})
		},
		updateUser : function() {
			usersFactory.putUser(editUserViewModel.user)
				.then(function(response){
					console.log("Updating user with id: ", editUserViewModel.user.id," Response: ", response);
					$location.path('/userProfile');
    			}, function(response){
    				console.log("Error updating user data");
    				editUserViewModel.validationErrors = response.data;
    				// No se redirige a ningún sitio para que el usuario pueda editar de nuevo
    			})
		}
    }
    
    // Se realiza esta llamada para cuando el controlador sea creado (se inicialice).
    // Esto hará que el objeto "user" esté cargado con los valores correctos
    console.log("Entering editUserCtrl");
	editUserViewModel.functions.readUser();
	
	// Agrega los estilos específicos para esta vista
 	var head = document.getElementsByTagName('head')[0];
  	var link = document.createElement('link');
  	link.rel = 'stylesheet';
  	link.href = '../css/user/editUser.css';
  	head.appendChild(link);
  	// Remueve los estilos específicos cuando la vista cambia
  	$rootScope.$on('$routeChangeSuccess', function() {
    	if(link.parentElement === head){
			head.removeChild(link);
		}
  	});
}])