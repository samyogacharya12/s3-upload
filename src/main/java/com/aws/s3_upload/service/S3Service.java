package com.aws.s3_upload.service;

import com.aws.s3_upload.config.StorageConfig;
import com.aws.s3_upload.utils.FileConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.nio.file.Paths;

@Service
@Slf4j
public class S3Service {

    @Autowired
    private final S3Client s3Client;

    @Autowired
    private final StorageConfig storageConfig;

    public S3Service(S3Client s3Client, StorageConfig storageConfig){
        this.s3Client=s3Client;
        this.storageConfig=storageConfig;
    }



    public String uploadFile(MultipartFile multipartFile) {
        try {
            File file = FileConverter.convertMultipartFileToFile(multipartFile);
            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(storageConfig.getBucket())
                    .key(file.getName())
                    .build(),
                RequestBody.fromFile(Paths.get(file.getPath()))
            );
            file.deleteOnExit();
            return "File Uploaded";
        } catch (Exception exception) {
            log.error("uploadFile {} ", exception);
        }
        return "Upload Failed";
    }

    public byte[] downloadFile(String key) {
        log.info("downloadFile");
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(storageConfig.getBucket())
            .key(key)
            .build();
        ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);
        return responseBytes.asByteArray();
    }


    public String deleteFile(String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(storageConfig.getBucket())
                .key(key)
                .build();

            s3Client.deleteObject(deleteObjectRequest);

            log.info("✅ File deleted: {} ", key);
            return "File is Deleted";
        } catch (S3Exception e) {
            log.info("❌ Error deleting file from S3: {}", e.awsErrorDetails()
                .errorMessage());
            throw e;
        }
    }
}
