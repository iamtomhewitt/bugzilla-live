package com.bugzillalive.model.bug;

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

	public static Bug toBug(JSONObject json) {
		return Bug.builder()
			.assignedTo(json.getString("assigned_to"))
			.component(json.getString("component"))
			.id(json.get("id").toString())
			.lastUpdated(json.getString("last_change_time"))
			.product(json.getString("product"))
			.severity(json.getString("severity"))
			.status(json.getString("status"))
			.summary(json.getString("summary"))
			.build();
	}
}
