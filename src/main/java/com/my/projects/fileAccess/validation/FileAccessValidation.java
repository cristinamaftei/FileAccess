package com.my.projects.fileAccess.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileAccessValidation {
	@Autowired
	private FileExists fileExists;
	@Autowired
	private FileNameValid fileNameValid;

	public boolean isValid(String fileName) {
		return fileExists
				.and(fileNameValid)
				.test(fileName);
	}
}
