package com.bugzillalive.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class DatabaseRepository<T> {

	private MongoOperations mongoOps;
	private String collectionName;

	// TODO remove deprecated mongo template
	public DatabaseRepository(String dbHost, String collectionName) {
		String DATABASE = "bugzilla_live";
		mongoOps = new MongoTemplate(new MongoClient(dbHost, 27017), DATABASE);
		this.collectionName = collectionName;
	}

	public MongoOperations mongo() {
		return mongoOps;
	}

	public MongoCollection<Document> collection() {
		return mongoOps.getCollection(collectionName);
	}

	abstract void save(T object) throws Exception;

	abstract Object get(String s) throws Exception;

	abstract Object getAll() throws Exception;

	abstract void update(T Object) throws Exception;

	abstract void delete(String s) throws Exception;
}
