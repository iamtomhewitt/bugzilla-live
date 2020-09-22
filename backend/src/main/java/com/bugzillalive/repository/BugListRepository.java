package com.bugzillalive.repository;

import com.bugzillalive.exception.ListAlreadyExistsException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.model.mongo.BugList;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class BugListRepository extends DatabaseRepository {

	public BugListRepository(String dbHost) {
		super(dbHost);
	}

	@Override
	public void save(Object object) throws ListAlreadyExistsException {
		BugList list = (BugList) object;

		boolean exists = !mongo().find(query(where("name").is(list.getName())), BugList.class).isEmpty();
		if (exists) {
			throw new ListAlreadyExistsException(list.getName());
		}

		mongo().save(list);
	}

	@Override
	public BugList get(String name) throws ListNotFoundException {
		Query query = query(where("name").is(name));
		return Optional.ofNullable(mongo().findOne(query, BugList.class)).orElseThrow(() -> new ListNotFoundException(name));
	}

	@Override
	public List<BugList> getAll() {
		return mongo().findAll(BugList.class);
	}

	@Override
	public void update(Object object) {
		BugList list = (BugList) object;
		Bson filter = eq("name", list.getName());
		Bson content = set("content", list.getContent());
		Bson isCurrent = set("isCurrent", list.isCurrent());
		Bson updates = combine(content, isCurrent);
		mongo().getCollection("lists").updateOne(filter, updates);
	}

	@Override
	public void delete(String s) throws ListNotFoundException {
		Bson filter = eq("name", s);

		Query query = query(where("name").is(s));
		Optional.ofNullable(mongo().findOne(query, BugList.class)).orElseThrow(() -> new ListNotFoundException(s));

		mongo().getCollection("lists").deleteOne(filter);
	}
}
