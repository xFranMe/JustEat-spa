angular.module('JustEatApp')
.factory('dishesFactory',['$http', function($http){
	
	// The base URL needed for executing the web services
	var url = 'https://localhost:8443/JustEat_Act03/rest/dishes/';
	var dishesInterface = {
		getDish : function(id){
			var urlid = url + id;
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});   
    	},
    	postDish : function(dish){
			url = url;
			return $http.post(url, dish)
				.then(function(response){
					return response;
				});
    	},
    	putDish : function(dish){
			var urlid = url + dish.id;
			return $http.put(urlid, dish)
				.then(function(response){
					return response;
				});
    	},
    	deleteDish : function(id){
			var urlid = url + id;
			return $http.delete(urlid)
				.then(function(response){
					return response.status;
				});
        }		
    }
    return dishesInterface;
}])