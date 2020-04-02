package com.bugzillalive.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(BugsController.class)
@AutoConfigureMockMvc
@ComponentScan({
	"com.bugzillalive"
})
public class BugsControllerTests {

	@MockBean
	private RestTemplate restTemplate;

	@Mock
	private BugsController bugsController;

	@Autowired
	private MockMvc mvc;

	@Test
	public void bugsByNumbers() throws Exception {
		String mockExternalResponse = "{\n" +
			"\t\"bugs\": [\n" +
			"\t\t{\n" +
			"\t\t\t\"id\": 12345,\n" +
			"\t\t\t\"priority\": \"P1\",\n" +
			"\t\t\t\"last_change_time\": \"2020-02-27T19:49:08Z\",\n" +
			"\t\t\t\"status\": \"Done\",\n" +
			"\t\t\t\"assigned_to\": \"someone@s.com\",\n" +
			"\t\t\t\"severity\": \"normal\",\n" +
			"\t\t\t\"summary\": \"Test bug\",\n" +
			"\t\t\t\"component\": \"Backend\",\n" +
			"\t\t\t\"product\": \"Core\"\n" +
			"\t\t}\n" +
			"\t]\n" +
			"}";

		String expectedResponse = "[\n" +
			"    {\n" +
			"        \"id\": \"12345\",\n" +
			"        \"summary\": \"Test bug\",\n" +
			"        \"status\": \"Done\",\n" +
			"        \"product\": \"Core\",\n" +
			"        \"component\": \"Backend\",\n" +
			"        \"severity\": \"normal\",\n" +
			"        \"assignedTo\": \"someone@s.com\",\n" +
			"        \"lastUpdated\": \"2020-02-27T19:49:08Z\"\n" +
			"    }\n" +
			"]";

		Mockito.when(restTemplate.getForObject(anyString(), eq(String.class)))
			.thenReturn(mockExternalResponse);

		mvc.perform(get("/bugs/numbers?numbers=12345"))
			.andExpect(content().json(expectedResponse));
	}

	@Test
	public void bugsByUsername() throws Exception {
		String mockExternalResponse = "{\n" +
			"\t\"bugs\": [\n" +
			"\t\t{\n" +
			"\t\t\t\"id\": 12345,\n" +
			"\t\t\t\"priority\": \"P1\",\n" +
			"\t\t\t\"last_change_time\": \"2020-02-27T19:49:08Z\",\n" +
			"\t\t\t\"status\": \"Done\",\n" +
			"\t\t\t\"assigned_to\": \"someone@s.com\",\n" +
			"\t\t\t\"severity\": \"normal\",\n" +
			"\t\t\t\"summary\": \"Test bug\",\n" +
			"\t\t\t\"component\": \"Backend\",\n" +
			"\t\t\t\"product\": \"Core\"\n" +
			"\t\t}\n" +
			"\t]\n" +
			"}";

		String expectedResponse = "[\n" +
			"    {\n" +
			"        \"id\": \"12345\",\n" +
			"        \"summary\": \"Test bug\",\n" +
			"        \"status\": \"Done\",\n" +
			"        \"product\": \"Core\",\n" +
			"        \"component\": \"Backend\",\n" +
			"        \"severity\": \"normal\",\n" +
			"        \"assignedTo\": \"someone@s.com\",\n" +
			"        \"lastUpdated\": \"2020-02-27T19:49:08Z\"\n" +
			"    }\n" +
			"]";

		Mockito.when(restTemplate.getForObject(anyString(), eq(String.class)))
			.thenReturn(mockExternalResponse);

		mvc.perform(get("/bugs/username?username=someone@s.com"))
			.andExpect(content().json(expectedResponse));
	}

	@Test
	public void bugComments() throws Exception {
		String mockExternalResponse = "{\n" +
			"\t\"bugs\": {\n" +
			"\t\t\"12345\": {\n" +
			"\t\t\t\"comments\": [\n" +
			"\t\t\t\t{\n" +
			"\t\t\t\t\t\"text\": \"A comment\",\n" +
			"\t\t\t\t\t\"bug_id\": 12345,\n" +
			"\t\t\t\t\t\"author\": \"someone@c.com\",\n" +
			"\t\t\t\t\t\"creator\": \"someone@c.com\",\n" +
			"\t\t\t\t\t\"tags\": [],\n" +
			"\t\t\t\t\t\"count\": 0,\n" +
			"\t\t\t\t\t\"raw_text\": \"This is raw text\",\n" +
			"\t\t\t\t\t\"is_private\": false,\n" +
			"\t\t\t\t\t\"attachment_id\": null,\n" +
			"\t\t\t\t\t\"time\": \"2019-12-19T21:33:56Z\",\n" +
			"\t\t\t\t\t\"creation_time\": \"2019-12-19T21:33:56Z\",\n" +
			"\t\t\t\t\t\"id\": 14557377\n" +
			"\t\t\t\t}\n" +
			"\t\t\t]\n" +
			"\t\t}\n" +
			"\t}\n" +
			"}";

		String expectedResponse = "[\n" +
			"\t{\n" +
			"\t\t\"text\": \"A comment\",\n" +
			"\t\t\"time\": \"2019-12-19T21:33:56Z\",\n" +
			"\t\t\"creator\": \"someone@c.com\"\n" +
			"\t}\n" +
			"]";

		Mockito.when(restTemplate.getForObject(anyString(), eq(String.class)))
			.thenReturn(mockExternalResponse);

		mvc.perform(get("/bugs/12345/comments"))
			.andExpect(content().json(expectedResponse));
	}
}