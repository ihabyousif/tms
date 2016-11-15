(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('CustomerGroupDialogController', CustomerGroupDialogController);

    CustomerGroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CustomerGroup'];

    function CustomerGroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CustomerGroup) {
        var vm = this;

        vm.customerGroup = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.customerGroup.id !== null) {
                CustomerGroup.update(vm.customerGroup, onSaveSuccess, onSaveError);
            } else {
                CustomerGroup.save(vm.customerGroup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tmsApp:customerGroupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
