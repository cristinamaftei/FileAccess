package com.my.projects.fileAccess.controller;

import com.my.projects.fileAccess.FileAccessApplication;
import com.my.projects.fileAccess.managemet.FileAccessService;
import com.my.projects.fileAccess.validation.FileAccessValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController("/api")
public class FileAccessController {
    private static final Logger logger = LoggerFactory.getLogger(FileAccessApplication.class);

    @Autowired
    FileAccessService fileAccessService;

    @Autowired
    FileAccessValidation fileAccessValidation;

    @GetMapping("/")
    public String home() {
        return "Welcome!";
    }

    /**
     * Creates a file in the storage
     * @param fileName name of the file to be created
     * @param fileContent content of the file to be created
     * @return HTTP status 200 when the file is created, 500 if there was a server error, 400 if the request is not correct
     *
     */
    @PostMapping("/create")
    public ResponseEntity<String> createFile(@RequestParam String fileName, @RequestBody String fileContent) {

        if (fileAccessValidation.isValid(fileName)) {
            try {
                fileAccessService.create(fileName, fileContent.getBytes());
                return ResponseEntity.status(HttpStatus.OK)
                        .body("File created");
            } catch (IOException e) {
                logger.error("File was not created");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("There was a problem creating the file");
            }
        } else {
            return ResponseEntity.badRequest()
                    .body("Please review your request");
        }
    }

    /**
     * Returns the content of the file identified by the provided fileName parameter
     * @param fileName name of the file to be read
     * @return HTTP status 200 when the content can be read, 500 if there was a server error, 400 if the request is not correct
     */
    @GetMapping("/read")
    public ResponseEntity<String> readFile(@RequestParam String fileName) {
        String content = null;
        if (fileAccessValidation.isValid(fileName)) {
            try {
                content = fileAccessService.read(fileName);
            } catch (IOException e) {
                logger.error("Can't access the file");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("There was a problem accessing the file");
            }
        }else {
            return ResponseEntity.badRequest()
                    .body("Not a valid file name or the file doesn't exist");
        }
        return ResponseEntity.ok()
                .body(content);
    }

    /**
     * Updates the content of the file identified by the fileName parameter, appending the new content to the existing one
     * @param fileName name of the file to be updated
     * @param newContent additional content to be appended to the existing file content
     * @return HTTP status 200 when the file's content is updated, 500 if there was a server error, 400 if the request is not correct
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateFile(@RequestParam String fileName, @RequestBody String newContent) {

        if (fileAccessValidation.isValid(fileName)) {
            try {
                fileAccessService.update(fileName, newContent);

            } catch (IOException e) {
                logger.error("Can't update the file");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("There was a problem accessing the file");
            }
        } else {
            return ResponseEntity.badRequest()
                    .body("Not a valid file name or the file doesn't exist");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("File updated");
    }

    /**
     * Deletes a file in the storage identified by the fileName parameter
     * @param fileName name of the file to be deleted
     * @return HTTP status 200 when the file is deleted, 500 if there was a server error, 400 if the request is not correct
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String fileName) {
        if (fileAccessValidation.isValid(fileName)) {
            try {
                fileAccessService.delete(fileName);
            } catch (IOException e) {
                logger.error("Can't delete the file");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("There was a problem accessing the file");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Not a valid file name or the file doesn't exist");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("File deleted");
    }

    /**
     * Returns the files in the storage filtered by a provided file name pattern
     * @param namePattern  pattern used to filter files
     * @return HTTP status 200 when the list can be created, 500 if there was a server error
     */
    @GetMapping("/enumeration")
    public ResponseEntity<String> listAllFiles(@RequestParam String namePattern) {
        String filesList = null;
        try {
            filesList=fileAccessService.getFilesList(namePattern);
        } catch (IOException e) {
            logger.error("Can't list the list");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There was a problem listing the files");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(filesList);
    }

    /**
     * Returns the number of files available in the storage
     * @return HTTP status 200 and the number of files stored or HTTP status 500 when error is encountered
     */
    @GetMapping("/size")
    public ResponseEntity<String> countNumberOfFiles() {
        long count =0;
        try {
           count =  fileAccessService.getSize();
        } catch (IOException e) {
            logger.error("Can't list the list");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occur, the files can't be listed");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body( String.valueOf(count));
    }
}
