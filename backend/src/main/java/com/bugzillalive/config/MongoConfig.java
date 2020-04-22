package com.bugzillalive.config;

import com.bugzillalive.repository.DatabaseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
	@Bean
	public DatabaseRepository repository(@Value("${dbHost}") String dbHost) {
		return new DatabaseRepository(dbHost);
	}
}