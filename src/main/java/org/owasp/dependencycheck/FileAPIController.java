package org.owasp.dependencycheck;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/api")
public class FileAPIController {



    @Autowired
    FileService fileService; //Service which will do all data retrieval/manipulation work

    // -------------------Retrieve All Users---------------------------------------------

    @RequestMapping(value = "/file/", method = RequestMethod.GET)
    public ResponseEntity<List<FileEntity>> listAllFiles() {
        List<FileEntity> files = fileService.getFilesinDB();
        if (files.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<FileEntity>>(files, HttpStatus.OK);
    }
    // -------------------Retrieve Single User------------------------------------------

    @RequestMapping(value = "/file/{file_name}", method = RequestMethod.GET)
    public ResponseEntity<?> getFile(@PathVariable("file_name") String fileName) {

        FileEntity file = fileService.getFileByFileName(fileName);
        if (file == null) {
            return new ResponseEntity(("File with this file name: " + fileName
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<FileEntity>(file, HttpStatus.OK);
    }

    // -------------------Retrieve Single User By Id------------------------------------------

    @RequestMapping(value = "/fileById/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getFileById(@PathVariable("id") long id) {

        FileEntity user = fileService.findById(id);
        if (user == null) {
            return new ResponseEntity(("User with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<FileEntity>(user, HttpStatus.OK);
    }

}