package com.spring.kodo.controller;

import com.spring.kodo.service.inter.FileService;
import com.spring.kodo.util.exception.FileUploadToGCSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="/file")
public class FileController
{
    /**
     * 3 areas for file upload / download:
     * (1) Account's Display Picture
     * (2) Course's Banner Picture
     * (3) Content > Multimedia for Lesson
    */
    Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile multipartFile) {

        logger.info("HIT file/upload | POST | Received File Name : " + multipartFile.getOriginalFilename());

        try
        {
            String fileBucketURL = fileService.upload(multipartFile);
            return ResponseEntity.status(HttpStatus.OK).body(fileBucketURL);
        }
        catch (FileUploadToGCSException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{gcsFileName}")
    public ResponseEntity delete(@PathVariable String gcsFileName)
    {
           Boolean success = fileService.delete(gcsFileName);
           if (success)
           {
               return ResponseEntity.status(HttpStatus.OK).body("File successfully deleted");
           }
           else
           {
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File does not exist. Nothing to delete.");
           }
    }
}
