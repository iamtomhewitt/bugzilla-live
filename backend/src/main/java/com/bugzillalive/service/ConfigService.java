package com.bugzillalive.service;

import com.bugzillalive.config.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.repository.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {
	@Autowired
	private DatabaseRepository repository;

	public UserConfig getConfig() throws ConfigNotFoundException {
		return repository.getConfig();
	}

	public void saveConfig(UserConfig config) {
		repository.deleteAll();
		repository.saveConfig(config);
	}
}