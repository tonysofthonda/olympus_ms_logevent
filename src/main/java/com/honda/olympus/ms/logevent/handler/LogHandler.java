package com.honda.olympus.ms.logevent.handler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.honda.olympus.ms.logevent.domain.Event;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class LogHandler 
{
	
	private static final String SEPARATOR = ",";
	private static final String NEW_LINE = "\n";
	
	private static final String PREFIX = "event";
	private static final String FILE_EXTENSION = ".txt";
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "HH:mm:ss";
	
	
	public boolean createDirectory(String logPath) {
		File directory = new File(logPath);
		
		if (!directory.exists()) { 
			log.info("# Creating logpath directory: {}", logPath);
			directory.mkdirs();
			return true;
		}
		
		log.info("# Logpath directory already exists");
		return false;
	}
	
	
	public String getFileName() {
		StringBuilder sb = new StringBuilder()
			.append(PREFIX)
			.append(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
			.append(FILE_EXTENSION);
		
		return sb.toString();
	}
	
	
	public String getLogEntry(Event event) {
		LocalDateTime ldt = LocalDateTime.now();
		
		return new StringBuilder()
			.append(ldt.toLocalDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
			.append(SEPARATOR)
            .append(ldt.toLocalTime().format(DateTimeFormatter.ofPattern(TIME_FORMAT)))
            .append(SEPARATOR)
            .append(event.getSource())
            .append(SEPARATOR)
            .append(event.getStatus())
            .append(SEPARATOR)
            .append(event.getMsg())
            .append(SEPARATOR)
            .append(event.getFile())
			.toString();
	}
	
	
	public void appendToFile(Path path, String line) throws IOException {
		line += NEW_LINE;
		Files.write(path, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}

}
