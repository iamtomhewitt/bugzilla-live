package com.bugzillalive.controller;

import com.bugzillalive.model.Bug;
import com.bugzillalive.service.BugsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bugs")
public class BugsController {

	@Autowired
	private BugsService service;

	private String BASE_URL = "https://bugzilla.mozilla.org";

	@GetMapping("/health")
	@ResponseBody
	public ResponseEntity<String> health() {
		return new ResponseEntity<>("UP", HttpStatus.OK);
	}

	@GetMapping("/numbers")
	@ResponseBody
	public ResponseEntity<List<Bug>> getBugsByNumbers(@RequestParam String bugNumbers) {
		String url = BASE_URL + "/rest/bug?id="; // TODO make the config service return this by querying mongo for it
		List<Bug> bugs = service.getBugsByNumbers(url, bugNumbers);
		return new ResponseEntity<>(bugs, HttpStatus.OK);
	}

	@GetMapping("/username")
	@ResponseBody
	public ResponseEntity<String> getBugsByUsername(@RequestParam String username) {
		return new ResponseEntity<>("/username", HttpStatus.OK);
	}

	@GetMapping("/{number}/comments")
	@ResponseBody
	public ResponseEntity<String> getCommentsForBug(@PathVariable String number) {
		return new ResponseEntity<>("/{number}/comments", HttpStatus.OK);
	}

	@GetMapping("/{number}/attachments")
	@ResponseBody
	public ResponseEntity<String> getAttachmentsForBug(@PathVariable String number) {
		return new ResponseEntity<>("/{number}/attachments", HttpStatus.OK);
	}

	@PostMapping("/{number}/comments/add")
	@ResponseBody
	public ResponseEntity<String> addCommentToBug(@PathVariable String number, @RequestBody String comment) { // TODO create comment object
		return new ResponseEntity<>("/{number}/comments/add", HttpStatus.OK);
	}

	@PostMapping("/{number}/status/change")
	@ResponseBody
	public ResponseEntity<String> changeBugStatus(@PathVariable String number, @RequestBody String status) { // TODO create status object
		return new ResponseEntity<>("/{number}/status/change", HttpStatus.OK);
	}
}