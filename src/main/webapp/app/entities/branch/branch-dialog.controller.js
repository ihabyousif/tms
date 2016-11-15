(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('BranchDialogController', BranchDialogController);

    BranchDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Branch'];

    function BranchDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Branch) {
        var vm = this;

        vm.branch = entity;
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
            if (vm.branch.id !== null) {
                Branch.update(vm.branch, onSaveSuccess, onSaveError);
            } else {
                Branch.save(vm.branch, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tmsApp:branchUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
