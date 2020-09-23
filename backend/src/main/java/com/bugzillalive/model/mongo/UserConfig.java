package com.bugzillalive.model.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "config")
@Builder
public class UserConfig {

	@Id
	private String id;
	private String bugzillaUrl;

	public UserConfig() {
	}

	public UserConfig(String bugzillaUrl) {
		this.bugzillaUrl = bugzillaUrl;
	}

	public UserConfig(String id, String bugzillaUrl) {
		this.id = id;
		this.bugzillaUrl = bugzillaUrl;
	}

	@Override
	public String toString() {
		return String.format("id=%s, bugzillaUrl=%s", id, bugzillaUrl);
	}
}
