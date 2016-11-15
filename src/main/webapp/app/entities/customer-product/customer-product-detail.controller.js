(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('CustomerProductDetailController', CustomerProductDetailController);

    CustomerProductDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CustomerProduct', 'Customer', 'Product'];

    function CustomerProductDetailController($scope, $rootScope, $stateParams, entity, CustomerProduct, Customer, Product) {
        var vm = this;

        vm.customerProduct = entity;

        var unsubscribe = $rootScope.$on('tmsApp:customerProductUpdate', function(event, result) {
            vm.customerProduct = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
