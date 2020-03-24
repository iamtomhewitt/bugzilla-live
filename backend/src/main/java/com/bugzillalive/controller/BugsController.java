package com.bugzillalive.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bugs")
public class BugsController {

	@GetMapping("/health")
	@ResponseBody
	public ResponseEntity<String> health() {
		return new ResponseEntity<>("UP", HttpStatus.OK);
	}

	@GetMapping("/numbers")
	@ResponseBody
	public ResponseEntity<String> getBugsByNumbers(@RequestParam String bugNumbers) {
		return new ResponseEntity<>("/numbers", HttpStatus.OK);
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