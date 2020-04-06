package com.bugzillalive.controller;

import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.model.*;
import com.bugzillalive.model.request.AddCommentRequestBody;
import com.bugzillalive.model.request.ChangeStatusRequestBody;
import com.bugzillalive.service.BugsService;
import com.bugzillalive.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bugs")
@CrossOrigin(origins="*")
public class BugsController {
	@Autowired
	private BugsService service;

	@Autowired
	private ConfigService configService;

	@GetMapping("/health")
	@ResponseBody
	public ResponseEntity<String> health() {
		return new ResponseEntity<>("UP", HttpStatus.OK);
	}

	@GetMapping("/numbers")
	@ResponseBody
	public ResponseEntity<List<Bug>> getBugsByNumbers(@RequestParam String numbers) throws ConfigNotFoundException {
		String url = configService.getBugzillaUrl() + "/rest/bug?id=";
		List<Bug> bugs = service.getBugsByNumbers(url, numbers);
		return new ResponseEntity<>(bugs, HttpStatus.OK);
	}

	@GetMapping("/username")
	@ResponseBody
	public ResponseEntity<List<Bug>> getBugsByUsername(@RequestParam String username) throws ConfigNotFoundException {
		String url = configService.getBugzillaUrl() + "/rest/bug?assigned_to=";
		List<Bug> bugs = service.getBugsByUsername(url, username);
		return new ResponseEntity<>(bugs, HttpStatus.OK);
	}

	@GetMapping("/{number}/comments")
	@ResponseBody
	public ResponseEntity<List<Comment>> getCommentsForBug(@PathVariable String number) throws ConfigNotFoundException {
		String url = configService.getBugzillaUrl() + "/rest/bug/" + number + "/comment";
		List<Comment> comments = service.getCommentsForBug(url, number);
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

	@GetMapping("/{number}/attachments")
	@ResponseBody
	public ResponseEntity<List<Attachment>> getAttachmentsForBug(@PathVariable String number) throws ConfigNotFoundException {
		String url = configService.getBugzillaUrl() + "/rest/bug/" + number + "/attachment";
		List<Attachment> attachments = service.getAttachmentsForBug(url, number);
		return new ResponseEntity<>(attachments, HttpStatus.OK);
	}

	@PostMapping("/{number}/comments/add")
	@ResponseBody
	public ResponseEntity<String> addCommentToBug(@PathVariable String number, @RequestBody AddCommentRequestBody body) throws ConfigNotFoundException {
		return service.addCommentToBug(configService.getBugzillaUrl() + "/rest/bug/" + number + "/comment", body);
	}

	@PutMapping("/{number}/status/change")
	@ResponseBody
	public ResponseEntity<String> changeBugStatus(@PathVariable String number, @RequestBody ChangeStatusRequestBody body) throws ConfigNotFoundException {
		return service.changeBugStatus(configService.getBugzillaUrl() + "/rest/bug/" + number, body);
	}
}