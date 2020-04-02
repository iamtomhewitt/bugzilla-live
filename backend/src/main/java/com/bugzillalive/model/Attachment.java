package com.bugzillalive.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.json.JSONObject;

@Data
@Builder
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
