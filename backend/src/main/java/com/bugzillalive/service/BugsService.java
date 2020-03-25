package com.bugzillalive.service;

import com.bugzillalive.model.Bug;
import com.bugzillalive.model.BugResponse;
import com.bugzillalive.model.Comment;
import com.bugzillalive.model.CommentResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
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

		ResponseEntity<BugResponse> response = restTemplate.getForEntity(bugzillaUrl, BugResponse.class);
		return response.getBody().getBugs();
	}

	public List<Bug> getBugsByUsername(String url, String username) {
		ResponseEntity<BugResponse> response = restTemplate.getForEntity(url + username, BugResponse.class);
		return response.getBody().getBugs();
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
			comments.add(Comment.builder()
				.time(c.getString("time"))
				.creator(c.getString("creator"))
				.text(c.getString("text"))
				.build());
		}

		return comments;
	}
}
