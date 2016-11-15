(function() {
    'use strict';

    angular
        .module('tmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('branch', {
            parent: 'entity',
            url: '/branch?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tmsApp.branch.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/branch/branches.html',
                    controller: 'BranchController',
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
                    $translatePartialLoader.addPart('branch');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('branch-detail', {
            parent: 'entity',
            url: '/branch/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tmsApp.branch.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/branch/branch-detail.html',
                    controller: 'BranchDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('branch');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Branch', function($stateParams, Branch) {
                    return Branch.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('branch.new', {
            parent: 'branch',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/branch/branch-dialog.html',
                    controller: 'BranchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                phone1: null,
                                phone2: null,
                                description: null,
                                address: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('branch', null, { reload: true });
                }, function() {
                    $state.go('branch');
                });
            }]
        })
        .state('branch.edit', {
            parent: 'branch',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/branch/branch-dialog.html',
                    controller: 'BranchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Branch', function(Branch) {
                            return Branch.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('branch', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('branch.delete', {
            parent: 'branch',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/branch/branch-delete-dialog.html',
                    controller: 'BranchDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Branch', function(Branch) {
                            return Branch.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('branch', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
