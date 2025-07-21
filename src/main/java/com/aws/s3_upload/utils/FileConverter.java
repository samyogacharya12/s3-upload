package com.aws.s3_upload.utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@UtilityClass
public class FileConverter {


    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File convFile = File.createTempFile("upload-", multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipartFile.getBytes());
        }
        return convFile;
    }
}
