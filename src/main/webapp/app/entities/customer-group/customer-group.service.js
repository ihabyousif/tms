(function() {
    'use strict';
    angular
        .module('tmsApp')
        .factory('CustomerGroup', CustomerGroup);

    CustomerGroup.$inject = ['$resource'];

    function CustomerGroup ($resource) {
        var resourceUrl =  'api/customer-groups/:id';

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
