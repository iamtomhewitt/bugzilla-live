package com.bugzillalive.controller;

import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ConfigSaveException;
import com.bugzillalive.model.bug.Attachment;
import com.bugzillalive.model.bug.Bug;
import com.bugzillalive.model.bug.Comment;
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
@CrossOrigin(origins = "*")
public class BugsController {
	@Autowired
	private BugsService service;

	@GetMapping("/{numbers}")
	@ResponseBody
	public ResponseEntity<List<Bug>> getBugsByNumbers(@PathVariable String numbers) throws ConfigNotFoundException, ConfigSaveException {
		List<Bug> bugs = service.getBugsByNumbers(numbers);
		return new ResponseEntity<>(bugs, HttpStatus.OK);
	}

	@GetMapping("/user/{username}")
	@ResponseBody
	public ResponseEntity<List<Bug>> getBugsByUsername(@PathVariable String username) throws ConfigNotFoundException, ConfigSaveException {
		List<Bug> bugs = service.getBugsByUsername(username);
		return new ResponseEntity<>(bugs, HttpStatus.OK);
	}

	@GetMapping("/{number}/comments")
	@ResponseBody
	public ResponseEntity<List<Comment>> getCommentsForBug(@PathVariable String number) throws ConfigNotFoundException, ConfigSaveException {
		List<Comment> comments = service.getCommentsForBug(number);
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

	@GetMapping("/{number}/attachments")
	@ResponseBody
	public ResponseEntity<List<Attachment>> getAttachmentsForBug(@PathVariable String number) throws ConfigNotFoundException, ConfigSaveException {
		List<Attachment> attachments = service.getAttachmentsForBug(number);
		return new ResponseEntity<>(attachments, HttpStatus.OK);
	}

	@PostMapping("/{number}/comments/add")
	@ResponseBody
	public ResponseEntity<String> addCommentToBug(@PathVariable String number, @RequestBody AddCommentRequestBody body) throws ConfigNotFoundException, ConfigSaveException {
		return service.addCommentToBug(number, body);
	}

	@PutMapping("/{number}/status/change")
	@ResponseBody
	public ResponseEntity<String> changeBugStatus(@PathVariable String number, @RequestBody ChangeStatusRequestBody body) throws ConfigNotFoundException, ConfigSaveException {
		return service.changeBugStatus(number, body);
	}
}