package com.himansh.targetlabs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bobby on 6/2/2017.
 */

@RestController
public class RestFileController {

    private static String UPLOADED_FOLDER = "C://Storage//";

    private final Logger logger = LoggerFactory.getLogger(RestFileController.class);

    @PostMapping(name = "/api/upload",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadFile(
            @RequestParam("uploadFile") MultipartFile[] uploadFile) {

        BasicFileAttributes attr = null;
        Path pathRetrieve = null;
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        String metaData = null;
        String nameWithoutExtension = null;

        String uploadedFileName = Arrays.stream(uploadFile).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity<>("please select a file!", HttpStatus.OK);
        }

        try {
            saveUploadedFiles(Arrays.asList(uploadFile));
            pathRetrieve = Paths.get(UPLOADED_FOLDER + uploadedFileName);
            attr = Files.readAttributes(pathRetrieve, BasicFileAttributes.class);
            nameWithoutExtension = uploadedFileName.substring(0, uploadedFileName.length() - 4);
            fileWriter = new FileWriter(UPLOADED_FOLDER + nameWithoutExtension + "-retrieved_metadata.txt");
            bufferedWriter = new BufferedWriter(fileWriter);
            metaData = ("\nSuccessfully uploaded - " + uploadedFileName
                    + "\tto " + UPLOADED_FOLDER
                    + "\tcreationTime: " + attr.creationTime().toString()
                    + "\tlastAccessTime: " + attr.lastAccessTime().toString()
                    + "\tlastModifiedTime: " + attr.lastModifiedTime().toString()
                    + "\tisDirectory: " + attr.isDirectory()
                    + "\tisRegularFile: " + attr.isRegularFile()
                    + "\tisSymbolicLink: " + attr.isSymbolicLink()
                    + "\tsize: " + attr.size() + " Bytes").toString();
            bufferedWriter.write(metaData);
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("\nSuccessfully uploaded - " + uploadedFileName
                + "\tto " + UPLOADED_FOLDER
                + "\tcreationTime: " + attr.creationTime().toString()
                + "\tlastAccessTime: " + attr.lastAccessTime().toString()
                + "\tlastModifiedTime: " + attr.lastModifiedTime().toString()
                + "\tisDirectory: " + attr.isDirectory()
                + "\tisRegularFile: " + attr.isRegularFile()
                + "\tisSymbolicLink: " + attr.isSymbolicLink()
                + "\tsize: " + attr.size() + " Bytes", HttpStatus.OK);
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
            logger.debug("File uploaded!");
        }

    }
}
