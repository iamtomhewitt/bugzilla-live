package com.bugzillalive.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Bug {
	private String id;
	private String summary;
	private String status;
	private String product;
	private String component;
	private String severity;

	@JsonProperty("assigned_to")
	private String assignedTo;

	@JsonProperty("last_change_time")
	private String lastUpdated;
}
