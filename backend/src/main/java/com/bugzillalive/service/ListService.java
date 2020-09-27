package com.bugzillalive.service;

import com.bugzillalive.exception.ListAlreadyExistsException;
import com.bugzillalive.model.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.exception.NoCurrentListException;
import com.bugzillalive.model.mongo.BugList;
import com.bugzillalive.repository.BugListRepository;
import com.bugzillalive.repository.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListService {

	@Autowired
	private BugListRepository repository;

	public BugList getList(String name) throws ListNotFoundException {
		return repository.get(name);
	}

	public List<BugList> getAllLists() {
		return repository.getAll();
	}

	public void updateList(BugList list) throws ListAlreadyExistsException {
		repository.update(list);
	}

	public void saveList(BugList list) throws ListAlreadyExistsException {
		repository.save(list);
	}

	public void deleteList(String listName) throws ListNotFoundException {
		repository.delete(listName);
	}
}