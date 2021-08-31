package com.spring.kodo.service.impl;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.spring.kodo.config.GCSConfig;
import com.spring.kodo.service.FileService;
import com.spring.kodo.util.exception.FileUploadToGCSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService
{
    @Autowired
    private GCSConfig gcsConfig;

    @Override
    public String getExtension(String fileName)
    {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Override
    public String upload(MultipartFile multipartFile) throws FileUploadToGCSException
    {
        try
        {
            String originalFilename = multipartFile.getOriginalFilename();
            // Generate random string values for file name
            String newFileName = UUID.randomUUID().toString().concat(this.getExtension(originalFilename));
            return this.uploadFile(multipartFile, newFileName);
        }
        catch (Exception e)
        {
            throw new FileUploadToGCSException("Unable to upload file to GCS Bucket: " + e.getMessage());
        }
    }

    @Override
    public Boolean delete(String gcsFileName)
    {
        // Get storage object and bucket name from config
        Storage storage = gcsConfig.getGCSClient();
        String bucketName = gcsConfig.getBucketName();

        BlobId blobId = BlobId.of(bucketName, gcsFileName);
        return storage.delete(blobId);
    }

    private String uploadFile(MultipartFile multipartFile, String fileName) throws IOException
    {
        // Get storage object and bucket name from config
        Storage storage = gcsConfig.getGCSClient();
        String bucketName = gcsConfig.getBucketName();

        // TODO: prefix filename with folder in gcs

        try
        {
            BlobInfo blobInfo = storage.create(
                    BlobInfo.newBuilder(bucketName, fileName).build(), // get original file name
                    multipartFile.getBytes() // the file
            );

            return blobInfo.getMediaLink();
        }
        catch (IllegalStateException e)
        {
            throw new RuntimeException(e);
        }
    }
}
