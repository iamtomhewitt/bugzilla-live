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
public class Attachment {
	private String filename;
	private String contentType;
	private String data;

	public static Attachment toAttachment(JSONObject json) {
		return Attachment.builder()
			.filename(json.getString("file_name"))
			.contentType(json.getString("content_type"))
			.data(json.getString("data"))
			.build();
	}
}
