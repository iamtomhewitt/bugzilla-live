package com.bugzillalive.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
