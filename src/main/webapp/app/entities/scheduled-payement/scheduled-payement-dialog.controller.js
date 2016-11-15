(function() {
    'use strict';

    angular
        .module('tmsApp')
        .controller('ScheduledPayementDialogController', ScheduledPayementDialogController);

    ScheduledPayementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ScheduledPayement', 'Customer'];

    function ScheduledPayementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ScheduledPayement, Customer) {
        var vm = this;

        vm.scheduledPayement = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.customers = Customer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.scheduledPayement.id !== null) {
                ScheduledPayement.update(vm.scheduledPayement, onSaveSuccess, onSaveError);
            } else {
                ScheduledPayement.save(vm.scheduledPayement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tmsApp:scheduledPayementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
