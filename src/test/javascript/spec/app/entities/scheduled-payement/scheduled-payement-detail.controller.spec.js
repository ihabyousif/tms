'use strict';

describe('Controller Tests', function() {

    describe('ScheduledPayement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockScheduledPayement, MockCustomer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockScheduledPayement = jasmine.createSpy('MockScheduledPayement');
            MockCustomer = jasmine.createSpy('MockCustomer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ScheduledPayement': MockScheduledPayement,
                'Customer': MockCustomer
            };
            createController = function() {
                $injector.get('$controller')("ScheduledPayementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tmsApp:scheduledPayementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
