package com.bugzillalive.model;

import lombok.Builder;
import lombok.Data;
import org.json.JSONObject;

@Data
@Builder
public class Comment {
	private String text;
	private String time;
	private String creator;

	public static Comment toComment(JSONObject json) {
		return Comment.builder()
			.time(json.getString("time"))
			.creator(json.getString("creator"))
			.text(json.getString("text"))
			.build();
	}
}
