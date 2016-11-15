(function() {
    'use strict';

    angular
        .module('tmsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transaction', {
            parent: 'entity',
            url: '/transaction?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tmsApp.transaction.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transaction/transactions.html',
                    controller: 'TransactionController',
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
                    $translatePartialLoader.addPart('transaction');
                    $translatePartialLoader.addPart('trxType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('transaction-detail', {
            parent: 'entity',
            url: '/transaction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tmsApp.transaction.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transaction/transaction-detail.html',
                    controller: 'TransactionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transaction');
                    $translatePartialLoader.addPart('trxType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Transaction', function($stateParams, Transaction) {
                    return Transaction.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('transaction.new', {
            parent: 'transaction',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction/transaction-dialog.html',
                    controller: 'TransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                amount: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('transaction', null, { reload: true });
                }, function() {
                    $state.go('transaction');
                });
            }]
        })
        .state('transaction.edit', {
            parent: 'transaction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction/transaction-dialog.html',
                    controller: 'TransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Transaction', function(Transaction) {
                            return Transaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaction', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transaction.delete', {
            parent: 'transaction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction/transaction-delete-dialog.html',
                    controller: 'TransactionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Transaction', function(Transaction) {
                            return Transaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaction', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
