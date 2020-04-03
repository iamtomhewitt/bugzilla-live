package com.bugzillalive.config;

import org.springframework.data.annotation.Id;

import java.util.List;

public class UserConfig {

	@Id
	public String id;
	public String bugzillaUrl;
	public List<String> lists;

	public UserConfig() {
	}

	public UserConfig(String bugzillaUrl, List<String> lists) {
		this.bugzillaUrl = bugzillaUrl;
		this.lists = lists;
	}

	/**
	 * Only use for testing.
	 */
	public UserConfig(String bugzillaUrl, List<String> lists, String id) {
		this.bugzillaUrl = bugzillaUrl;
		this.lists = lists;
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("id=%s, bugzillaUrl=%s, lists=%s", id, bugzillaUrl, lists);
	}
}