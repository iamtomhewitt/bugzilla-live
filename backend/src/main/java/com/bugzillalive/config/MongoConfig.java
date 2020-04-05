package com.bugzillalive.config;

import com.bugzillalive.repository.DatabaseRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
	@Bean
	public DatabaseRepository repository() {
		return new DatabaseRepository();
	}
}