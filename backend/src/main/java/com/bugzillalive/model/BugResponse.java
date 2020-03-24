package com.bugzillalive.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BugResponse {
	private List<Bug> bugs;
}
