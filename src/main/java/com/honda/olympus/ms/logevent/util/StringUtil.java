package com.honda.olympus.ms.logevent.util;


public final class StringUtil 
{
	public static final String EMPTY = "";
	
	private StringUtil() { }
	
	public static boolean isBlank(String str) {
		return str == null || str.replaceAll("\\s+", EMPTY).equals(EMPTY); 
	}
}
