package com.zenith.exception;

public class InvalidRequestParameters extends Exception{
	
	private static final long serialVersionUID = 1L;

	public InvalidRequestParameters(String errorMessage) {
		super(errorMessage);
	}
}
