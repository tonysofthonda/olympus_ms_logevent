package com.honda.olympus.ms.logevent.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.honda.olympus.ms.logevent.domain.Event;
import com.honda.olympus.ms.logevent.handler.HttpHandler;
import com.honda.olympus.ms.logevent.handler.LogHandler;

import lombok.Setter;


@Service
public class LogService 
{
	
	private String logPath;
	private LogHandler logHandler;
	
	@Setter
	@Autowired
	private HttpHandler httpHandler;
	
	
	public LogService(@Value("${logpath}") String logpath, @Autowired LogHandler logHandler) {
		logPath = logpath;
		this.logHandler = logHandler;
		
		logHandler.createDirectory(logPath);
	}
	
	
	public void logEvent(Event event) {
		try {
			Path path = Paths.get(logPath, logHandler.getFileName());
			String line = logHandler.getLogEntry(event);
			
			logHandler.appendToFile(path, line);
		}
		catch(Exception exception) {
			httpHandler.handleBadResponse("Error found while writing to: " + logPath, exception);
		}
	}
	
}
