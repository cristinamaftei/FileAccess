package com.my.projects.fileAccess.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileAccessErrorController implements ErrorController {
    @Override
    @GetMapping("/error")
    public String getErrorPath() {

        return "Please review your request";
    }
}
