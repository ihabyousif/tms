(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('CustomerGroupDeleteController',CustomerGroupDeleteController);

    CustomerGroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'CustomerGroup'];

    function CustomerGroupDeleteController($uibModalInstance, entity, CustomerGroup) {
        var vm = this;

        vm.customerGroup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CustomerGroup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
