package com.honda.olympus.ms.logevent.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.honda.olympus.ms.logevent.domain.Event;
import com.honda.olympus.ms.logevent.util.StringUtil;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class FileService 
{
	
	private static final String SEPARATOR = ",";
	private static final String NEW_LINE = "\n";
	private static final String PREFIX = "event";
	private static final String FILE_EXTENSION = ".txt";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "hh:mm:ss";
	
	private String logPath;
	
	
	public FileService(@Value("${logpath:}") String logpath, @Value("${defaultLogpath}") String defaultLogpath) 
	{
		boolean externalPropertiesFound = !StringUtil.isBlank(logpath);
		
		if (externalPropertiesFound) {
			this.logPath = logpath; 
			log.info("# Accessing to logpath: {}", this.logPath);
		}
		else {
			this.logPath = defaultLogpath; 
			log.info("# Accessing to 'default' logpath: {}", this.logPath);
		}
	
		try {
			createDirectory(this.logPath);
		}
		catch(Exception exception) {
			String message = "Error found while accessing to: " + this.logPath;
			log.error("### {}", message, exception);
			
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
		}
	}
	
	
	private static void createDirectory(String logPath) {
		File baseDir = new File(logPath);
		
		if (!baseDir.exists()) { 
			log.info("# Trying to create directories in logpath: {}", logPath);
			
			baseDir.mkdirs(); 
			return;
		}
		log.info("# Directories already exists in logpath: {}", logPath);
	}
	
	
	public void appendToFile(Event event) throws IOException {
		Path path = buildPathname();
		String line = buildLogEntry(event);
		
		Files.write(path, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}
	
	
	private Path buildPathname() {
		StringBuilder sb = new StringBuilder()
			.append(PREFIX)
			.append(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
		    .append(FILE_EXTENSION);
		
		return Paths.get(logPath, sb.toString());
	}
	
	
	private String buildLogEntry(Event event) {
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
            .append(NEW_LINE)
			.toString();
	}
	
	
	public String getLogPath() { 
		return logPath; 
	} 

}
