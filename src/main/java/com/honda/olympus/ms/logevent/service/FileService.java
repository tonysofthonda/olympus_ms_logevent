package com.honda.olympus.ms.logevent.service;

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
import org.springframework.stereotype.Service;

import com.honda.olympus.ms.logevent.domain.Event;


@Service
public class FileService 
{
	
	private static final String SEPARATOR = ",";
	private static final String NEW_LINE = "\n";
	private static final String PREFIX = "event";
	private static final String FILE_EXTENSION = ".txt";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "hh:mm:ss";
	
	
	@Value("${logpath}")
	private String logPath;
	
	
	public void appendToFile(Path path, String line) throws IOException {
		Files.write(path, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}
	
	
	public String buildEntryLine(Event event) 
	{
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
	
	
	public Path buildPathname() 
	{
		StringBuilder sb = new StringBuilder()
			.append(PREFIX)
			.append(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
		    .append(FILE_EXTENSION);
		
		return Paths.get(logPath, sb.toString());
	}

}
