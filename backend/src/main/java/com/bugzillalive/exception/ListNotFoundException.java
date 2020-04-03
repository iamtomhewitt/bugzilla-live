package com.bugzillalive.exception;

public class ListNotFoundException extends Exception {
	public ListNotFoundException(String listName) {
		super(String.format("%s could not be found", listName));
	}
}
