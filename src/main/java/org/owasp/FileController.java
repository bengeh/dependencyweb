package org.owasp.dependencycheck;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import org.owasp.dependencycheck.exception.ExceptionCollection;
import org.owasp.dependencycheck.exception.ReportException;
import org.owasp.dependencycheck.*;


@Controller
public class FileController {

    @Value("${welcome.message:test}")
    private String message = "Hello World";

    /*@GetMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("message", this.message);
        return "welcome";
    }*/

    @GetMapping("/")
    public String index(){
        return "upload";
    }


    @PostMapping("/upload")
    public String fileUploadPage(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        System.out.println("inside the fileuploadpage");



        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try{
            byte[] Byte = file.getBytes();
            System.out.println("this is the file name: " + file.getOriginalFilename());

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch(IOException e) {
            e.printStackTrace();
        }

        return "redirect:uploadStatus";
    }


    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

}
