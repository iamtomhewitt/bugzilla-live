package com.bugzillalive.controller;

import com.bugzillalive.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BugController {

	@Autowired
	private Config config;

	@GetMapping("/test")
	public ResponseEntity<String> test() {
		return new ResponseEntity<>(config.getBugzillaUrl(), HttpStatus.OK);
	}
}
