(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('ScheduledPayementDeleteController',ScheduledPayementDeleteController);

    ScheduledPayementDeleteController.$inject = ['$uibModalInstance', 'entity', 'ScheduledPayement'];

    function ScheduledPayementDeleteController($uibModalInstance, entity, ScheduledPayement) {
        var vm = this;

        vm.scheduledPayement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ScheduledPayement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
