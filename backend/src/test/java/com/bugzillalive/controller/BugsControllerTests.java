package com.bugzillalive.controller;

import com.bugzillalive.model.bug.Attachment;
import com.bugzillalive.model.bug.Bug;
import com.bugzillalive.model.bug.Comment;
import com.bugzillalive.repository.DatabaseRepository;
import com.bugzillalive.service.ConfigService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BugsController.class)
@AutoConfigureMockMvc
@ComponentScan({"com.bugzillalive"})
@MockBeans({@MockBean(MongoOperations.class), @MockBean(DatabaseRepository.class), @MockBean(ConfigService.class)})
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

		Mockito.when(restTemplate.getForObject(anyString(), eq(String.class)))
			.thenReturn(mockExternalResponse);

		MvcResult result = mvc.perform(get("/bugs/numbers?numbers=12345"))
			.andExpect(status().isOk())
			.andReturn();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		List<Bug> bugs = mapper.convertValue(
			mapper.readValue(result.getResponse().getContentAsString(), Bug[].class),
			new TypeReference<List<Bug>>() {
			}
		);

		assertEquals(1, bugs.size());
		assertEquals("12345", bugs.get(0).getId());
		assertEquals("Test bug", bugs.get(0).getSummary());
		assertEquals("Done", bugs.get(0).getStatus());
		assertEquals("Core", bugs.get(0).getProduct());
		assertEquals("Backend", bugs.get(0).getComponent());
		assertEquals("Backend", bugs.get(0).getComponent());
		assertEquals("normal", bugs.get(0).getSeverity());
		assertEquals("someone@s.com", bugs.get(0).getAssignedTo());
		assertEquals("2020-02-27T19:49:08Z", bugs.get(0).getLastUpdated());
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

		Mockito.when(restTemplate.getForObject(anyString(), eq(String.class)))
			.thenReturn(mockExternalResponse);

		MvcResult result = mvc.perform(get("/bugs/username?username=someone@s.com"))
			.andExpect(status().isOk())
			.andReturn();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		List<Bug> bugs = mapper.convertValue(
			mapper.readValue(result.getResponse().getContentAsString(), Bug[].class),
			new TypeReference<List<Bug>>() {
			}
		);

		assertEquals(1, bugs.size());
		assertEquals("12345", bugs.get(0).getId());
		assertEquals("Test bug", bugs.get(0).getSummary());
		assertEquals("Done", bugs.get(0).getStatus());
		assertEquals("Core", bugs.get(0).getProduct());
		assertEquals("Backend", bugs.get(0).getComponent());
		assertEquals("Backend", bugs.get(0).getComponent());
		assertEquals("normal", bugs.get(0).getSeverity());
		assertEquals("someone@s.com", bugs.get(0).getAssignedTo());
		assertEquals("2020-02-27T19:49:08Z", bugs.get(0).getLastUpdated());
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

		Mockito.when(restTemplate.getForObject(anyString(), eq(String.class)))
			.thenReturn(mockExternalResponse);

		MvcResult result = mvc.perform(get("/bugs/12345/comments"))
			.andExpect(status().isOk())
			.andReturn();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		List<Comment> comments = mapper.convertValue(
			mapper.readValue(result.getResponse().getContentAsString(), Comment[].class),
			new TypeReference<List<Comment>>() {
			}
		);

		assertEquals(1, comments.size());
		assertEquals("someone@c.com", comments.get(0).getCreator());
		assertEquals("A comment", comments.get(0).getText());
		assertEquals("2019-12-19T21:33:56Z", comments.get(0).getTime());
	}

	@Test
	public void bugAttachments() throws Exception {
		String mockExternalResponse = "{\n" +
			"    \"bugs\": {\n" +
			"        \"12345\": [\n" +
			"            {\n" +
			"                \"last_change_time\": \"2015-09-23T18:36:19Z\",\n" +
			"                \"is_obsolete\": 1,\n" +
			"                \"description\": \"Some patch\",\n" +
			"                \"file_name\": \"Some filename\",\n" +
			"                \"flags\": [],\n" +
			"                \"bug_id\": 12345,\n" +
			"                \"data\": \"test\",\n" +
			"                \"summary\": \"Some patch.\",\n" +
			"                \"creation_time\": \"2015-09-23T18:35:48Z\",\n" +
			"                \"creator_detail\": {\n" +
			"                    \"id\": 406194,\n" +
			"                    \"nick\": \"nick\",\n" +
			"                    \"real_name\": \"nick\",\n" +
			"                    \"email\": \"someone@c.com\",\n" +
			"                    \"name\": \"someone@c.com\"\n" +
			"                },\n" +
			"                \"id\": 1,\n" +
			"                \"size\": 1,\n" +
			"                \"attacher\": \"someone@c.com\",\n" +
			"                \"is_patch\": 1,\n" +
			"                \"is_private\": 0,\n" +
			"                \"content_type\": \"text/plain\",\n" +
			"                \"creator\": \"someone@c.com\"\n" +
			"            }\n" +
			"        ]\n" +
			"    },\n" +
			"    \"attachments\": {}\n" +
			"}";

		Mockito.when(restTemplate.getForObject(anyString(), eq(String.class)))
			.thenReturn(mockExternalResponse);

		MvcResult result = mvc.perform(get("/bugs/12345/attachments"))
			.andExpect(status().isOk())
			.andReturn();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		List<Attachment> attachments = mapper.convertValue(
			mapper.readValue(result.getResponse().getContentAsString(), Attachment[].class),
			new TypeReference<List<Attachment>>() {
			}
		);

		assertEquals(1, attachments.size());
		assertEquals("Some filename", attachments.get(0).getFilename());
		assertEquals(1, attachments.get(0).getId());
	}

	@Test
	public void addCommentToBug() throws Exception {
		ResponseEntity<String> mockResponse = new ResponseEntity<>("{\"id\":999}", HttpStatus.CREATED);

		Mockito.when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
			.thenReturn(mockResponse);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
			.post("/bugs/12345/comments/add")
			.accept(MediaType.APPLICATION_JSON)
			.content("{\n" +
				"\t\"comment\": \"A test\",\n" +
				"\t\"apiKey\": \"key\"\n" +
				"}")
			.contentType(MediaType.APPLICATION_JSON);

		MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		assertEquals("{\"id\":999}", response.getContentAsString());
	}

	@Test
	public void changeBugStatusNoResolution() throws Exception {
		String mockResponseBody = "{\n" +
			"    \"bugs\": [\n" +
			"        {\n" +
			"            \"changes\": {\n" +
			"                \"resolution\": {\n" +
			"                    \"added\": \"\",\n" +
			"                    \"removed\": \"INVALID\"\n" +
			"                },\n" +
			"                \"status\": {\n" +
			"                    \"added\": \"UNCONFIRMED\",\n" +
			"                    \"removed\": \"RESOLVED\"\n" +
			"                }\n" +
			"            },\n" +
			"            \"id\": 1605238,\n" +
			"            \"last_change_time\": \"2020-04-02T13:32:17Z\",\n" +
			"            \"alias\": null\n" +
			"        }\n" +
			"    ]\n" +
			"}";
		ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

		Mockito.when(restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
			.thenReturn(mockResponse);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
			.put("/bugs/12345/status/change")
			.accept(MediaType.APPLICATION_JSON)
			.content("{\n" +
				"\t\"status\" : \"UNCONFIRMED\",\n" +
				"\t\"apiKey\": \"key\"\n" +
				"}")
			.contentType(MediaType.APPLICATION_JSON);

		MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(mockResponseBody, response.getContentAsString());
	}

	@Test
	public void changeBugStatusWithResolution() throws Exception {
		String mockResponseBody = "{\n" +
			"    \"bugs\": [\n" +
			"        {\n" +
			"            \"changes\": {\n" +
			"                \"resolution\": {\n" +
			"                    \"added\": \"INVALID\",\n" +
			"                    \"removed\": \"\"\n" +
			"                },\n" +
			"                \"status\": {\n" +
			"                    \"removed\": \"UNCONFIRMED\",\n" +
			"                    \"added\": \"RESOLVED\"\n" +
			"                }\n" +
			"            },\n" +
			"            \"alias\": null,\n" +
			"            \"last_change_time\": \"2020-04-02T13:35:00Z\",\n" +
			"            \"id\": 1605238\n" +
			"        }\n" +
			"    ]\n" +
			"}";
		ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

		Mockito.when(restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
			.thenReturn(mockResponse);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
			.put("/bugs/12345/status/change")
			.accept(MediaType.APPLICATION_JSON)
			.content("{\n" +
				"\t\"status\" : \"RESOLVED\",\n" +
				"\t\"resolution\": \"INVALID\",\n" +
				"\t\"apiKey\": \"key\"\n" +
				"}")
			.contentType(MediaType.APPLICATION_JSON);

		MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(mockResponseBody, response.getContentAsString());
	}
}