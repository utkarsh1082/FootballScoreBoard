package com.zenith.exception;

public class CustomErrorException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public CustomErrorException(String errorMessage) {
		super(errorMessage);
	}
}
