(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('CustomerGroupDetailController', CustomerGroupDetailController);

    CustomerGroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CustomerGroup'];

    function CustomerGroupDetailController($scope, $rootScope, $stateParams, entity, CustomerGroup) {
        var vm = this;

        vm.customerGroup = entity;

        var unsubscribe = $rootScope.$on('tmsApp:customerGroupUpdate', function(event, result) {
            vm.customerGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
