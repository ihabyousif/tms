(function() {
    'use strict';
    angular
        .module('tmsApp')
        .factory('Product', Product);

    Product.$inject = ['$resource', 'DateUtils'];

    function Product ($resource, DateUtils) {
        var resourceUrl =  'api/products/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.activeDate = DateUtils.convertLocalDateFromServer(data.activeDate);
                        data.inactiveDate = DateUtils.convertLocalDateFromServer(data.inactiveDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.activeDate = DateUtils.convertLocalDateToServer(data.activeDate);
                    data.inactiveDate = DateUtils.convertLocalDateToServer(data.inactiveDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.activeDate = DateUtils.convertLocalDateToServer(data.activeDate);
                    data.inactiveDate = DateUtils.convertLocalDateToServer(data.inactiveDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
