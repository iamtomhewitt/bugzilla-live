package com.bugzillalive.repository;

import com.bugzillalive.config.UserConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends MongoRepository<UserConfig, String> {

}
