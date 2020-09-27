package com.bugzillalive;

import com.bugzillalive.controller.BugsController;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ConfigSaveException;
import com.bugzillalive.model.bug.Attachment;
import com.bugzillalive.model.bug.Bug;
import com.bugzillalive.model.bug.Comment;
import com.bugzillalive.model.mongo.UserConfig;
import com.bugzillalive.model.response.BugsResponse;
import com.bugzillalive.repository.ConfigRepository;
import com.bugzillalive.service.BugsService;
import com.bugzillalive.service.ConfigService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.bugzillalive.TestData.BUG_ATTACHMENT;
import static com.bugzillalive.TestData.BUG_COMMENT;
import static com.bugzillalive.model.request.OutboundEndpoint.*;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BugTests {

	private BugsController controller;
	private BugsService mockBugService;
	private ConfigService configService;
	private ConfigRepository configRepository;

	@MockBean
	private RestTemplate restTemplate;

	private final String MOCK_URL = "test";

	@Before
	public void eachTest() throws ConfigSaveException, ConfigNotFoundException {
		configRepository = mock(ConfigRepository.class);
		configService = new ConfigService(configRepository);
		mockBugService = new BugsService(restTemplate, configService);
		controller = new BugsController(mockBugService);


		when(configService.getConfig())
			.thenReturn(UserConfig.builder().bugzillaUrl(MOCK_URL).build());
	}

	private Bug aBug(String number) {
		return Bug.builder()
			.assignedTo("someone")
			.component("component")
			.id(number)
			.lastUpdated("2020-04-13T10:15:51Z")
			.product("product")
			.severity("severity")
			.status("status")
			.summary("summary")
			.build();
	}

	@Test
	public void shouldReturnBug() throws Exception {
		when(restTemplate.getForObject(BUGS_FOR_NUMBERS.uri(MOCK_URL, "12345"), BugsResponse.class))
			.thenReturn(BugsResponse.builder().bugs(asList(aBug("12345"))).build());

		List<Bug> bugs = controller.getBugsByNumbers("12345").getBody();

		assertThat(bugs.isEmpty(), is(false));
		assertThat(bugs.get(0).getId(), is("12345"));
	}

	@Test
	public void shouldReturnMultipleBugs() throws Exception {
		List<Bug> mockBugs = asList(aBug("12345"), aBug("12346"));
		when(restTemplate.getForObject(BUGS_FOR_NUMBERS.uri(MOCK_URL, "12345,12346"), BugsResponse.class))
			.thenReturn(BugsResponse.builder().bugs(mockBugs).build());

		List<Bug> bugs = controller.getBugsByNumbers("12345,12346").getBody();

		assertThat(bugs.isEmpty(), is(false));
		assertThat(bugs.size(), is(2));
		assertThat(bugs.get(0).getId(), is("12345"));
		assertThat(bugs.get(1).getId(), is("12346"));
	}

	@Test
	public void shouldReturnBugsForUser() throws Exception {
		List<Bug> mockBugs = asList(aBug("12345"), aBug("12346"));
		when(restTemplate.getForObject(BUGS_FOR_USER.uri(MOCK_URL, "someone@gmail.com"), BugsResponse.class))
			.thenReturn(BugsResponse.builder().bugs(mockBugs).build());

		List<Bug> bugs = controller.getBugsByUsername("someone@gmail.com").getBody();

		assertThat(bugs.isEmpty(), is(false));
		assertThat(bugs.size(), is(2));
		assertThat(bugs.get(0).getId(), is("12345"));
		assertThat(bugs.get(1).getId(), is("12346"));
	}

	@Test
	public void shouldReturnCommentsForBug() throws Exception {
		when(restTemplate.getForObject(BUG_COMMENTS.uri(MOCK_URL, "12345"), String.class))
			.thenReturn(BUG_COMMENT);

		List<Comment> comments = controller.getCommentsForBug("12345").getBody();

		assertThat(comments.isEmpty(), is(false));
	}

	@Test
	public void shouldReturnAttachmentsForBug() throws Exception {
		when(restTemplate.getForObject(BUG_ATTACHMENTS.uri(MOCK_URL, "12345"), String.class))
			.thenReturn(BUG_ATTACHMENT);

		List<Attachment> attachments = controller.getAttachmentsForBug("12345").getBody();

		assertThat(attachments.isEmpty(), is(false));
	}
}
