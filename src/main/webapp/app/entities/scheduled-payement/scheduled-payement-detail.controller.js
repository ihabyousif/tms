(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('ScheduledPayementDetailController', ScheduledPayementDetailController);

    ScheduledPayementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ScheduledPayement', 'Customer'];

    function ScheduledPayementDetailController($scope, $rootScope, $stateParams, entity, ScheduledPayement, Customer) {
        var vm = this;

        vm.scheduledPayement = entity;

        var unsubscribe = $rootScope.$on('tmsApp:scheduledPayementUpdate', function(event, result) {
            vm.scheduledPayement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
