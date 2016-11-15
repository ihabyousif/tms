(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('TransactionDetailController', TransactionDetailController);

    TransactionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Transaction', 'Customer'];

    function TransactionDetailController($scope, $rootScope, $stateParams, entity, Transaction, Customer) {
        var vm = this;

        vm.transaction = entity;

        var unsubscribe = $rootScope.$on('tmsApp:transactionUpdate', function(event, result) {
            vm.transaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
