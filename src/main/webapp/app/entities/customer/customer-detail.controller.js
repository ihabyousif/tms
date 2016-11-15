(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('CustomerDetailController', CustomerDetailController);

    CustomerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Customer', 'Branch'];

    function CustomerDetailController($scope, $rootScope, $stateParams, entity, Customer, Branch) {
        var vm = this;

        vm.customer = entity;

        var unsubscribe = $rootScope.$on('tmsApp:customerUpdate', function(event, result) {
            vm.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
