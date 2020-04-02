package com.bugzillalive.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeStatusRequestBody {
	private String status;
	private String resolution;
	private String apiKey;
}
