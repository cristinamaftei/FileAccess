package com.my.projects.fileAccess;

import com.my.projects.fileAccess.controller.FileAccessController;
import com.my.projects.fileAccess.managemet.FileAccessService;
import com.my.projects.fileAccess.validation.FileExists;
import com.my.projects.fileAccess.validation.FileNameValid;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileAccessApplicationTests {

    @Autowired
    FileAccessController fileAccessController;
    @Autowired
    FileAccessService fileAccessService;
    @Autowired
    FileNameValid fileNameValid;
    @Autowired
    FileExists fileExists;

    private String fileName = "newfile89_";
    private String fileNameNotValid = "!457jub-";
    private String fileContent = "File content for the test";
    private String newContent = " append new text";
    private String testFileName = "fileTest";
    private String otherFileName = "testName";
    private String filePattern = "file";
    @Value("${fileAccess.path}")
    private Path generalPath;

    @After
    public void deleteCreatedFile() throws IOException {
        if (fileExists.test(fileName)) {
            fileAccessService.delete(fileName);
        }
        if (fileExists.test(testFileName)) {
            fileAccessService.delete(testFileName);
        }
        if (fileExists.test(otherFileName)) {
            fileAccessService.delete(otherFileName);
        }
    }

    @Test
    public void checkRegexPatternForFileName() {
        boolean isValid = fileNameValid.test(fileName);
        assertEquals(true, isValid);
    }

    @Test
    public void checkRegexPatternForFileNameNotValid() {
        boolean isValid = fileNameValid.test(fileNameNotValid);
        assertEquals(false, isValid);
    }

    @Test
    public void checkFileExists() throws IOException {
        //arrange
        fileAccessService.create(testFileName, fileContent.getBytes());

        //act
        boolean isCreated = fileExists.test(testFileName);

        //assert
        assertEquals(true, isCreated);
    }

    @Test
    public void checkFileCanBeCreated() throws IOException {
        boolean isCreated = fileAccessService.create(testFileName, fileContent.getBytes());
        assertEquals(true, isCreated);
    }

    @Test
    public void checkFileContentWhenRead() throws IOException {
        //arrange
        fileAccessService.create(testFileName, fileContent.getBytes());
        //act
        String content = fileAccessService.read(testFileName);
        //assert
        assertEquals("File content for the test",content );
    }

    @Test
    public void checkContentWhenTheFileIsUpdated() throws IOException {
        //arrange
        fileAccessService.create(testFileName, fileContent.getBytes());

        //act
        fileAccessService.update(testFileName, newContent);

        //assert
        assertEquals("File content for the test append new text", fileContent + newContent);
    }

    @Test
    public void checkSize()throws IOException{
        //arrange
        fileAccessService.create(testFileName, fileContent.getBytes());
        fileAccessService.create(fileName, fileContent.getBytes());

        //act
       long actualSize = fileAccessService.getSize();

       //assert
          assertEquals(2,actualSize);
    }

    @Test
    public void checkListOfFiles()throws IOException{
        //arrange
        fileAccessService.create(testFileName, fileContent.getBytes());
        fileAccessService.create(fileName, fileContent.getBytes());
        fileAccessService.create(otherFileName, fileContent.getBytes());

        //act
        String[]expectedList={testFileName,fileName};
        String expectedFiles = String.join(", ", expectedList);
        String actualList = fileAccessService.getFilesList(filePattern);

        //assert
        assertEquals(expectedFiles,actualList);
    }

}
