package com.honda.olympus.ms.logevent.handler;

import static java.util.stream.Collectors.joining;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class HttpHandler 
{	
	
	public void handleBadRequest(Errors errors) {
		if (errors.hasErrors()) {
			String messages = errors.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(joining("; "));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, messages);
		}
	}
	
	
	public void handleBadResponse(String message, Exception exception) {
		log.error("### {}", message, exception);
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}
	
}
