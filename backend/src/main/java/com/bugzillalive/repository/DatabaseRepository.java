package com.bugzillalive.repository;

import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ListAlreadyExistsException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.exception.NoCurrentListException;
import com.bugzillalive.model.mongo.BugList;
import com.bugzillalive.model.mongo.UserConfig;
import com.mongodb.MongoClient;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
// TODO make this abstract and have a list repository and a config repository
public class DatabaseRepository {
	private final String DATABASE = "bugzilla_live";
	private final String COLLECTION = "userConfig";

	private MongoOperations mongoOps;

	public DatabaseRepository(String dbHost) {
		mongoOps = new MongoTemplate(new MongoClient(dbHost, 27017), DATABASE);
	}

	public BugList getBugList(String name) throws ListNotFoundException {
		Query query = query(where("name").is(name));
		return Optional.ofNullable(mongoOps.findOne(query, BugList.class)).orElseThrow(() -> new ListNotFoundException(name));
	}

	public BugList getCurrentBugList() throws NoCurrentListException {
		Query query = query(where("isCurrent").is(true));
		return Optional.ofNullable(mongoOps.findOne(query, BugList.class)).orElseThrow(NoCurrentListException::new);
	}

	public List<BugList> getAllBugLists() {
		return mongoOps.findAll(BugList.class);
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

	public UserConfig updateCurrentList(BugList list) throws ConfigNotFoundException {
		UserConfig currentConfig = getConfig();
		currentConfig.setCurrentList(list);
		mongoOps.save(currentConfig);
		return currentConfig;
	}

	public UserConfig saveList(BugList list) throws ConfigNotFoundException, ListAlreadyExistsException {
		UserConfig currentConfig = getConfig();

		if (currentConfig.getLists().stream().anyMatch(x -> x.getName().equals(list.getName()))) {
			throw new ListAlreadyExistsException(list.getName());
		}

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

		if (config.size() == 0) {
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
