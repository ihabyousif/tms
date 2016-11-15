(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('CustomerProductDeleteController',CustomerProductDeleteController);

    CustomerProductDeleteController.$inject = ['$uibModalInstance', 'entity', 'CustomerProduct'];

    function CustomerProductDeleteController($uibModalInstance, entity, CustomerProduct) {
        var vm = this;

        vm.customerProduct = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CustomerProduct.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
