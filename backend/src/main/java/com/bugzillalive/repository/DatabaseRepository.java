package com.bugzillalive.repository;

import com.bugzillalive.config.mongo.UserConfig;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.model.BugList;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DatabaseRepository {
	// https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#reference
	private MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "bugzilla_live");

	public BugList getBugList(String listName) throws ListNotFoundException {
		List<BugList> lists = getAllBugLists();

		for (BugList list : lists) {
			if (listName.equals(list.getName())) {
				return list;
			}
		}

		throw new ListNotFoundException(listName);
	}

	public List<BugList> getAllBugLists() {
		return mongoOps.findAll(UserConfig.class).get(0).getLists();
	}

	public void updateList(String listName, String contents) {
		Query query = new Query(new Criteria("lists.name").is(listName));
		Update update = new Update().set("content", contents);
		mongoOps.updateFirst(query, update, "userConfig");
	}

	public void saveList(String listName, String contents) {
		// TODO
	}

	public void deleteList(String listName) {
		// TODO
	}

	public String getBugzillaUrl() {
		return "TODO";
	}

	public void setBugzillaUrl() {
		// TODO
	}
}
