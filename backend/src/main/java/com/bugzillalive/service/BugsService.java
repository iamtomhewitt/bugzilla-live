package com.bugzillalive.service;

import com.bugzillalive.model.Bug;
import com.bugzillalive.model.BugResponse;
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
}
