package com.bugzillalive.controller;

import com.bugzillalive.model.*;
import com.bugzillalive.model.request.AddCommentRequestBody;
import com.bugzillalive.model.request.ChangeStatusRequestBody;
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
	public ResponseEntity<List<Bug>> getBugsByNumbers(@RequestParam String numbers) {
		String url = BASE_URL + "/rest/bug?id="; // TODO make the config service return this by querying mongo for it
		List<Bug> bugs = service.getBugsByNumbers(url, numbers);
		return new ResponseEntity<>(bugs, HttpStatus.OK);
	}

	@GetMapping("/username")
	@ResponseBody
	public ResponseEntity<List<Bug>> getBugsByUsername(@RequestParam String username) {
		String url = BASE_URL + "/rest/bug?assigned_to="; // TODO make the config service return this by querying mongo for it
		List<Bug> bugs = service.getBugsByUsername(url, username);
		return new ResponseEntity<>(bugs, HttpStatus.OK);
	}

	@GetMapping("/{number}/comments")
	@ResponseBody
	public ResponseEntity<List<Comment>> getCommentsForBug(@PathVariable String number) {
		String url = BASE_URL + "/rest/bug/" + number + "/comment"; // TODO make the config service return this by querying mongo for it
		List<Comment> comments = service.getCommentsForBug(url, number);
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

	@GetMapping("/{number}/attachments")
	@ResponseBody
	public ResponseEntity<List<Attachment>> getAttachmentsForBug(@PathVariable String number) {
		String url = BASE_URL + "/rest/bug/" + number + "/attachment"; // TODO make the config service return this by querying mongo for it
		List<Attachment> attachments = service.getAttachmentsForBug(url, number);
		return new ResponseEntity<>(attachments, HttpStatus.OK);
	}

	@PostMapping("/{number}/comments/add")
	@ResponseBody
	public ResponseEntity<String> addCommentToBug(@PathVariable String number, @RequestBody AddCommentRequestBody body) {
		return service.addCommentToBug(BASE_URL + "/rest/bug/" + number + "/comment", body);
	}

	@PutMapping("/{number}/status/change")
	@ResponseBody
	public ResponseEntity<String> changeBugStatus(@PathVariable String number, @RequestBody ChangeStatusRequestBody body) {
		return service.changeBugStatus(BASE_URL + "/rest/bug/" + number, body);
	}
}