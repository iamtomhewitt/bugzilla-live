package com.bugzillalive.controller;

import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.model.BugList;
import com.bugzillalive.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("list")
public class ListController {

	@Autowired
	private ListService service;

	@GetMapping("/health")
	public ResponseEntity<String> health() {
		return new ResponseEntity<>("UP", HttpStatus.OK);
	}

	@GetMapping("/{listName}")
	public ResponseEntity<BugList> getList(@PathVariable String listName) throws ListNotFoundException, ConfigNotFoundException {
		return new ResponseEntity<>(service.getList(listName), HttpStatus.OK);
	}
}
