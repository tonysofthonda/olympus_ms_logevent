package com.honda.olympus.ms.logevent;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@PropertySource(value = "file:../logevent.properties", ignoreResourceNotFound = false)
public class Application 
{

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	
	@PostConstruct
	public void init() {
		TimeZone.setDefault( TimeZone.getTimeZone("America/Mexico_City") );
	}

}
