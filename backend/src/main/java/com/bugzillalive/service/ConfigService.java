package com.bugzillalive.service;

import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ConfigSaveException;
import com.bugzillalive.model.mongo.UserConfig;
import com.bugzillalive.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

	@Autowired
	private ConfigRepository configRepository;

	public ConfigService(ConfigRepository configRepository) {
		this.configRepository = configRepository;
	}

	public UserConfig getConfig() throws ConfigNotFoundException, ConfigSaveException {
		return configRepository.getAll();
	}

	public void saveConfig(UserConfig config) throws ConfigSaveException, ConfigNotFoundException {
		configRepository.save(config);
	}

	public void updateConfig(UserConfig config) throws ConfigNotFoundException, ConfigSaveException {
		configRepository.update(config);
	}
}