package com.bugzillalive.controller;

import com.bugzillalive.config.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.model.BugList;
import com.bugzillalive.repository.DatabaseRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BugsController.class)
@AutoConfigureMockMvc
@ComponentScan({"com.bugzillalive"})
//@MockBeans({@MockBean(MongoOperations.class)})
public class ListControllerTests {
	@MockBean
	private RestTemplate restTemplate;

	@Mock
	private ListController listController;

	@MockBean
	private DatabaseRepository repository;

	@MockBean
	private MongoOperations mongoOperations;

	@Autowired
	private MockMvc mvc;

	private UserConfig mockEmptyDbConfig;
	private UserConfig mockPopulatedDbConfig;

	@Before
	public void eachTest() {
		mockEmptyDbConfig = new UserConfig();
		mockPopulatedDbConfig = new UserConfig("someUrl", Arrays.asList(new BugList("List Name", "123,456"), new BugList("List Name 2", "789,10")), "123");
	}

	@Test
	public void testGetList() throws Exception {
		String expectedJson = "{\n" +
			"    \"name\": \"List Name\",\n" +
			"    \"content\": \"123,456\"\n" +
			"}";
		when(repository.getBugList(anyString())).thenReturn(mockPopulatedDbConfig.getLists().get(0));

		mvc.perform(get("/lists/List Name"))
			.andExpect(status().isOk())
			.andExpect(content().json(expectedJson));
	}

	@Test
	public void testGetAllLists() throws Exception {
		String expectedJson = "[\n" +
			"    {\n" +
			"        \"name\": \"List Name\",\n" +
			"        \"content\": \"123,456\"\n" +
			"    },\n" +
			"    {\n" +
			"        \"name\": \"List Name 2\",\n" +
			"        \"content\": \"789,10\"\n" +
			"    }\n" +
			"]";

		when(repository.getAllBugLists()).thenReturn(mockPopulatedDbConfig.getLists());

		mvc.perform(get("/lists/all"))
			.andExpect(status().isOk())
			.andExpect(content().json(expectedJson));
	}

	@Test
	public void testSaveList() throws Exception {
		String expectedJson = "{\"id\":\"123\",\"bugzillaUrl\":\"someUrl\",\"lists\":[{\"name\":\"List Name\",\"content\":\"123,456\"},{\"name\":\"List Name 2\",\"content\":\"789,10\"}]}";

		when(repository.saveList(any())).thenReturn(mockPopulatedDbConfig);

		mvc.perform(post("/lists/save")
			.content("{\n" +
				"    \"name\": \"My new\",\n" +
				"    \"content\": \"My new content\"\n" +
				"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(expectedJson));
	}

	@Test
	public void testUpdateList() throws Exception {
		String expectedJson = "{\"id\":\"123\",\"bugzillaUrl\":\"someUrl\",\"lists\":[{\"name\":\"List Name\",\"content\":\"123,456\"},{\"name\":\"List Name 2\",\"content\":\"789,10\"}]}";

		when(repository.updateList(anyString(), anyString())).thenReturn(mockPopulatedDbConfig);

		mvc.perform(put("/lists/update")
			.content("{\n" +
				"    \"name\": \"My new\",\n" +
				"    \"content\": \"My new content\"\n" +
				"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(expectedJson));
	}

	@Test
	public void testDeleteList() throws Exception {
		String expectedJson = "{\"id\":\"123\",\"bugzillaUrl\":\"someUrl\",\"lists\":[{\"name\":\"List Name\",\"content\":\"123,456\"},{\"name\":\"List Name 2\",\"content\":\"789,10\"}]}";

		when(repository.deleteList(anyString())).thenReturn(mockPopulatedDbConfig);

		mvc.perform(delete("/lists/delete?listName=List Name"))
			.andExpect(status().isOk())
			.andExpect(content().json(expectedJson));
	}
}
