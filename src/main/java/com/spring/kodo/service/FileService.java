package com.spring.kodo.service;

import com.spring.kodo.util.exception.FileUploadToGCSException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService
{
    String upload(MultipartFile multipartFile) throws FileUploadToGCSException;

    String getExtension(String fileName);  // used to get extension of an uploaded file

    Boolean delete(String gcsFileName);
}
