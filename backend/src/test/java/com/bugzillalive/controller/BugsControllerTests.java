package com.bugzillalive.controller;

import com.bugzillalive.model.Bug;
import com.bugzillalive.model.BugResponse;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(BugsController.class)
@AutoConfigureMockMvc
@ComponentScan({
	"com.bugzillalive"
})
public class BugsControllerTests {

	@Mock
	private BugsController bugsController;

	@Test
	public void bugsByNumbers() {
		BugResponse mockBugResponse = BugResponse.builder()
			.bugs(Arrays.asList(
				Bug.builder()
					.assignedTo("Someone")
					.component("Component")
					.id("12345")
					.lastUpdated("2020-02-27T19:49:08Z")
					.product("product")
					.severity("severity")
					.status("status")
					.summary("summary")
					.build())).build();

		Mockito.when(bugsController.getBugsByNumbers("12345")).thenReturn(new ResponseEntity<>(mockBugResponse.getBugs(), HttpStatus.OK));

		ResponseEntity<List<Bug>> response = bugsController.getBugsByNumbers("12345");

		Mockito.verify(bugsController).getBugsByNumbers("12345");
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getBody().size()).isEqualTo(1);
	}

	@Test
	public void bugsByUsername() {
		BugResponse mockBugResponse = BugResponse.builder()
			.bugs(Arrays.asList(
				Bug.builder()
					.assignedTo("Someone")
					.component("Component")
					.id("12345")
					.lastUpdated("2020-02-27T19:49:08Z")
					.product("product")
					.severity("severity")
					.status("status")
					.summary("summary")
					.build())).build();

		Mockito.when(bugsController.getBugsByUsername("Someone")).thenReturn(new ResponseEntity<>(mockBugResponse.getBugs(), HttpStatus.OK));

		ResponseEntity<List<Bug>> response = bugsController.getBugsByUsername("Someone");

		Mockito.verify(bugsController).getBugsByUsername("Someone");
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getBody().size()).isEqualTo(1);
	}
}
