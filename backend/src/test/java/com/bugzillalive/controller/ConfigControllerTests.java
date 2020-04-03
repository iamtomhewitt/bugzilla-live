package com.bugzillalive.controller;

import com.bugzillalive.config.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.repository.ConfigRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfigController.class)
@AutoConfigureMockMvc
@ComponentScan({
	"com.bugzillalive"
})
public class ConfigControllerTests {
	@Mock
	private ConfigController configController;

	@MockBean
	private ConfigRepository repository;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private RestTemplate restTemplate;

	private List<UserConfig> mockEmptyDbConfig;
	private List<UserConfig> mockPopulatedDbConfig;

	@Before
	public void eachTest() {
		mockEmptyDbConfig = new ArrayList<>();
		mockPopulatedDbConfig = Collections.singletonList(new UserConfig("someUrl", Arrays.asList("1,2,3", "4,5,6"), "123"));
	}

	@Test
	public void getConfigReturnsConfig() throws Exception {
		String expectedJson = "{\n" +
			"    \"id\": \"123\",\n" +
			"    \"bugzillaUrl\": \"someUrl\",\n" +
			"    \"lists\": [\n" +
			"        \"1,2,3\",\n" +
			"        \"4,5,6\"\n" +
			"    ]\n" +
			"}";

		when(repository.findAll()).thenReturn(mockPopulatedDbConfig);

		mvc.perform(get("/config/get"))
			.andExpect(status().is(HttpStatus.OK.value()))
			.andExpect(content().json(expectedJson));
	}

	@Test
	public void whenNoConfigAvailableHandlesErrorCorrectly() throws Exception {
		when(repository.findAll()).thenReturn(mockEmptyDbConfig);

		try {
			mvc.perform(get("/config/get")).andReturn();
		} catch (Exception e) {
			assertEquals(e.getCause().getClass(), ConfigNotFoundException.class);
			assertEquals(true, e.getMessage().contains("No config recorded in the database. Perhaps you need to save some config first?"));
		}
	}
	
	@Test
	public void saveConfigIsSuccessful() throws Exception {
		String expectedJson = "{\n" +
			"    \"status\": \"OK\"" +
			"}";

		when(repository.findAll()).thenReturn(mockPopulatedDbConfig);

		mvc.perform(put("/config/save")
			.accept(MediaType.APPLICATION_JSON)
			.content("{\n" +
				"\t\"bugzillaUrl\" : \"someUrl\",\n" +
				"\t\"lists\": [\"1,2,3\", \"4,5,6\"]\n" +
				"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is(HttpStatus.OK.value()))
			.andExpect(content().json(expectedJson));
	}
}