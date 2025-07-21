package com.aws.s3_upload.controller;

import com.aws.s3_upload.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class StorageController {

    @Autowired
    private S3Service service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam(value = "fileName") String fileName) {
        return new ResponseEntity<>(service.downloadFile(fileName), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam(value = "fileName") String fileName) {
        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
    }


}
