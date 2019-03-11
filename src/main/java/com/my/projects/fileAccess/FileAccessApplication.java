package com.my.projects.fileAccess;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FileAccessApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileAccessApplication.class, args);
	}
}
