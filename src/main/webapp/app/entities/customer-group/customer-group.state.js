(function() {
    'use strict';

    angular
        .module('tmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer-group', {
            parent: 'entity',
            url: '/customer-group?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tmsApp.customerGroup.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-group/customer-groups.html',
                    controller: 'CustomerGroupController',
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
                    $translatePartialLoader.addPart('customerGroup');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('customer-group-detail', {
            parent: 'entity',
            url: '/customer-group/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tmsApp.customerGroup.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-group/customer-group-detail.html',
                    controller: 'CustomerGroupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerGroup');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CustomerGroup', function($stateParams, CustomerGroup) {
                    return CustomerGroup.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('customer-group.new', {
            parent: 'customer-group',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-group/customer-group-dialog.html',
                    controller: 'CustomerGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                groupName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('customer-group', null, { reload: true });
                }, function() {
                    $state.go('customer-group');
                });
            }]
        })
        .state('customer-group.edit', {
            parent: 'customer-group',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-group/customer-group-dialog.html',
                    controller: 'CustomerGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomerGroup', function(CustomerGroup) {
                            return CustomerGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-group', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-group.delete', {
            parent: 'customer-group',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-group/customer-group-delete-dialog.html',
                    controller: 'CustomerGroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CustomerGroup', function(CustomerGroup) {
                            return CustomerGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-group', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
