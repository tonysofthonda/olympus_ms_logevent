package com.honda.olympus.ms.logevent.domain;

import lombok.Data;


@Data
public class Event 
{	
	private String source;
	private Integer status;
	private String message;
	private String file;
}
