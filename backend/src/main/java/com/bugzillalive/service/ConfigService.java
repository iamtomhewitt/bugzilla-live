package com.bugzillalive.service;

import com.bugzillalive.config.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.repository.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConfigService {
	@Autowired
	private DatabaseRepository repository;

	public UserConfig getConfig() throws ConfigNotFoundException {
		return repository.getConfig();
	}

	public ResponseEntity<Map<String, String>> saveConfig(UserConfig config) {
		Map<String, String> map = new HashMap<>();

		try {
			repository.deleteAll();
			repository.saveConfig(config);
			map.put("status", "OK");
			map.put("error", "");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			map.put("status", "ERROR");
			map.put("error", e.getMessage());
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}