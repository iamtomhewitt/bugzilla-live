package com.bugzillalive.exception;

public class ListAlreadyExistsException extends Exception {
	public ListAlreadyExistsException(String listName) {
		super(String.format("Cannot save list, '%s' already exists", listName));
	}
}