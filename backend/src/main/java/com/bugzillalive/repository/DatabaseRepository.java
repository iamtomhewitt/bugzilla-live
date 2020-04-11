package com.bugzillalive.repository;

import com.bugzillalive.config.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.exception.NoCurrentListException;
import com.bugzillalive.model.BugList;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class DatabaseRepository {
	private final String DATABASE = "bugzilla_live";
	private final String COLLECTION = "userConfig";

	private MongoOperations mongoOps = new MongoTemplate(new MongoClient("db", 27017), DATABASE);

	public BugList getBugList(String listName) throws ListNotFoundException {
		List<BugList> lists = getAllBugLists();

		for (BugList list : lists) {
			if (listName.equals(list.getName())) {
				return list;
			}
		}

		throw new ListNotFoundException(listName);
	}

	public BugList getCurrentBugList() throws NoCurrentListException {
		List<BugList> lists = getAllBugLists();

		for (BugList list : lists) {
			if (list.isCurrent()) {
				return list;
			}
		}

		throw new NoCurrentListException();
	}

	public List<BugList> getAllBugLists() {
		return mongoOps.findAll(UserConfig.class).get(0).getLists();
	}

	public UserConfig updateList(String listName, String contents) throws ConfigNotFoundException {
		UserConfig currentConfig = getConfig();

		for (BugList list : currentConfig.getLists()) {
			if (listName.equals(list.getName())) {
				list.setContent(contents);
			}
		}

		mongoOps.save(currentConfig);

		return currentConfig;
	}

	public UserConfig saveList(BugList list) throws ConfigNotFoundException {
		UserConfig currentConfig = getConfig();
		currentConfig.getLists().add(list);
		mongoOps.save(currentConfig);
		return currentConfig;
	}

	public UserConfig deleteList(String listName) throws ConfigNotFoundException {
		UserConfig currentConfig = getConfig();
		currentConfig.getLists().removeIf(l -> l.getName().equals(listName));
		mongoOps.save(currentConfig);
		return currentConfig;
	}

	public UserConfig getConfig() throws ConfigNotFoundException {
		List<UserConfig> config = mongoOps.findAll(UserConfig.class);

		if (config.size() == 0){
			throw new ConfigNotFoundException();
		}

		return config.get(0);
	}

	public void saveConfig(UserConfig config) {
		mongoOps.save(config);
	}

	public void deleteAll() {
		mongoOps.dropCollection(COLLECTION);
	}
}
