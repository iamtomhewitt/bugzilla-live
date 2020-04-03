package com.bugzillalive.service;

import com.bugzillalive.config.UserConfig;
import com.bugzillalive.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ConfigService {

	@Autowired
	private ConfigRepository repository;

	public void test() {
		repository.save(new UserConfig("url", Arrays.asList("1", "2")));
		for (UserConfig u : repository.findAll()) {
			System.out.println(u.bugzillaUrl);
			System.out.println(u.id);
			for (String s : u.lists) {
				System.out.println(s);
			}
		}
	}

}
