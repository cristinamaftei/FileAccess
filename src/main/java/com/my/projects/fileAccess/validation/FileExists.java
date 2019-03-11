package com.my.projects.fileAccess.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

@Component
public class FileExists implements Predicate<String> {
	@Value("${fileAccess.path}")
	private Path generalPath;

	@Override
	public boolean test(String fileName) {
		Path filePath = generalPath.resolve(fileName);
		if (Files.exists(filePath)) {
			return true;
		}
		return false;
	}
}
