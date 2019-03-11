package com.my.projects.fileAccess;

import com.my.projects.fileAccess.controller.FileAccessErrorController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FileAccessErrorApplicationTests {

    @Autowired
    protected WebApplicationContext wac;

    MockMvc mockMvc;
    @Autowired
    FileAccessErrorController fileAccessErrorController;
    @Value("${server.port}")
    private int portNo;

    @Before
    public void setup(){
        this.mockMvc = standaloneSetup(this.fileAccessErrorController).build();
    }

    @Test
    public void testFileAccessErrorController() throws Exception{
        mockMvc.perform(get("/error"))
                .andDo(print())
                .andExpect(content().string("Please review your request"));
    }
}
