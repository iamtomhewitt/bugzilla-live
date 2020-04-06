package com.bugzillalive.service;

import com.bugzillalive.config.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.model.BugList;
import com.bugzillalive.repository.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListService {

	@Autowired
	private DatabaseRepository repository;

	public BugList getList(String listName) throws ListNotFoundException {
		return repository.getBugList(listName);
	}

	public List<BugList> getBugLists() {
		return repository.getAllBugLists();
	}

	public UserConfig updateList(BugList list) throws ConfigNotFoundException {
		return repository.updateList(list.getName(), list.getContent());
	}

	public UserConfig saveList(BugList list) throws ConfigNotFoundException {
		return repository.saveList(list);
	}

	public UserConfig deleteList(String listName) throws ConfigNotFoundException {
		return repository.deleteList(listName);
	}
}