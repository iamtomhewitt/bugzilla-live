package com.bugzillalive.controller;

import com.bugzillalive.config.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.model.BugList;
import com.bugzillalive.service.ListService;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("list")
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
	public ResponseEntity<BugList> getList(@PathVariable String listName) throws ListNotFoundException, ConfigNotFoundException {
		return new ResponseEntity<>(service.getList(listName), HttpStatus.OK);
	}
}
