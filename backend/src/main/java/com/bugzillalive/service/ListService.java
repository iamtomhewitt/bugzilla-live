package com.bugzillalive.service;

import com.bugzillalive.exception.ListAlreadyExistsException;
import com.bugzillalive.model.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.exception.NoCurrentListException;
import com.bugzillalive.model.mongo.BugList;
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

	public BugList getCurrentList() throws NoCurrentListException {
		return repository.getCurrentBugList();
	}

	public UserConfig updateList(BugList list) throws ConfigNotFoundException {
		return repository.updateList(list.getName(), list.getContent());
	}

	public UserConfig updateCurrentList(BugList list) throws ConfigNotFoundException {
		list.setCurrent(true);
		UserConfig config = repository.updateCurrentList(list);

		try {
			config = repository.saveList(list);
		} catch (ListAlreadyExistsException e) {
			System.out.println(e.getMessage());
		}

		return config;
	}

	public UserConfig saveList(BugList list) throws ConfigNotFoundException, ListAlreadyExistsException {
		return repository.saveList(list);
	}

	public UserConfig deleteList(String listName) throws ConfigNotFoundException {
		return repository.deleteList(listName);
	}
}