package com.bugzillalive.controller;

import com.bugzillalive.exception.ListAlreadyExistsException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.model.mongo.BugList;
import com.bugzillalive.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("lists")
@CrossOrigin(origins = "*")
public class ListController {

	@Autowired
	private ListService listService;

	private Map<String, String> body = new HashMap<>();

	@GetMapping("/{name}")
	public ResponseEntity<BugList> getList(@PathVariable String name) throws ListNotFoundException {
		return new ResponseEntity<>(listService.getList(name), HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<BugList>> getAllList() {
		return new ResponseEntity<>(listService.getAllLists(), HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<Map<String, String>> saveList(@RequestBody BugList list) throws ListAlreadyExistsException {
		listService.saveList(list);
		return new ResponseEntity<>(setBodyMessage("Saved"), HttpStatus.OK);
	}

	@PutMapping("/")
	public ResponseEntity<Map<String, String>> updateList(@RequestBody BugList list) {
		listService.updateList(list);
		return new ResponseEntity<>(setBodyMessage("Updated"), HttpStatus.OK);
	}

	@DeleteMapping()
	public ResponseEntity<Map<String, String>> deleteList(@RequestParam String name) throws ListNotFoundException {
		listService.deleteList(name);
		return new ResponseEntity<>(setBodyMessage("Deleted"), HttpStatus.OK);
	}

	private Map<String, String> setBodyMessage(String message) {
		this.body.clear();
		this.body.put("message", message);
		return this.body;
	}
}