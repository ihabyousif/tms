(function() {
    'use strict';

    angular
        .module('tmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer-product', {
            parent: 'entity',
            url: '/customer-product?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tmsApp.customerProduct.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-product/customer-products.html',
                    controller: 'CustomerProductController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerProduct');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('customer-product-detail', {
            parent: 'entity',
            url: '/customer-product/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tmsApp.customerProduct.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-product/customer-product-detail.html',
                    controller: 'CustomerProductDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerProduct');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CustomerProduct', function($stateParams, CustomerProduct) {
                    return CustomerProduct.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('customer-product.new', {
            parent: 'customer-product',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-product/customer-product-dialog.html',
                    controller: 'CustomerProductDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                price: null,
                                discount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('customer-product', null, { reload: true });
                }, function() {
                    $state.go('customer-product');
                });
            }]
        })
        .state('customer-product.edit', {
            parent: 'customer-product',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-product/customer-product-dialog.html',
                    controller: 'CustomerProductDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomerProduct', function(CustomerProduct) {
                            return CustomerProduct.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-product', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-product.delete', {
            parent: 'customer-product',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-product/customer-product-delete-dialog.html',
                    controller: 'CustomerProductDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CustomerProduct', function(CustomerProduct) {
                            return CustomerProduct.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-product', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
