package com.bugzillalive.model;

import lombok.Data;

import java.util.List;

@Data
public class BugResponse {
	private List<Bug> bugs;
}
