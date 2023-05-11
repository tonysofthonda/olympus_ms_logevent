package com.honda.olympus.ms.logevent.service;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.honda.olympus.ms.logevent.domain.Event;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class EventService 
{
	
	@Autowired
	private FileService fileService;
	
	
	public void saveEvent(Event event) 
	{
		Path pathname = fileService.buildPathname(event);
		String line = fileService.buildEntryLine(event);
		
		try {
			fileService.appendToFile(pathname, line);
		}
		catch(IOException ioe) {
			log.error(ioe.toString());
		}
	}	
	
}
