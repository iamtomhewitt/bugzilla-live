package com.bugzillalive.controller;

import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ConfigSaveException;
import com.bugzillalive.model.mongo.UserConfig;
import com.bugzillalive.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("config")
@CrossOrigin(origins = "*")
public class ConfigController {

	@Autowired
	private ConfigService service;

	private Map<String, String> body = new HashMap<>();

	@GetMapping()
	public ResponseEntity<UserConfig> getConfig() throws Exception {
		return new ResponseEntity<>(service.getConfig(), HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<Map<String, String>> saveConfig(@RequestBody UserConfig config) throws ConfigNotFoundException, ConfigSaveException {
		service.saveConfig(config);
		return new ResponseEntity<>(setBodyMessage("Saved"), HttpStatus.OK);
	}

	@PutMapping()
	public ResponseEntity<Map<String, String>> updateConfig(@RequestBody UserConfig config) throws ConfigNotFoundException, ConfigSaveException {
		service.updateConfig(config);
		return new ResponseEntity<>(setBodyMessage("Updated"), HttpStatus.OK);
	}

	private Map<String, String> setBodyMessage(String message) {
		this.body.clear();
		this.body.put("message", message);
		return this.body;
	}
}