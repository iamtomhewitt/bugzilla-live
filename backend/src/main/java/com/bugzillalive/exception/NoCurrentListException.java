package com.bugzillalive.exception;

public class NoCurrentListException extends Exception {
	public NoCurrentListException() {
		super("There is no current list in use");
	}
}
