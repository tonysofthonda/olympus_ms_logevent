package com.honda.olympus.ms.logevent.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public final class FileUtil 
{
	
	private FileUtil() { }
	
	
	public static boolean createDir(String path) {
		File dir = new File(path);
		boolean createDir = !dir.exists();
		
		if (createDir) { 
			log.info("# Creating dir in path ...");
			dir.mkdirs();
		}
		else {
			log.info("# Dir in path already exists !");
		}
		
	    return createDir;
	}
	
	
	public static void appendToFile(Path path, String line) throws IOException {
		Files.write(path, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}
	
}
