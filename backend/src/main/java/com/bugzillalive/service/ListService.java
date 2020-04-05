package com.bugzillalive.service;

import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.model.BugList;
import com.bugzillalive.repository.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListService {

	@Autowired
	private DatabaseRepository repository;

	public BugList getList(String listName) throws ConfigNotFoundException, ListNotFoundException {

		BugList list = repository.getBugList(listName);

		return list;
	}
}
