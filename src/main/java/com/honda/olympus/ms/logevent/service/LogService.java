package com.honda.olympus.ms.logevent.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.honda.olympus.ms.logevent.domain.Event;
import com.honda.olympus.ms.logevent.handler.HttpHandler;
import com.honda.olympus.ms.logevent.handler.LogHandler;


@Service
public class LogService 
{
	
	private String logPath;
	
	
	public LogService(@Value("${logpath}") String logpath) {
		logPath = logpath;	
		try {
			LogHandler.createDirectory(logPath);
		}
		catch(Exception exception) {
			HttpHandler.handleBadResponse("Error found while accessing to: " + logPath, exception);
		}
	}
	
	
	public void logEvent(Event event) {
		try {
			Path path = Paths.get(logPath, LogHandler.getFileName());
			String line = LogHandler.getLogEntry(event);
			
			LogHandler.appendToFile(path, line);
		}
		catch(Exception exception) {
			HttpHandler.handleBadResponse("Error found while writing to: " + logPath, exception);
		}
	}
	
}
