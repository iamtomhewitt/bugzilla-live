package com.bugzillalive.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Comment {
	private String text;
	private String time;
	private String creator;
}
