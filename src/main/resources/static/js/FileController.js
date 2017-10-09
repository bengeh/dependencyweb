'use strict';

angular.module('fileManagerApp').controller('FileController',
    ['FileService', '$scope', function(FileService, $scope){
        var self = this;
        self.file= {};
        self.files = [];

        $scope.files = getAllFiles();


        self.getAllFiles = getAllFiles;
        self.getFileById = getFileById;

        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;


        function getAllFiles(){
            console.log("hi");
            return FileService.getAllFiles();
        }

        function getFileById(){
            return FileService.getFileById(id);
            }
        }
    ]);