package com.bugzillalive.repository;

import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ConfigSaveException;
import com.bugzillalive.model.mongo.UserConfig;
import org.bson.conversions.Bson;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class ConfigRepository extends DatabaseRepository {

	public ConfigRepository(String dbHost) {
		super(dbHost, "config");
	}

	@Override
	public void save(Object config) throws ConfigNotFoundException, ConfigSaveException {
		UserConfig currentConfig = getAll();

		if (currentConfig != null) {
			throw new ConfigSaveException("Config already exists, please update using a PUT request");
		}

		mongo().save(config);
	}

	@Override
	public UserConfig get(String s) throws ConfigNotFoundException, ConfigSaveException {
		return getAll();
	}

	@Override
	public UserConfig getAll() throws ConfigNotFoundException, ConfigSaveException {
		List<UserConfig> config = mongo().findAll(UserConfig.class);

		if (config.isEmpty()) {
			this.save(new UserConfig("http://yoururl.com"));
			throw new ConfigNotFoundException("A new set of config has been saved.");
		}

		return config.get(0);
	}

	@Override
	public void update(Object config) throws ConfigNotFoundException, ConfigSaveException {
		UserConfig configToUpdate = (UserConfig) config;
		UserConfig currentConfig = getAll();

		Bson filter = eq("bugzillaUrl", currentConfig.getBugzillaUrl());
		Bson updates = set("bugzillaUrl", configToUpdate.getBugzillaUrl());
		collection().updateOne(filter, updates);
	}

	@Override
	public void delete(String s) throws ConfigSaveException {
		throw new ConfigSaveException("Config cannot be deleted");
	}
}
