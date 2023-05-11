package com.honda.olympus.ms.logevent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honda.olympus.ms.logevent.domain.Event;
import com.honda.olympus.ms.logevent.service.EventService;


@RestController
@RequestMapping("/event")
public class EventController 
{
	
	@Autowired
	private EventService eventService;
	
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveEvent(@RequestBody Event event) 
	{
		eventService.saveEvent(event);
		return ResponseEntity.ok("Done.");
	}
	
}
