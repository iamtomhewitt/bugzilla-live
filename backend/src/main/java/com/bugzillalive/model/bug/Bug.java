package com.bugzillalive.model.bug;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bug {
	private String id;
	private String summary;
	private String status;
	private String product;
	private String component;
	private String severity;
	private String assignedTo;
	private String lastUpdated;

	@JsonProperty("assignedTo")
	public String getAssignedTo() {
		return this.assignedTo;
	}

	@JsonProperty("assigned_to")
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	@JsonProperty("lastUpdated")
	public String getLastUpdated() {
		return this.lastUpdated;
	}

	@JsonProperty("last_change_time")
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
