(function() {
    'use strict';

    angular
        .module('tmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('scheduled-payement', {
            parent: 'entity',
            url: '/scheduled-payement?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tmsApp.scheduledPayement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/scheduled-payement/scheduled-payements.html',
                    controller: 'ScheduledPayementController',
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
                    $translatePartialLoader.addPart('scheduledPayement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('scheduled-payement-detail', {
            parent: 'entity',
            url: '/scheduled-payement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tmsApp.scheduledPayement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/scheduled-payement/scheduled-payement-detail.html',
                    controller: 'ScheduledPayementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('scheduledPayement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ScheduledPayement', function($stateParams, ScheduledPayement) {
                    return ScheduledPayement.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('scheduled-payement.new', {
            parent: 'scheduled-payement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/scheduled-payement/scheduled-payement-dialog.html',
                    controller: 'ScheduledPayementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('scheduled-payement', null, { reload: true });
                }, function() {
                    $state.go('scheduled-payement');
                });
            }]
        })
        .state('scheduled-payement.edit', {
            parent: 'scheduled-payement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/scheduled-payement/scheduled-payement-dialog.html',
                    controller: 'ScheduledPayementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ScheduledPayement', function(ScheduledPayement) {
                            return ScheduledPayement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('scheduled-payement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('scheduled-payement.delete', {
            parent: 'scheduled-payement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/scheduled-payement/scheduled-payement-delete-dialog.html',
                    controller: 'ScheduledPayementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ScheduledPayement', function(ScheduledPayement) {
                            return ScheduledPayement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('scheduled-payement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
