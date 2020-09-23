package com.bugzillalive.config;

import com.bugzillalive.repository.BugListRepository;
import com.bugzillalive.repository.ConfigRepository;
import com.bugzillalive.repository.DatabaseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

	@Bean
	public BugListRepository bugListRepository(@Value("${dbHost}") String dbHost) {
		return new BugListRepository(dbHost);
	}
	@Bean
	public ConfigRepository configRepository(@Value("${dbHost}") String dbHost) {
		return new ConfigRepository(dbHost);
	}
}