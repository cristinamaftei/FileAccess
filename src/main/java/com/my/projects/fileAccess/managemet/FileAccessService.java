package com.my.projects.fileAccess.managemet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileAccessService {

	@Value("${fileAccess.path}")
	private Path generalPath;

	public boolean create(String fileName, byte[] fileContent) throws IOException {
		Path filePath = generalPath.resolve(fileName);
		Files.createFile(filePath);
		Files.write(filePath, fileContent);

		if (Files.exists(filePath)) {
			return true;
		} else {
			return false;
		}
	}

	@Cacheable("fileName")
	public String read(String fileName) throws IOException {
		Path filePath = generalPath.resolve(fileName);
		Stream<String>lines = Files.lines(filePath);
		String data = lines.collect(Collectors.joining("\n"));
		lines.close();
		return data;
	}

	public boolean update(String fileName, String newContent) throws IOException {
        Path filePath = generalPath.resolve(fileName);
		Files.write(filePath, newContent.getBytes(), StandardOpenOption.APPEND);
		return true;
	}

	public boolean delete(String fileName) throws IOException {
        Path filePath = generalPath.resolve(fileName);
		Files.delete(filePath);
		if (!Files.exists(filePath)) {
			return true;
		} else {
			return false;
		}
	}

	public String getFilesList(String namePattern) throws IOException {
		List<String> filesName = new ArrayList<>();
		File dir = new File(generalPath.toString());
		File[] files = dir.listFiles((d, name) -> name.contains(namePattern));
		for(File file : files){
			if (file.isFile())
			filesName.add(file.getName());
			}
		String allFilesName = String.join(", ", filesName);
		return allFilesName;
		}


	public long getSize() throws IOException {
		return Files.list(generalPath).count()-1;
	}

}
