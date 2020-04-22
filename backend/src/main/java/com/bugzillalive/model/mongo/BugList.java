package com.bugzillalive.model.mongo;

import lombok.Data;

@Data
public class BugList {
	private String name;
	private String content;
	private boolean isCurrent;

	public BugList(){}

	public BugList(String name, String content) {
		this.name = name;
		this.content = content;
		this.isCurrent = false;
	}
}
