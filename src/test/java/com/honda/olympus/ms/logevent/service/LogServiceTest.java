package com.honda.olympus.ms.logevent.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.honda.olympus.ms.logevent.domain.Event;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@TestMethodOrder(OrderAnnotation.class)
public class LogServiceTest 
{
	
	static final String TMP_DIR = System.getProperty("java.io.tmpdir");
	static final String DIR_PATH = "/dir_" + UUID.randomUUID();
	
	static Path logPath = Paths.get(TMP_DIR, DIR_PATH);
	static Event event = new Event("ms.monitor", 1, "SUCCESS", "file.txt");
	
	static File dir;
	static LogService logService;
	static File file;
	
	
	@BeforeAll
	static void beforeAll() {
		dir = logPath.toFile();
	}
	
	@AfterAll
	static void afterAll() {
		file.delete();
		dir.delete();
	}
	
	
	@Test
	@Order(1)
	void shouldCreateLogPathDirectory() {
		assertThat(dir).doesNotExist().withFailMessage(() -> "the directory should not exist");
		logService = new LogService(logPath.toString());
		assertThat(dir).exists().withFailMessage(() -> "the directory should exist");
	}
	
	
	@Test
	@Order(2)
	void shouldNotCreateLogPathDirectory() {
		assertThrows(NullPointerException.class, () -> new LogService(null), () -> "a null pointer exception is expected");
	}
	
	
	@Test
	@Order(3)
	void shouldReturnAWellFomedLine() {
		LocalDateTime ldt = LocalDateTime.now();
		String expected = ldt.toLocalDate().format(DateTimeFormatter.ofPattern(LogService.DATE_FORMAT)) + LogService.SEP +  
			ldt.toLocalTime().format(DateTimeFormatter.ofPattern(LogService.TIME_FORMAT)) + LogService.SEP + 
			event.getSource() + LogService.SEP + 
			event.getStatus() + LogService.SEP + 
			event.getMsg() + LogService.SEP + 
			event.getFile() + LogService.NEW_LINE;
		
		String actual = logService.getLine(event);
		
		assertThat(actual).isEqualTo(expected).withFailMessage(() -> "a well formed line should be returned");
		log.info(">> Line: {}", actual.trim());
	}
	
	
	@Test
	@Order(4)
	void shouldReturnAWellFormedFileName(){
		String expected = LogService.PREFIX + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + LogService.FILE_EXT;
		String actual = logService.getFileName();
		
		assertThat(actual).isEqualTo(expected).withFailMessage(() -> "a well formed file name should be returned");
		log.info(">> FileName: {}", actual);
	}
	
	
	@Test
	@Order(5)
	void shouldLogEvent() {
		file = logService.getPath().toFile();
		logService.logEvent(event);
		assertThat(file).hasContent(logService.getLine(event)).withFailMessage(() -> "the event should be logged");
	}
	
	
	@Test
	@Order(6)
	void shouldNotLogEvent() {
		file.setWritable(false, false);
		
		assertThatThrownBy( () -> logService.logEvent(event) )
			.isInstanceOf(ResponseStatusException.class)
			.hasMessageContaining("Error found while writing to")
			.withFailMessage(() -> "a ResponseStatusException is expected");
		
		file.setWritable(true, true);
	}
	
}
