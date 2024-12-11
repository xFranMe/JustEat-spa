angular.module('JustEatApp')
.factory('categoriesFactory',['$http', function($http){
	
	// The base URL needed for executing the web services
	var url = 'https://localhost:8443/JustEat_Act03/rest/categories/';
	var categoriesInterface = {
    	getAllCategories : function(){
			url = url;
			return $http.get(url)
				.then(function(response){
					return response.data;
				});   
    	},
    	getCategory : function(id){
			var urlid = url + id;
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});   
    	},			
    }
    return categoriesInterface;
}])