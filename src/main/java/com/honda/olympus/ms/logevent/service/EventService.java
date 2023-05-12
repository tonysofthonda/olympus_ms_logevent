package com.honda.olympus.ms.logevent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.honda.olympus.ms.logevent.domain.Event;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class EventService 
{
	
	@Autowired
	private FileService fileService;
	
	
	public void saveEvent(Event event) {
		try {
			fileService.appendToFile(event); 
		}
		catch(Exception exception) {
			String message = "Error found while writing to: " + fileService.getLogPath();
			log.error("### {}", message, exception);
			
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
		}
	}	
	
}
