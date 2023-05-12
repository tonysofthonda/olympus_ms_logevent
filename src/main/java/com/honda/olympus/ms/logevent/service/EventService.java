package com.honda.olympus.ms.logevent.service;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.honda.olympus.ms.logevent.domain.Event;
import com.honda.olympus.ms.logevent.util.FileHandler;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class EventService 
{
	
	@Value("${logpath}")
	private String logPath;
	
	@Autowired
	private FileHandler fileHandler;
	
	
	public void saveEvent(Event event) 
	{
		Path pathname = fileHandler.buildPathname(logPath);
		String line = fileHandler.buildEntryLine(event);
		
		try {
			fileHandler.appendToFile(pathname, line);
		}
		catch(Exception exception) {
			String message = "Error found while accessing: " + pathname;
			
			log.error("### {}", message, exception);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
		}
	}	
	
}
