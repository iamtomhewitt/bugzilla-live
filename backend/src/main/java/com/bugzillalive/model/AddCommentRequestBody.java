package com.bugzillalive.model;

import lombok.Data;

@Data
public class AddCommentRequestBody {
	private String comment;
	private String apiKey;
}
