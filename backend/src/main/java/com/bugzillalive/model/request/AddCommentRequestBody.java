package com.bugzillalive.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCommentRequestBody {
	private String comment;
	private String apiKey;
}
