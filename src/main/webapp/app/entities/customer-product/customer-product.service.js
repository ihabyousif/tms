(function() {
    'use strict';
    angular
        .module('tmsApp')
        .factory('CustomerProduct', CustomerProduct);

    CustomerProduct.$inject = ['$resource'];

    function CustomerProduct ($resource) {
        var resourceUrl =  'api/customer-products/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
