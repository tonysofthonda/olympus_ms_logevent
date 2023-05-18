package com.honda.olympus.ms.logevent.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.honda.olympus.ms.logevent.domain.Event;
import com.honda.olympus.ms.logevent.util.FileUtil;
import com.honda.olympus.ms.logevent.util.HttpUtil;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class LogService 
{
	
	public static final String SEP = ",";
	public static final String NEW_LINE = "\n";
	
	public static final String PREFIX = "event";
	public static final String FILE_EXT = ".txt";
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String TIME_FORMAT = "HH:mm:ss";
	
	private String logPath;
	
	
	public LogService(@Value("${logpath}") String logPath) {
		this.logPath = logPath;
		try { 
			FileUtil.createDir(logPath);
		}
		catch (Exception exception) {
			log.error("### Unable to create: {}", logPath, exception);
			throw exception;
		}
		log.info("# logpath: {}", logPath);
	}
	
	
	public void logEvent(Event event) {
		try {
			FileUtil.appendToFile(this.getPath(), this.getLine(event));
		}
		catch(Exception exception) {
			HttpUtil.handleBadResponse("Error found while writing to: " + this.getPath(), exception);
		}
	}
	
	
	public Path getPath() {
		return Paths.get(this.logPath, this.getFileName());
	}
	
	
	public String getFileName() {
		return new StringBuilder()
			.append(PREFIX)
			.append(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
			.append(FILE_EXT).toString();
	}
	
	
	public String getLine(Event event) {
		LocalDateTime ldt = LocalDateTime.now();
		return new StringBuilder()
			.append(ldt.toLocalDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT))).append(SEP)
            .append(ldt.toLocalTime().format(DateTimeFormatter.ofPattern(TIME_FORMAT))).append(SEP)
            .append(event.getSource()).append(SEP)
            .append(event.getStatus()).append(SEP)
            .append(event.getMsg()).append(SEP)
            .append(event.getFile()).append(NEW_LINE)
			.toString();
	}
	
}
