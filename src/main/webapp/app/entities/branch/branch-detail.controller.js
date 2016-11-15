(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('BranchDetailController', BranchDetailController);

    BranchDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Branch'];

    function BranchDetailController($scope, $rootScope, $stateParams, entity, Branch) {
        var vm = this;

        vm.branch = entity;

        var unsubscribe = $rootScope.$on('tmsApp:branchUpdate', function(event, result) {
            vm.branch = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
