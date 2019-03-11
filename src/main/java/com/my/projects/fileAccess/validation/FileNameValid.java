package com.my.projects.fileAccess.validation;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FileNameValid implements Predicate<String> {

	@Override
	public boolean test(String fileName) {
		Pattern fileNamePattern = Pattern.compile("^[a-zA-Z0-9_-]{1,64}$");
		Matcher matcher = fileNamePattern.matcher(fileName);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

}
