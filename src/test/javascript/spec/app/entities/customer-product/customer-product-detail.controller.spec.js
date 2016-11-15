'use strict';

describe('Controller Tests', function() {

    describe('CustomerProduct Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCustomerProduct, MockCustomer, MockProduct;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCustomerProduct = jasmine.createSpy('MockCustomerProduct');
            MockCustomer = jasmine.createSpy('MockCustomer');
            MockProduct = jasmine.createSpy('MockProduct');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CustomerProduct': MockCustomerProduct,
                'Customer': MockCustomer,
                'Product': MockProduct
            };
            createController = function() {
                $injector.get('$controller')("CustomerProductDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tmsApp:customerProductUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
