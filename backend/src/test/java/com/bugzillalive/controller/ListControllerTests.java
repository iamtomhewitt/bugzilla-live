package com.bugzillalive.controller;

import com.bugzillalive.exception.ListAlreadyExistsException;
import com.bugzillalive.model.mongo.BugList;
import com.bugzillalive.model.mongo.UserConfig;
import com.bugzillalive.repository.DatabaseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BugsController.class)
@AutoConfigureMockMvc
@ComponentScan({"com.bugzillalive"})
public class ListControllerTests {
	@MockBean
	private RestTemplate restTemplate;

	@Mock
	private ListController listController;

	@MockBean
	private DatabaseRepository mockRepository;

	@MockBean
	private MongoOperations mongoOperations;

	@Autowired
	private MockMvc mvc;

	private UserConfig mockUserConfig;

	@Before
	public void eachTest() {
		mockUserConfig = new UserConfig("someUrl", Arrays.asList(new BugList("List Name", "123,456"), new BugList("List Name 2", "789,10")), "123");
	}

	@Test
	public void testGetList() throws Exception {
		when(mockRepository.getBugList(anyString())).thenReturn(mockUserConfig.getLists().get(0));

		MvcResult result = mvc.perform(get("/lists/List Name"))
			.andExpect(status().isOk())
			.andReturn();

		BugList listResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), BugList.class);
		assertEquals("List Name", listResult.getName());
		assertEquals("123,456", listResult.getContent());
		assertFalse(listResult.isCurrent());
	}

	@Test
	public void testGetCurrentList() throws Exception {
		BugList mockCurrentList = new BugList("List Name", "123,456");
		mockCurrentList.setCurrent(true);
		when(mockRepository.getCurrentBugList()).thenReturn(mockCurrentList);

		MvcResult result = mvc.perform(get("/lists/current"))
			.andExpect(status().isOk())
			.andReturn();

		BugList listResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), BugList.class);
		assertEquals("List Name", listResult.getName());
		assertEquals("123,456", listResult.getContent());
		assertTrue(listResult.isCurrent());
	}

	@Test
	public void testGetAllLists() throws Exception {
		when(mockRepository.getAllBugLists()).thenReturn(mockUserConfig.getLists());

		MvcResult result = mvc.perform(get("/lists/all"))
			.andExpect(status().isOk())
			.andReturn();

		List<BugList> lists = new ObjectMapper().convertValue(
			new ObjectMapper().readValue(result.getResponse().getContentAsString(), List.class),
			new TypeReference<List<BugList>>() {
			}
		);
		assertEquals("List Name", lists.get(0).getName());
		assertEquals("123,456", lists.get(0).getContent());
		assertEquals("List Name 2", lists.get(1).getName());
		assertEquals("789,10", lists.get(1).getContent());
	}

	@Test
	public void testSaveList() throws Exception {
		UserConfig mockConfig = mockUserConfig;
		mockConfig.setLists(Collections.singletonList(new BugList("My new list", "My new content")));
		when(mockRepository.saveList(any())).thenReturn(mockConfig);

		MvcResult result = mvc.perform(post("/lists/save")
			.content("{\n" +
				"    \"name\": \"My new list\",\n" +
				"    \"content\": \"My new content\"\n" +
				"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		UserConfig listResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserConfig.class);
		assertEquals("someUrl", listResult.getBugzillaUrl());
		assertEquals("My new content", listResult.getLists().get(0).getContent());
		assertEquals("My new list", listResult.getLists().get(0).getName());
	}

	@Test
	public void updatingCurrentListWorks() throws Exception {
		UserConfig mockConfig = mockUserConfig;
		mockConfig.setCurrentList(new BugList("My new list", "My new content"));
		when(mockRepository.updateCurrentList(any())).thenReturn(mockConfig);

		MvcResult result = mvc.perform(put("/lists/current")
			.content("{\n" +
				"    \"name\": \"My new list\",\n" +
				"    \"content\": \"My new content\"\n" +
				"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		UserConfig listResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserConfig.class);
		assertEquals("someUrl", listResult.getBugzillaUrl());
		assertEquals("My new content", listResult.getCurrentList().getContent());
		assertEquals("My new list", listResult.getCurrentList().getName());
	}

	@Test
	public void savingExistingListThrowsError() throws Exception {
		when(mockRepository.saveList(any())).thenThrow(new ListAlreadyExistsException("My new list"));

		try {
			mvc.perform(post("/lists/save")
				.content("{\n" +
				"    \"name\": \"My new list\",\n" +
				"    \"content\": \"My new content\"\n" +
				"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		} catch (Exception e) {
			assertEquals(e.getCause().getClass(), ListAlreadyExistsException.class);
			assertTrue(e.getMessage().contains("Cannot save list, 'My new list' already exists"));
		}
	}

	@Test
	public void testUpdateList() throws Exception {
		when(mockRepository.updateList(anyString(), anyString())).thenReturn(mockUserConfig);

		MvcResult result = mvc.perform(put("/lists/update")
			.content("{\n" +
				"    \"name\": \"My new list\",\n" +
				"    \"content\": \"My new content\"\n" +
				"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		UserConfig listResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserConfig.class);
		assertEquals("someUrl", listResult.getBugzillaUrl());
		assertEquals("123,456", listResult.getLists().get(0).getContent());
		assertEquals("List Name", listResult.getLists().get(0).getName());
	}

	@Test
	public void testDeleteList() throws Exception {
		when(mockRepository.deleteList(anyString())).thenReturn(mockUserConfig);

		MvcResult result = mvc.perform(delete("/lists/delete?listName=List Name"))
			.andExpect(status().isOk())
			.andReturn();

		UserConfig listResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserConfig.class);
		assertEquals("someUrl", listResult.getBugzillaUrl());
		assertEquals("123,456", listResult.getLists().get(0).getContent());
		assertEquals("List Name", listResult.getLists().get(0).getName());
	}
}
