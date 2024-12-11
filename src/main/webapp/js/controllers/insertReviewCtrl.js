angular.module('JustEatApp')
.controller('insertReviewCtrl', ['usersFactory','reviewsFactory', 'restaurantsFactory', '$routeParams', '$location', '$rootScope',
			function(usersFactory, reviewsFactory, restaurantsFactory, $routeParams, $location, $rootScope){
	var insertReviewViewModel = this;
   	insertReviewViewModel.review = {};
   	insertReviewViewModel.user = {};
   	insertReviewViewModel.restaurant = {};
   	insertReviewViewModel.alreadyReviewed = false;
   	
   	insertReviewViewModel.functions = {
   		readUser : function() {
			usersFactory.getUser()
				.then(function(response){
					insertReviewViewModel.user = response
					console.log("Reading user with id: ", insertReviewViewModel.user.id," Response: ", response)
					insertReviewViewModel.review.idu = insertReviewViewModel.user.id;
				}, function(response){
					console.log("Error reading user data");
					$location.path('/');
				})
		},
		readRestaurant : function() {
			restaurantsFactory.getRestaurant($routeParams.ID)
				.then(function(response){
					insertReviewViewModel.restaurant = response
					console.log("Reading restaurant with id: ", insertReviewViewModel.restaurant.id," Response: ", response)
					insertReviewViewModel.review.idr = insertReviewViewModel.restaurant.id;
				}, function(response){
					console.log("Error reading restaurant data");
					$location.path('/');
				})
		},
		readIfRestaurantAlreadyReviewed : function() {
			reviewsFactory.getIfRestaurantReviewed($routeParams.ID)
				.then(function(response){
					insertReviewViewModel.alreadyReviewed = response
					console.log("Reading if restaurant already reviewed by user. Response: ", response)
					if(insertReviewViewModel.alreadyReviewed == true){
						// El usuario ya había valorado el restaurante, por lo que se redirige
						console.log("Restaurant already reviewed. You can not review the restaurant more than once");
						$location.path('/');
					} else {
						// Se lee la info del restaurante
						insertReviewViewModel.functions.readRestaurant();
					}
				}, function(response){
					console.log("Error reading if restaurant already reviewed");
					$location.path('/');
				})
		},
		createReview : function() {
			if(insertReviewViewModel.review.review == null){
				insertReviewViewModel.review.review = '';
			}
			reviewsFactory.postReview(insertReviewViewModel.review)
				.then(function(response){
					console.log("Creating review. Response: ", response);
    			}, function(response){
    				console.log("Error creating review");
    			})	
    			$location.path('/restaurantProfile/'+$routeParams.ID);		
		}
	}
	
	// Comprobación inicial al crearse el controlador (al entrar al template)
   	console.log("Entering insertReviewCtrl with $routeParams.ID=",$routeParams.ID);
   	insertReviewViewModel.functions.readUser();
   	insertReviewViewModel.functions.readIfRestaurantAlreadyReviewed();
   	
	// Agrega los estilos específicos para esta vista
 	var head = document.getElementsByTagName('head')[0];
 	var link = document.createElement('link');
  	link.rel = 'stylesheet';
  	link.href = '../css/general/form.css';
  	head.appendChild(link);
  	var link2 = document.createElement('link');
  	link2.rel = 'stylesheet';
  	link2.href = '../css/review/createReview.css';
  	head.appendChild(link2);
  	// Remueve los estilos específicos cuando la vista cambia
  	$rootScope.$on('$routeChangeSuccess', function() {
    	if(link.parentElement === head && link2.parentElement === head){
			head.removeChild(link);
			head.removeChild(link2);
		}
  	});
}]);