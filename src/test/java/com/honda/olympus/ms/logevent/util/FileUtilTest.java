package com.honda.olympus.ms.logevent.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@TestMethodOrder(OrderAnnotation.class)
public class FileUtilTest 
{
	
	static final String TMP_DIR = System.getProperty("java.io.tmpdir");
	static final String DIR_PATH = "/dir_" + UUID.randomUUID();
	static final String FILE_NAME = "file.txt";
	static final String LINE = "line";
	
	static Path dirPath;
	static File dir;
	
	static Path filePath;
	static File file;
	
	
	@BeforeAll
	static void beforeAll() {
		dirPath = Paths.get(TMP_DIR, DIR_PATH);
		dir = dirPath.toFile();
		
		filePath = Paths.get(TMP_DIR, DIR_PATH, FILE_NAME);
		file = filePath.toFile();
	}
	
	
	@AfterAll
	static void afterAll() {
		file.delete();
		dir.delete();
	}
	
	
	@Test
	@Order(1)
	void shouldCreateDir() {
		assertThat(dir).doesNotExist().withFailMessage(() -> "the directory should not exist");
		
		boolean actual = FileUtil.createDir(dirPath.toString());
		
		assertThat(dir).exists().withFailMessage(() -> "the directory should exist");
		assertThat(actual).isTrue().withFailMessage(() -> "the directory should be created");
	}
	
	
	@Test
	@Order(2)
	void shouldNotCreateDir() {
		assertThat(dir).exists().withFailMessage(() -> "the directory should exist");
		
		boolean actual = FileUtil.createDir(dirPath.toString());
		
		assertThat(actual).isFalse().withFailMessage(() -> "the directory should not be created");
	}
	
	
	@Test
	@Order(3)
	void shouldWriteLineToFile() throws IOException {
		FileUtil.appendToFile(filePath, LINE);
		
		assertThat(file).hasContent(LINE).withFailMessage(() -> "the file should contain a line");
	}

}
