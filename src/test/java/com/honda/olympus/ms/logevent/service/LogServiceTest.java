package com.honda.olympus.ms.logevent.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.web.server.ResponseStatusException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.honda.olympus.ms.logevent.domain.Event;
import com.honda.olympus.ms.logevent.handler.LogHandler;


@TestMethodOrder(OrderAnnotation.class)
public class LogServiceTest 
{
	
	static String logPath = Paths.get(System.getProperty("java.io.tmpdir"), "/ms.logevent/logs").toString();
	static File directory = new File(logPath);
	
	static LogService logService = new LogService(logPath);
	
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
	void shouldLogEvent()
	{
		logService.logEvent(event);
		
		String line = LogHandler.getLogEntry(event);
		assertThat(path.toFile()).hasContent(line + "\n").withFailMessage(() -> "the event should be logged");
	}
	
	
	@Test
	@Order(2)
	void shouldThrowResponseStatusException()
	{
		path.toFile().setWritable(false, false);
		
		assertThatThrownBy( () -> logService.logEvent(event) )
			.isInstanceOf(ResponseStatusException.class)
			.hasMessageContaining("Error found while writing to")
			.withFailMessage(() -> "should throw ResponseStatusException with denied access to log file");
		
		path.toFile().setWritable(true, true);
	}
	
}
