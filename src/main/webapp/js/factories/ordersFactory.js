angular.module('JustEatApp')
.factory('ordersFactory',['$http', function($http){
	
	// The base URL needed for executing the web services
	var url = 'https://localhost:8443/JustEat_Act03/rest/orders/';
	var ordersInterface = {
    	getUserOrders : function(){
			url = url;
			return $http.get(url)
				.then(function(response){
					return response.data;
				});   
    	},
    	getOrder : function(id){
			var urlid = url + id;
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});   
    	},
    	getOrderDishes : function(id){
			var urlid = url + id +"/dishes";
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});   
    	},
    	getCurrentOrderDishes : function(){
			var newurl = url+"cart";
			return $http.get(newurl)
				.then(function(response){
					return response.data;
				});   
    	},
    	deleteDishFromOrder : function(id){
			var newurl = url +"cart/"+id;
			return $http.delete(newurl)
				.then(function(response){
					return response.status;
				});
		},
		postDishToOrder : function(id, amount){
			var newurl = url + "cart/"+id;
			var orderDishes = {};
			orderDishes.amount = amount;
			orderDishes.iddi = id;
			orderDishes.ido = 0;
			return $http.post(newurl, orderDishes)
				.then(function(response){
					return response.status;
				});   
    	},
    	postOrder : function(){
			var newurl = url + "cart";
			var orderDishes = {};
			return $http.post(newurl, orderDishes)
				.then(function(response){
					return response.status;
				});   
    	}
    }
    return ordersInterface;
}])