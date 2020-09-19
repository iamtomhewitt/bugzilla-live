package com.bugzillalive.service;

import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.model.bug.Attachment;
import com.bugzillalive.model.bug.Bug;
import com.bugzillalive.model.bug.Comment;
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

	private ConfigService configService;

	@Autowired
	public BugsService(ConfigService configService) {
		this.configService = configService;
	}

	public List<Bug> getBugsByNumbers(String numbers) throws ConfigNotFoundException {
		String url = this.configService.getBugzillaUrl() + "/rest/bug?id=" + numbers;
		JSONObject response = new JSONObject(restTemplate.getForObject(url, String.class));
		return toBugs((response.getJSONArray("bugs")));
	}

	public List<Bug> getBugsByUsername(String username) throws ConfigNotFoundException {
		String url = configService.getBugzillaUrl() + "/rest/bug?assigned_to=" + username;
		JSONObject response = new JSONObject(restTemplate.getForObject(url, String.class));
		return toBugs((response.getJSONArray("bugs")));
	}

	private List<Bug> toBugs(JSONArray array) {
		List<Bug> bugs = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject b = array.getJSONObject(i);
			bugs.add(Bug.toBug(b));
		}
		return bugs;
	}

	public List<Comment> getCommentsForBug(String number) throws ConfigNotFoundException {
		String url = configService.getBugzillaUrl() + "/rest/bug/" + number + "/comment";
		JSONObject response = new JSONObject(restTemplate.getForObject(url, String.class));
		List<Comment> comments = new ArrayList<>();

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

	public List<Attachment> getAttachmentsForBug(String number) throws ConfigNotFoundException {
		String url = configService.getBugzillaUrl() + "/rest/bug/" + number + "/attachment";
		JSONObject response = new JSONObject(restTemplate.getForObject(url, String.class));
		List<Attachment> attachments = new ArrayList<>();

		JSONArray array = response
			.getJSONObject("bugs")
			.getJSONArray(number);

		for (int i = 0; i < array.length(); i++) {
			JSONObject a = array.getJSONObject(i);
			attachments.add(Attachment.toAttachment(a));
		}

		return attachments;
	}

	public ResponseEntity<String> addCommentToBug(String number, AddCommentRequestBody body) throws ConfigNotFoundException {
		String url = configService.getBugzillaUrl() + "/rest/bug/" + number + "/comment";
		String jsonBody = "{\"comment\": \"" + body.getComment() + "\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-BUGZILLA-API-KEY", body.getApiKey());

		HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
		return restTemplate.postForEntity(url, entity, String.class);
	}

	public ResponseEntity<String> changeBugStatus(String number, ChangeStatusRequestBody body) throws ConfigNotFoundException {
		String url = configService.getBugzillaUrl() + "/rest/bug/" + number;
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