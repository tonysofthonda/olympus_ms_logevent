package com.honda.olympus.ms.logevent.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.honda.olympus.ms.logevent.domain.Event;
import com.honda.olympus.ms.logevent.handler.HttpHandler;
import com.honda.olympus.ms.logevent.service.LogService;


@RestController
public class LogController 
{
		
	@Autowired
	private LogService logService;
	
	
	@PostMapping(path = "/event", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> logEvent(@Valid @RequestBody Event event, Errors errors) 
	{
		HttpHandler.handleBadRequest(errors);
		logService.logEvent(event);
		
		return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
	}
	
}
