package com.himansh.targetlabs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bobby on 6/2/2017.
 */

@RestController
public class RestFileController {

    private static String UPLOADED_FOLDER = "D://Storage//";

    private final Logger logger = LoggerFactory.getLogger(RestFileController.class);

    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadFileMulti(
            @RequestParam("uploadFile") MultipartFile[] uploadfile) {

        logger.debug("File uploaded!");

        String uploadedFileName = Arrays.stream(uploadfile).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

            saveUploadedFiles(Arrays.asList(uploadfile));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - "
                + uploadedFileName, HttpStatus.OK);
    }


    /*Save files*/
    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        Path storageFolder = Paths.get(UPLOADED_FOLDER);
        /* Check if folder exists */
        if (Files.notExists(storageFolder)) {
            Files.createDirectory(storageFolder);
            logger.debug("Storage folder created!");
        }
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
        }

    }
}