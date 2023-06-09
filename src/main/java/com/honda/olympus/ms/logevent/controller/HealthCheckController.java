package com.honda.olympus.ms.logevent.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;


@Controller
public class HealthCheckController 
{
	
	@Value("${service.name}")
	private String name;
	
	@Value("${service.version}")
	private String version; 
	
	@Operation(summary = "Test service availability and version")
	@GetMapping("/health")
	public ResponseEntity<String> healthCheck() 
	{
		String message = String.format("Honda Olympus [name: %s] [version: %s] %s %s", 
			name, version, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), TimeZone.getDefault().getID() );
		
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
}
