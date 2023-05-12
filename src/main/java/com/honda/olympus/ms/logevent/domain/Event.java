package com.honda.olympus.ms.logevent.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
public class Event 
{	
	@NotBlank(message = "{event.source}")
	private String source;
	
	@NotNull(message = "{event.status}")
	@Min(value = 0, message = "{event.status}")
	@Max(value = Short.MAX_VALUE, message = "{event.status}")
	private Integer status;
	
	@NotBlank(message = "{event.msg}")
	private String msg;
	
	private String file;
}
