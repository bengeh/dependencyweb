package org.owasp.dependencycheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.owasp.dependencycheck.FileRepo;
import org.owasp.dependencycheck.FileEntity;

import java.io.File;
import java.util.List;


@Service
public class FileService {

        @Autowired
        private FileRepo fr;

        private FileEntity fe = new FileEntity();

       /* public File findAFile(String fileName){

            fr.findByFileName(fileName);
            System.out.println("this is the file name that I have gotten from the database: " + fr.findByFileName(fileName));
            System.out.println("this is the report results from my database: " + fe.getReportResults());
            return fe.getReportResults();
        }
*/

       public List<FileEntity> getFilesinDB(){
           return fr.findAll();
       }

    public FileEntity findById(Long id) {
        return fr.findOne(id);
    }


       public FileEntity getFileByFileName(String fileName) {
           return fr.findByFileName(fileName);
       }

        public String addNewFile(String fileName, byte[] ReportResults, long time){
            System.out.println("this is the file name entering the file service: " + fileName);
            System.out.println("this is the report results entering the file service: " + ReportResults);
            System.out.println("this is the time entering the file service: " + time);

            fe.setFileName(fileName);
            fe.setReportResults(ReportResults);
            fe.setTime(time);
            System.out.println("I manage to set everything in the entity %^$%^$");
            System.out.println("I am trying to get the time inside the file service: " + fe.getTime());
            fr.save(fe);
            System.out.println("I manage to save it into the database $%$%#%$#%");
            return "saved";
        }


        public @ResponseBody Iterable<FileEntity> getAllUsers() {
            // This returns a JSON or XML with the users
            return fr.findAll();
        }
    }

