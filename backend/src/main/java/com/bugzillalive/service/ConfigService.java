package com.bugzillalive.service;

import com.bugzillalive.config.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigService {

	@Autowired
	private ConfigRepository repository;

	public UserConfig getConfig() throws ConfigNotFoundException {
		List<UserConfig> allConfig = repository.findAll();

		if (allConfig.size() == 0) {
			throw new ConfigNotFoundException("No config recorded in the database. Perhaps you need to save some config first?");
		}

		return allConfig.get(0);
	}

	public void saveConfig(UserConfig config) {
		repository.deleteAll();
		repository.save(config);
	}
}
