package com.honda.olympus.ms.logevent.controller;

import static java.util.stream.Collectors.joining;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.honda.olympus.ms.logevent.domain.Event;
import com.honda.olympus.ms.logevent.service.LogService;


@RestController
public class LogController 
{
		
	@Autowired
	private LogService logService;
	
	
	@PostMapping(path = "/event", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> logEvent(@Valid @RequestBody Event event, Errors errors) 
	{
		handleBadRequest(errors);
		this.logService.logEvent(event);
		
		return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
	}
	
	
	
	public void handleBadRequest(Errors errors) {
		if (errors.hasErrors()) {
			String messages = errors.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(joining("; "));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, messages);
		}
	}
	
}
