package com.bugzillalive.controller;

import com.bugzillalive.config.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("config")
public class ConfigController {

	@Autowired
	private ConfigService service;

	@GetMapping("/health")
	@ResponseBody
	public ResponseEntity<String> health() {
		return new ResponseEntity<>("UP", HttpStatus.OK);
	}

	@GetMapping("/get")
	@ResponseBody
	public ResponseEntity<UserConfig> getUserConfig() throws ConfigNotFoundException {
		UserConfig config = service.getConfig();
		return new ResponseEntity<>(config, HttpStatus.OK);
	}

	@PutMapping("/save")
	@ResponseBody
	public ResponseEntity<String> saveUserConfig(@RequestBody UserConfig config) {
		service.saveConfig(config);
		return new ResponseEntity<>("{\n\t\"status\": \"OK\"\n}", HttpStatus.OK);
	}
}