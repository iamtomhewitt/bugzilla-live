package com.bugzillalive.controller;

import com.bugzillalive.model.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ConfigSaveException;
import com.bugzillalive.model.mongo.BugList;
import com.bugzillalive.repository.DatabaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfigController.class)
@AutoConfigureMockMvc
@ComponentScan({"com.bugzillalive"})
@MockBeans({@MockBean(MongoOperations.class)})
public class ConfigControllerTests {
	@Mock
	private ConfigController configController;

	@MockBean
	private DatabaseRepository repository;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private RestTemplate restTemplate;

	private UserConfig mockEmptyUserConfig;
	private UserConfig mockUserConfig;

	@Before
	public void eachTest() {
		mockEmptyUserConfig = new UserConfig();
		mockUserConfig = new UserConfig("someUrl", Collections.singletonList(new BugList("List Name", "123,456")), "123");
		mockUserConfig.setCurrentList(new BugList("List Name", "123,456"));
	}

	@Test
	public void getConfigReturnsConfig() throws Exception {
		when(repository.getConfig()).thenReturn(mockUserConfig);

		MvcResult result = mvc.perform(get("/config/get"))
			.andExpect(status().isOk())
			.andReturn();

		UserConfig config = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserConfig.class);
		assertEquals("someUrl", config.getBugzillaUrl());
		assertEquals("List Name", config.getLists().get(0).getName());
		assertEquals("123,456", config.getLists().get(0).getContent());
		assertEquals("List Name", config.getCurrentList().getName());
		assertEquals("123,456", config.getCurrentList().getContent());
	}

	@Test
	public void whenNoConfigAvailableHandlesErrorCorrectly() throws ConfigNotFoundException {
		when(repository.getConfig()).thenReturn(mockEmptyUserConfig);

		try {
			mvc.perform(get("/config/get")).andReturn();
		} catch (Exception e) {
			assertEquals(e.getCause().getClass(), ConfigNotFoundException.class);
			assertTrue(e.getMessage().contains("No config recorded in the database. Perhaps you need to save some config first?"));
		}
	}

	@Test
	public void saveConfigIsSuccessful() throws Exception {
		when(repository.getConfig()).thenReturn(mockUserConfig);

		MvcResult result = mvc.perform(put("/config/save")
			.accept(MediaType.APPLICATION_JSON)
			.content("{\n" +
				"    \"bugzillaUrl\": \"URL\",\n" +
				"    \"lists\": [\n" +
				"        {\n" +
				"            \"name\": \"My List\",\n" +
				"            \"content\": \"My content\"\n" +
				"        }\n" +
				"    ]\n" +
				"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is(HttpStatus.OK.value()))
			.andReturn();

		UserConfig config = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserConfig.class);
		assertEquals("someUrl", config.getBugzillaUrl());
		assertEquals("List Name", config.getLists().get(0).getName());
		assertEquals("123,456", config.getLists().get(0).getContent());
		assertEquals("List Name", config.getCurrentList().getName());
		assertEquals("123,456", config.getCurrentList().getContent());
	}

	@Test
	public void saveConfigErrorIsHandled() throws ConfigSaveException {
		doThrow(new ConfigSaveException("Testing exception")).when(configController).saveUserConfig(any());

		try {
			mvc.perform(put("/config/save")).andReturn();
		} catch (Exception e) {
			assertEquals(e.getCause().getClass(), ConfigSaveException.class);
			assertTrue(e.getMessage().contains("Testing exception"));
		}
	}
}