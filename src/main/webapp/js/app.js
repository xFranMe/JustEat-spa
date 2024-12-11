angular.module('JustEatApp', ['ngRoute'])
.config(function($routeProvider){
	$routeProvider
		.when("/", {
    		controller: "searchAndCategoriesCtrl",
    		controllerAs: "searchAndCategoriesVM",
    		templateUrl: "search/searchAndCategoriesTemplate.html",
    	})
    	.when("/userProfile", {
   			controller: "userProfileCtrl",
    		controllerAs: "userProfileVM",
    		templateUrl: "user/userProfileTemplate.html",
    		resolve: {
    			// produce 500 miliseconds (0,5 seconds) of delay that should be enough to allow the server
    			//does any requested update before reading the orders.
    			// Extracted from script.js used as example on https://docs.angularjs.org/api/ngRoute/service/$route
    			delay: function($q, $timeout) {
    			var delay = $q.defer();
    			$timeout(delay.resolve, 500);
    			return delay.promise;
    			}
    		}      	
        })
        .when("/editUser", {
   			controller: "editUserCtrl",
    		controllerAs: "editUserVM",
    		templateUrl: "user/editUserTemplate.html",     	
        })
        .when("/deleteUser|/deleteRestaurant/:ID|/deleteDish/:ID|/deleteDishFromOrder/:ID", {
   			controller: "deleteConfirmationHandlerCtrl",
    		controllerAs: "deleteConfirmationHandlerVM",
    		templateUrl: "general/deleteConfirmationHandlerTemplate.html",     	
        })
        .when("/restaurantProfile/:ID", {
   			controller: "restaurantProfileCtrl",
    		controllerAs: "restaurantProfileVM",
    		templateUrl: "restaurant/restaurantProfileTemplate.html",
    		resolve: {
    			// produce 500 miliseconds (0,5 seconds) of delay that should be enough to allow the server
    			//does any requested update before reading the orders.
    			// Extracted from script.js used as example on https://docs.angularjs.org/api/ngRoute/service/$route
    			delay: function($q, $timeout) {
    			var delay = $q.defer();
    			$timeout(delay.resolve, 500);
    			return delay.promise;
    			}
    		}    	
        })
        .when("/insertRestaurant|/editRestaurant/:ID", {
   			controller: "restaurantHandlerCtrl",
    		controllerAs: "restaurantHandlerVM",
    		templateUrl: "restaurant/restaurantHandlerTemplate.html",     	
        })
        .when("/insertDish/:ID|/editDish/:ID", {
   			controller: "dishHandlerCtrl",
    		controllerAs: "dishHandlerVM",
    		templateUrl: "dish/dishHandlerTemplate.html",     	
        })
        .when("/insertReview/:ID", {
   			controller: "insertReviewCtrl",
    		controllerAs: "insertReviewVM",
    		templateUrl: "review/insertReviewTemplate.html",     	
        })
        .when("/listOrder/:ID|/resumeOrder", {
   			controller: "orderHandlerCtrl",
    		controllerAs: "orderHandlerVM",
    		templateUrl: "order/orderHandlerTemplate.html",     	
        })
        .when("/searchByCategory/:ID|/search?:query", {
   			controller: "searchHandlerCtrl",
    		controllerAs: "searchHandlerVM",
    		templateUrl: "search/searchHandlerTemplate.html",     	
        })
        .otherwise({
			// En caso de que se intente acceder a una URL no contemplada, se redirige a la página principal
            redirectTo: '/'
         })
        ;
})