angular.module('JustEatApp')
.factory('usersFactory',['$http', function($http){
	
	// The base URL needed for executing the web services
	var url = 'https://localhost:8443/JustEat_Act03/rest/users/';
	var usersInterface = {
    	getUser : function(){
			url = url;
			return $http.get(url)
				.then(function(response){
					return response.data;
				});   
    	},
    	putUser : function(user){
			url = url;
			return $http.put(url, user)
				.then(function(response){	
					return response;
				});
    	},
    	deleteUser : function(){
			url = url;
			return $http.delete(url)
				.then(function(response){
					return response.status;
				});
        }		
    }
    return usersInterface;
}])