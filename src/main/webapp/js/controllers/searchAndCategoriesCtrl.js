angular.module('JustEatApp')
.controller('searchAndCategoriesCtrl', ['categoriesFactory', '$rootScope', '$location', function(categoriesFactory, $rootScope, $location){
    var searchAndCategoriesViewModel = this;
    searchAndCategoriesViewModel.categories=[];
    searchAndCategoriesViewModel.searchText = null;
    searchAndCategoriesViewModel.available = -1;
    searchAndCategoriesViewModel.order = 0;
    
    searchAndCategoriesViewModel.functions = {
		readCategories : function() {
			categoriesFactory.getAllCategories()
				.then(function(response){
					searchAndCategoriesViewModel.categories = response
					console.log("Obteniendo todas las categorías. Response: ", response)
				}, function(response){
					console.log("Error al recuperar todas las categorías");
				})
		},
		submitForm : function() {
			// Se recogen los query params y se prepara la nueva URL
			var encodedSearchText = encodeURIComponent(searchAndCategoriesViewModel.searchText);
			var encodedAvailable = encodeURIComponent(searchAndCategoriesViewModel.available);
			var encodedOrder = encodeURIComponent(searchAndCategoriesViewModel.order);
			var searchUrl = "searchText="+encodedSearchText+"&available="+encodedAvailable+"&order="+encodedOrder
			console.log('/search?'+searchUrl);
			$location.path('/search?'+searchUrl);
		}
    }
    // Se realiza esta llamada para cuando el controlador sea creado (se inicialice).
    // Esto hará que las categorías estén cargadas con los valores correctos
    console.log("Entering searchAndCategoriesCtrl");
	searchAndCategoriesViewModel.functions.readCategories();
	
	// Agrega los estilos específicos para esta vista
 	var head = document.getElementsByTagName('head')[0];
  	var link = document.createElement('link');
  	link.rel = 'stylesheet';
  	link.href = '../css/search/searchAndCategories.css';
  	head.appendChild(link);
  	// Remueve los estilos específicos cuando la vista cambia
  	$rootScope.$on('$routeChangeSuccess', function() {
    	if(link.parentElement === head){
			head.removeChild(link);
		}
  	});
}])