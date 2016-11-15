'use strict';

describe('Controller Tests', function() {

    describe('CustomerGroup Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCustomerGroup;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCustomerGroup = jasmine.createSpy('MockCustomerGroup');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CustomerGroup': MockCustomerGroup
            };
            createController = function() {
                $injector.get('$controller')("CustomerGroupDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tmsApp:customerGroupUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
