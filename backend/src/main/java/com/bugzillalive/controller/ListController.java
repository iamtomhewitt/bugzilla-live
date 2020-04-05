package com.bugzillalive.controller;

import com.bugzillalive.config.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.model.BugList;
import com.bugzillalive.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("lists")
public class ListController {

	@Autowired
	private ListService service;

	@Autowired
	private MongoOperations myMongoService;

	@GetMapping("/health")
	public ResponseEntity<String> health() {
		myMongoService.insert(new UserConfig("url", Arrays.asList(new BugList("name", "content"))));
		return new ResponseEntity<>("UP", HttpStatus.OK);
	}

	@GetMapping("/{listName}")
	public ResponseEntity<BugList> getList(@PathVariable String listName) throws ListNotFoundException {
		return new ResponseEntity<>(service.getList(listName), HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<BugList>> getAllList() {
		return new ResponseEntity<>(service.getBugLists(), HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<UserConfig> updateList(@RequestBody BugList list) throws ConfigNotFoundException {
		return new ResponseEntity<>(service.updateList(list), HttpStatus.OK);
	}
}