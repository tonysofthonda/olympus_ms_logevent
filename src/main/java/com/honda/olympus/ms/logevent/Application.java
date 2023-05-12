package com.honda.olympus.ms.logevent;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;


@SpringBootApplication
public class Application 
{

	public static void main(String[] args) {
		System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "dev");
		SpringApplication.run(Application.class, args);
	}
	
	
	@PostConstruct
	public void init() {
		TimeZone.setDefault( TimeZone.getTimeZone("America/Mexico_City") );
	}

}
