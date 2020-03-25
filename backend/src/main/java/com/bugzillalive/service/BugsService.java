package com.bugzillalive.service;

import com.bugzillalive.model.Bug;
import com.bugzillalive.model.Comment;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BugsService {

	private RestTemplate restTemplate = new RestTemplate();

	public List<Bug> getBugsByNumbers(String bugzillaUrl, String numbers) {
		for (String number : numbers.split(",")) {
			bugzillaUrl += number + ",";
		}

		JSONObject response = new JSONObject(restTemplate.getForObject(bugzillaUrl, String.class));
		List<Bug> bugs = new ArrayList<>();
		JSONArray array = response.getJSONArray("bugs");

		for (int i = 0; i < array.length(); i++) {
			JSONObject b = array.getJSONObject(i);
			bugs.add(Bug.toBug(b));
		}

		return bugs;
	}

	public List<Bug> getBugsByUsername(String url, String username) {
		JSONObject response = new JSONObject(restTemplate.getForObject(url + username, String.class));
		List<Bug> bugs = new ArrayList<>();
		JSONArray array = response.getJSONArray("bugs");

		for (int i = 0; i < array.length(); i++) {
			JSONObject b = array.getJSONObject(i);
			bugs.add(Bug.toBug(b));
		}

		return bugs;
	}

	public List<Comment> getCommentsForBug(String url, String number) {
		List<Comment> comments = new ArrayList<>();
		JSONObject response = new JSONObject(restTemplate.getForObject(url, String.class));

		JSONArray array = response
			.getJSONObject("bugs")
			.getJSONObject(number)
			.getJSONArray("comments");

		for (int i = 0; i < array.length(); i++) {
			JSONObject c = array.getJSONObject(i);
			comments.add(Comment.toComment(c));
		}

		return comments;
	}
}
