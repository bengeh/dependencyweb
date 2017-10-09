'use strict';

angular.module('fileManagerApp').factory('FileService',
    ['$localStorage', '$http', '$q', 'urls',
        function($localStorage, $http, $q, urls){
            var factory = {
                loadAllFiles: loadAllFiles,
                getAllFiles: getAllFiles,
                getFileById: getFileById
                };
                return factory;

                function loadAllFiles(){
                    console.log('Fetching all files');
                    var deferred = $q.defer();
                    $http.get(urls.FILE_SERVICE_API)
                        .then(
                            function(response){
                                console.log('Fetched all files successfully');
                                $localStorage.files = response.data;
                                console.log($localStorage.files);
                                console.log("file name: " + response.data[0].fileName);
                                deferred.resolve(response);
                            },
                            function(errResponse){
                                console.log('Error while loading files');
                                deferred.reject(errResponse);
                            }
                        );
                    return deferred.promise;
                }

                function getAllFiles(){
                    console.log("getttttttt all files inside file service: " + $localStorage.files);
                    return $localStorage.files;
                }

                function getFileById(id) {
                    console.log('Fetching File with id :'+id);
                    var deferred = $q.defer();
                    $http.get(urls.USER_SERVICE_API + id)
                        .then(
                            function (response) {
                                console.log('Fetched successfully File with id :'+id);
                                deferred.resolve(response.data);
                            },
                            function (errResponse) {
                                console.error('Error while loading File with id :'+id);
                                deferred.reject(errResponse);
                            }
                        );
                    return deferred.promise;
                    }

            }
        ]);