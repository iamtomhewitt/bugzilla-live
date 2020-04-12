package com.bugzillalive.controller;

import com.bugzillalive.model.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ConfigSaveException;
import com.bugzillalive.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("config")
@CrossOrigin(origins="*")
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
	public ResponseEntity<UserConfig> saveUserConfig(@RequestBody UserConfig config) throws ConfigSaveException {
		UserConfig updatedConfig = service.saveConfig(config);
		return new ResponseEntity<>(updatedConfig, HttpStatus.OK);
	}
}