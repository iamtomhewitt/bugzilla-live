package com.bugzillalive.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

	@Override
	public String getDatabaseName(){
		return "bugzilla_live";
	}

	@Override
	@Bean
	public MongoClient mongoClient() {
		return new MongoClient("localhost");
	}
}