(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('CustomerProductDialogController', CustomerProductDialogController);

    CustomerProductDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CustomerProduct', 'Customer', 'Product'];

    function CustomerProductDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CustomerProduct, Customer, Product) {
        var vm = this;

        vm.customerProduct = entity;
        vm.clear = clear;
        vm.save = save;
        vm.customers = Customer.query();
        vm.products = Product.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.customerProduct.id !== null) {
                CustomerProduct.update(vm.customerProduct, onSaveSuccess, onSaveError);
            } else {
                CustomerProduct.save(vm.customerProduct, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tmsApp:customerProductUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
