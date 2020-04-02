package com.bugzillalive.controller;

import com.bugzillalive.model.Comment;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

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
	public void bugsByUsername() {
//		BugResponse mockBugResponse = BugResponse.builder()
//			.bugs(Arrays.asList(
//				Bug.builder()
//					.assignedTo("Someone")
//					.component("Component")
//					.id("12345")
//					.lastUpdated("2020-02-27T19:49:08Z")
//					.product("product")
//					.severity("severity")
//					.status("status")
//					.summary("summary")
//					.build())).build();
//
//		Mockito.when(bugsController.getBugsByUsername("Someone")).thenReturn(new ResponseEntity<>(mockBugResponse.getBugs(), HttpStatus.OK));
//
//		ResponseEntity<List<Bug>> response = bugsController.getBugsByUsername("Someone");
//
//		Mockito.verify(bugsController).getBugsByUsername("Someone");
//		Assertions.assertThat(response).isNotNull();
//		Assertions.assertThat(response.getBody().size()).isEqualTo(1);
	}

	@Test
	public void bugComments() {
		List<Comment> mockComments = Arrays.asList(Comment.builder()
			.creator("Person")
			.text("A comment")
			.time("2020-06-06T09:19:37Z")
			.build());

		Mockito.when(bugsController.getCommentsForBug("12345")).thenReturn(new ResponseEntity<>(mockComments, HttpStatus.OK));

		ResponseEntity<List<Comment>> response = bugsController.getCommentsForBug("12345");

		Mockito.verify(bugsController).getCommentsForBug("12345");
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getBody().size()).isEqualTo(1);
	}
}
