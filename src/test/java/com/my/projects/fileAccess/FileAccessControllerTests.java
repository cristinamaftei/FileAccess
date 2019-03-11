package com.my.projects.fileAccess;


import com.my.projects.fileAccess.controller.FileAccessController;
import com.my.projects.fileAccess.managemet.FileAccessService;
import com.my.projects.fileAccess.validation.FileAccessValidation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FileAccessControllerTests {
    @Autowired
    protected WebApplicationContext wac;

    MockMvc mockMvc;

   @Autowired
   FileAccessController fileAccessController;

    @MockBean
    FileAccessService fileAccessService;

    @MockBean
    FileAccessValidation fileAccessValidation;
    private String fileContent = "File content for the test";
    private String newContent = " append new text";
    private String testFileName = "fileTest";

    @Value("${fileAccess.path}")
    private Path generalPath;

    @Value("${server.port}")
    private int portNo;


    @Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(this.fileAccessController).build();

        Path filePath = generalPath.resolve(testFileName);
        Files.createFile(filePath);
        Files.write(filePath, fileContent.getBytes());
    }

    @After
    public void clean() throws IOException {
        Path filePath = generalPath.resolve(testFileName);
        Files.delete(filePath);
    }

    @Test
    public void testHomeFileAccessController() throws Exception{
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(content().string("Welcome!"));
    }

    @Test
    public void checkSuccessResponseForReadFile() throws Exception {
        when(fileAccessValidation.isValid(testFileName)).thenReturn(true);
        when(fileAccessService.read(testFileName)).thenReturn(fileContent);

        mockMvc.perform(get("/read").contentType(MediaType.TEXT_PLAIN)
                .param("fileName", "fileTest")
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void checkBadRequestResponseForReadFile() throws Exception {
        when(fileAccessService.read(testFileName)).thenReturn(fileContent);

        mockMvc.perform(get("/read")
        ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkSuccessResponseFileIsCreated() throws Exception {
        when(fileAccessValidation.isValid(testFileName)).thenReturn(true);
        when(fileAccessService.create(testFileName, fileContent.getBytes())).thenReturn(true);

        mockMvc.perform(post("/create").contentType(MediaType.TEXT_PLAIN).content(fileContent)
                .param("fileName", "fileTest")
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void checkBadRequestResponse_whenCreatingFileBecauseOfValidation() throws Exception {
        when(fileAccessValidation.isValid(testFileName)).thenReturn(false);
        when(fileAccessService.create(testFileName, fileContent.getBytes())).thenReturn(true);

        mockMvc.perform(post("/create").contentType(MediaType.TEXT_PLAIN).content(fileContent)
                .param("fileName", "fileTest")
        ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkSuccessResponseFileIsUpdated() throws Exception {
        when(fileAccessValidation.isValid(testFileName)).thenReturn(true);
        when(fileAccessService.update(testFileName, newContent)).thenReturn(true);

        mockMvc.perform(put("/update").contentType(MediaType.TEXT_PLAIN).content(fileContent + newContent)
                .param("fileName", "fileTest")
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void checkBadRequestResponse_whenFileCantBeUpdatedBecauseOfValidation() throws Exception {
        when(fileAccessValidation.isValid(testFileName)).thenReturn(false);
        when(fileAccessService.update(testFileName, newContent)).thenReturn(true);

        mockMvc.perform(put("/update").contentType(MediaType.TEXT_PLAIN).content(fileContent)
                .param("fileName", "fileTest")
        ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkSuccessResponseFileIsDeleted() throws Exception {
        when(fileAccessValidation.isValid(testFileName)).thenReturn(true);
        when(fileAccessService.delete(testFileName)).thenReturn(true);

        mockMvc.perform(delete("/delete")
                .param("fileName", "fileTest")
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void checkBadRequestResponse_whenFileCantBeDeletedBecauseOfValidation() throws Exception {
        when(fileAccessValidation.isValid(testFileName)).thenReturn(false);
        when(fileAccessService.delete(testFileName)).thenReturn(true);

        mockMvc.perform(delete("/delete").contentType(MediaType.TEXT_PLAIN).content(fileContent)
                .param("fileName", "fileTest")
        ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkSuccessResponseForListOfFiles() throws Exception {
        when(fileAccessService.getFilesList("file")).thenReturn("fileTest");

        mockMvc.perform(get("/enumeration")
                .param("namePattern", "file")
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void checkSuccessResponseForFileListSize() throws Exception {
        when(fileAccessService.getSize()).thenReturn(1L);

        mockMvc.perform(get("/size")
        ).andDo(print())
                .andExpect(status().isOk());
    }
}
