package com.honda.olympus.ms.logevent.util;

import static java.util.stream.Collectors.joining;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;


@Component
public class HttpHandler 
{
	
	private static final String SEMICOLON = "; ";
	
	public void handleBadRequest(Errors errors) {
		if (errors.hasErrors()) {
			String messages = errors.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(joining(SEMICOLON));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, messages);
		}
	}
	
}
