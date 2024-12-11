angular.module('JustEatApp')
.factory('reviewsFactory',['$http', function($http){
	
	// The base URL needed for executing the web services
	var url = 'https://localhost:8443/JustEat_Act03/rest/reviews/';
	var reviewsInterface = {
    	postReview : function(review){
			url = url;
			return $http.post(url, review)
				.then(function(response){
					return response.status;
				});   
    	},
    	getIfRestaurantReviewed : function(id){
			var urlid = url + id;
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});   
    	}			
    }
    return reviewsInterface;
}])