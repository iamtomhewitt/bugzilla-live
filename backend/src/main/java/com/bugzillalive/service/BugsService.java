package com.bugzillalive.service;

import com.bugzillalive.model.*;
import com.bugzillalive.model.request.AddCommentRequestBody;
import com.bugzillalive.model.request.ChangeStatusRequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BugsService {

	@Autowired
	private RestTemplate restTemplate;

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

	public List<Attachment> getAttachmentsForBug(String url, String number) {
		List<Attachment> attachments = new ArrayList<>();
		JSONObject response = new JSONObject(restTemplate.getForObject(url, String.class));

		JSONArray array = response
			.getJSONObject("bugs")
			.getJSONArray(number);

		for (int i = 0; i < array.length(); i++) {
			JSONObject a = array.getJSONObject(i);
			attachments.add(Attachment.toAttachment(a));
		}

		return attachments;
	}

	public ResponseEntity<String> addCommentToBug(String url, AddCommentRequestBody body) {
		String jsonBody = "{\"comment\": \"" + body.getComment() + "\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-BUGZILLA-API-KEY", body.getApiKey());

		HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
		return restTemplate.postForEntity(url, entity, String.class);
	}

	public ResponseEntity<String> changeBugStatus(String url, ChangeStatusRequestBody body) {
		String jsonBody = body.getStatus().equals("RESOLVED") ?
			"{\n" +
				"\t\"status\" : \"" + body.getStatus() + "\",\n" +
				"\t\"resolution\": \"" + body.getResolution() + "\"\n" +
				"}"
			:
			"{\n" +
				"\t\"status\" : \"" + body.getStatus() + "\"\n" +
				"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-BUGZILLA-API-KEY", body.getApiKey());

		HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
		return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
	}
}
