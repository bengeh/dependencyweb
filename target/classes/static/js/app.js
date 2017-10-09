var app = angular.module('fileManagerApp', ['ui.router', 'ngStorage']);

app.constant('urls', {
    BASE: 'http://localhost:8080/',
    FILE_SERVICE_API : 'http://localhost:8080/api/file/'
});

app.config(['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider){
        $stateProvider
            .state('home',{
                url:'/',
                templateUrl: 'partials/upload',
                controller: 'FileController',
                controllerAs: 'ctrl',
                resolve:{
                    files: function($q, FileService){
                        console.log('Load all files');
                        var deferred = $q.defer();
                        FileService.loadAllFiles().then(deferred.resolve, deferred.resolve);
                        return deferred.promise;
                        }
                    }
                });
            $urlRouterProvider.otherwise('/');
        }])