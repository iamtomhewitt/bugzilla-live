package com.bugzillalive.model;

import lombok.Data;

@Data
public class BugList {
	private String name;
	private String content;

	public BugList(String name, String content) {
		this.name = name;
		this.content = content;
	}
}
