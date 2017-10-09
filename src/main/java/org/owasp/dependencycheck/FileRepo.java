package org.owasp.dependencycheck;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.*;
import org.owasp.dependencycheck.FileEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


import java.util.List;



@RepositoryRestResource
public interface FileRepo extends CrudRepository<FileEntity, Long> {


        List<FileEntity> findAll();

        FileEntity findByFileName(String fileName);

        FileEntity findById(Long id);



        /*@Query("SELECT report_result FROM fileModel.file_entity WHERE LOWER(fileModel.file_name) = LOWER(:fileName)")
        public List<FileEntity> find(@Param("fileName") String fileName);
*/
}

