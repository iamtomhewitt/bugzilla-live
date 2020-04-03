package com.bugzillalive.exception;

public class ConfigNotFoundException extends Exception {
	private static final String BASE_MESSAGE = "No config recorded in the database. Perhaps you need to save some config first?";

	public ConfigNotFoundException() {
		super(BASE_MESSAGE);
	}

	public ConfigNotFoundException(String message) {
		super(String.format("%s Additional information: %s", BASE_MESSAGE, message));
	}
}