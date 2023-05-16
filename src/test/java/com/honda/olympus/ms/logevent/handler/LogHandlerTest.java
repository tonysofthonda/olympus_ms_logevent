package com.honda.olympus.ms.logevent.handler;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.honda.olympus.ms.logevent.domain.Event;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@TestMethodOrder(OrderAnnotation.class)
public class LogHandlerTest 
{
	
	static final String SEP = ",";
	
	static String logPath = Paths.get(System.getProperty("java.io.tmpdir"), "/ms.logevent/logs").toString(); 
	static File directory = new File(logPath);
	
	static Event event = new Event("ms.monitor", 400, "Bad Request", "xfiles.zip");
	static Path path = Paths.get(logPath, LogHandler.getFileName());
	
	
	@AfterAll
	static void afterAll() {
		path.toFile().delete();               // delete log file   
		directory.delete();                   // delete main folder (logs)
		directory.getParentFile().delete();   // delete container folder (ms.logevent)
	}
	
	
	@Test
	@Order(1)
	void shouldCreateDirectory() 
	{
		assertThat(directory).doesNotExist().withFailMessage(() -> "the directory should not exist");
		boolean actual = LogHandler.createDirectory(logPath);
		
		assertThat(directory).exists().withFailMessage(() -> "the directory should exist");
		assertThat(actual).isTrue().withFailMessage(() -> "the directory should be created");
	}
	
	
	@Test
	@Order(2)
	void shouldNotCreateDirectory() 
	{
		assertThat(directory).exists().withFailMessage(() -> "the directory should exist");
		boolean actual = LogHandler.createDirectory(logPath);
		
		assertThat(actual).isFalse().withFailMessage(() -> "the directory should not be created");
	}
	
	
	@Test
	@Order(3)
	void shouldReturnFileName()
	{
		String expected = "event" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".txt";
		String actual = LogHandler.getFileName();
		
		assertThat(actual).isEqualTo(expected).withFailMessage(() -> "the file name should be returned");
		log.info(">> FileName: {}", actual);
	}
	
	
	@Test 
	@Order(4)
	void shouldReturnLogEntry()
	{
		LocalDateTime ldt = LocalDateTime.now();
		String expected = ldt.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + SEP +  
			ldt.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + SEP + 
			event.getSource() + SEP + 
			event.getStatus() + SEP + 
			event.getMsg() + SEP + 
			event.getFile();
		
		String actual = LogHandler.getLogEntry(event);
		
		assertThat(actual).isEqualTo(expected).withFailMessage(() -> "a well formed log entry should be returned");
		log.info(">> LogEntry: {}", actual);
	}
	
	
	@Test
	@Order(5)
	void shouldWriteEventToLog() throws IOException
	{
		String line = LogHandler.getLogEntry(event);
		
		LogHandler.appendToFile(path, line);
		assertThat(path.toFile()).hasContent(line + "\n").withFailMessage(() -> "a log entry should be in the log file");
	}

}
