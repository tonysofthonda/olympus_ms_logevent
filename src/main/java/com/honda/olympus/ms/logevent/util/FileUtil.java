package com.honda.olympus.ms.logevent.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public final class FileUtil 
{
	
	private FileUtil() { }
	
	public static boolean createDir(String dir) {
		Path path = Paths.get(dir);
		
		if (!path.toFile().exists()) { 
			log.info("# Creating dir '{}' ...", path.getFileName());
			path.toFile().mkdirs();
			return true;
		}
		
		log.info("# Dir '{}' already exists !", path.getFileName());
		return false;
	}
	
	public static void appendToFile(Path path, String line) throws IOException {
		Files.write(path, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}
	
}
