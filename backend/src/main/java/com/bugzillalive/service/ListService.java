package com.bugzillalive.service;

import com.bugzillalive.config.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.model.BugList;
import com.bugzillalive.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListService {

	@Autowired
	private ConfigRepository repository;

	public BugList getList(String listName) throws ConfigNotFoundException, ListNotFoundException {
		List<UserConfig> allConfig = repository.findAll();

		if (allConfig.size() == 0) {
			throw new ConfigNotFoundException();
		}

		for (BugList l : allConfig.get(0).lists) {
			if (listName.equals(l.name)) {
				return l;
			}
		}

		throw new ListNotFoundException(listName);
	}
}
