package com.bugzillalive.model.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "lists")
@Builder
public class BugList {
	private String name;
	private String content;
	private boolean isCurrent;

	public BugList() {
	}

	public BugList(String name, String content, boolean isCurrent) {
		this.name = name;
		this.content = content;
		this.isCurrent = isCurrent;
	}
}
