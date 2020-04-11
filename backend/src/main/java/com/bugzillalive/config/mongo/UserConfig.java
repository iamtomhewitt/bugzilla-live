package com.bugzillalive.config.mongo;

import com.bugzillalive.model.BugList;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "userConfig")
public class UserConfig {

	@Id
	private String id;
	private String bugzillaUrl;
	private List<BugList> lists;
	private BugList currentList;

	public UserConfig() {
	}

	public UserConfig(String bugzillaUrl, List<BugList> lists) {
		this.bugzillaUrl = bugzillaUrl;
		this.lists = lists;
	}

	/**
	 * Only use for testing.
	 */
	public UserConfig(String bugzillaUrl, List<BugList> lists, String id) {
		this.bugzillaUrl = bugzillaUrl;
		this.lists = lists;
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("id=%s, bugzillaUrl=%s, lists=%s", id, bugzillaUrl, lists);
	}
}
