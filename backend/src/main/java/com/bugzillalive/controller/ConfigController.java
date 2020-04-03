package com.bugzillalive.controller;

import com.bugzillalive.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bugs")
public class ConfigController {

	@Autowired
	private ConfigService service;

	@GetMapping("/health")
	@ResponseBody
	public ResponseEntity<String> health() {
		return new ResponseEntity<>("UP", HttpStatus.OK);
	}
}