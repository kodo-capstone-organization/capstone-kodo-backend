package com.spring.kodo.config;

import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.storage.Storage;

@Configuration
public class GCSConfig
{
    @Value("${gcs.bucket.name}")
    private String bucketName;

    public Storage getGCSClient()
    {
        // Get service by env var GOOGLE_APPLICATION_CREDENTIALS (stores path to json key file)
        Storage storage = StorageOptions.getDefaultInstance().getService();
        return storage;
    }

    public String getBucketName()
    {
        return bucketName;
    }
}
