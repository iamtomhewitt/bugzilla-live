package com.bugzillalive.service;

import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ConfigSaveException;
import com.bugzillalive.model.bug.Attachment;
import com.bugzillalive.model.bug.Bug;
import com.bugzillalive.model.bug.Comment;
import com.bugzillalive.model.request.AddCommentRequestBody;
import com.bugzillalive.model.request.ChangeStatusRequestBody;
import com.bugzillalive.model.response.BugsResponse;
import com.bugzillalive.repository.ConfigRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.bugzillalive.model.request.OutboundEndpoint.*;

@Service
public class BugsService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ConfigService configService;

	public BugsService(RestTemplate restTemplate, ConfigService configService) {
		this.restTemplate = restTemplate;
		this.configService = configService;
	}

	public List<Bug> getBugsByNumbers(String numbers) throws ConfigNotFoundException, ConfigSaveException {
		String url = BUGS_FOR_NUMBERS.uri(this.configService.getConfig().getBugzillaUrl(), numbers);
		BugsResponse response = restTemplate.getForObject(url, BugsResponse.class);
		return response.getBugs();
	}

	public List<Bug> getBugsByUsername(String username) throws ConfigNotFoundException, ConfigSaveException {
		String url = BUGS_FOR_USER.uri(this.configService.getConfig().getBugzillaUrl(), username);
		BugsResponse response = restTemplate.getForObject(url, BugsResponse.class);
		return response.getBugs();
	}

	public List<Comment> getCommentsForBug(String number) throws ConfigNotFoundException, ConfigSaveException {
		String url = BUG_COMMENTS.uri(this.configService.getConfig().getBugzillaUrl(), number);
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

	public List<Attachment> getAttachmentsForBug(String number) throws ConfigNotFoundException, ConfigSaveException {
		String url = BUG_ATTACHMENTS.uri(this.configService.getConfig().getBugzillaUrl(), number);
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

	public ResponseEntity<String> addCommentToBug(String number, AddCommentRequestBody body) throws ConfigNotFoundException, ConfigSaveException {
		String url = BUG_COMMENTS.uri(this.configService.getConfig().getBugzillaUrl(), number);

		JSONObject json = new JSONObject();
		json.put("comment", body.getComment());

		HttpEntity<String> entity = new HttpEntity<>(json.toString(), createHeaders(body.getApiKey()));
		return restTemplate.postForEntity(url, entity, String.class);
	}

	public ResponseEntity<String> changeBugStatus(String number, ChangeStatusRequestBody body) throws ConfigNotFoundException, ConfigSaveException {
		String url = this.configService.getConfig().getBugzillaUrl() + "/rest/bug/" + number;

		JSONObject json = new JSONObject();
		json.put("status", body.getStatus());
		json.put("resolution", body.getResolution());

		HttpEntity<String> entity = new HttpEntity<>(json.toString(), createHeaders(body.getApiKey()));
		return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
	}

	private HttpHeaders createHeaders(String apiKey) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-BUGZILLA-API-KEY", apiKey);
		return headers;
	}
}