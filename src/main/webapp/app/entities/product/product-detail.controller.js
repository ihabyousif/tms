(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('ProductDetailController', ProductDetailController);

    ProductDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Product'];

    function ProductDetailController($scope, $rootScope, $stateParams, entity, Product) {
        var vm = this;

        vm.product = entity;

        var unsubscribe = $rootScope.$on('tmsApp:productUpdate', function(event, result) {
            vm.product = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
