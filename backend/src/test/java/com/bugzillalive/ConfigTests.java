package com.bugzillalive;

import com.bugzillalive.controller.ConfigController;
import com.bugzillalive.controller.ListController;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ConfigSaveException;
import com.bugzillalive.exception.ListAlreadyExistsException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.model.mongo.BugList;
import com.bugzillalive.model.mongo.UserConfig;
import com.bugzillalive.service.ConfigService;
import com.bugzillalive.service.ListService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ConfigTests {

	@InjectMocks
	private ConfigController controller;

	@Mock
	private ConfigService mockService;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private UserConfig mockConfig;

	@Before
	public void eachTest() {
		MockitoAnnotations.initMocks(this);

		mockConfig = UserConfig.builder()
			.bugzillaUrl("url")
			.build();
	}

	@Test
	public void shouldReturnConfig() throws Exception {
		when(mockService.getConfig()).thenReturn(mockConfig);

		UserConfig config = controller.getConfig().getBody();

		assertThat(config.getBugzillaUrl(), is("url"));
	}

	@Test
	public void shouldThrowExceptionWhenGettingNonExistentConfig() throws Exception {
		expectedException.expect(ConfigNotFoundException.class);
		expectedException.expectMessage("No config recorded in the database. Perhaps you need to save some config first?");
		when(mockService.getConfig()).thenThrow(new ConfigNotFoundException());

		controller.getConfig();
	}

	@Test
	public void shouldSaveConfig() throws ConfigSaveException, ConfigNotFoundException {
		ResponseEntity<Map<String, String>> response = controller.saveConfig(mockConfig);

		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().get("message"), is("Saved"));
	}

	@Test
	public void shouldThrowExceptionWhenTryingToSaveConfig() throws ConfigSaveException, ConfigNotFoundException {
		expectedException.expect(ConfigSaveException.class);
		expectedException.expectMessage("test exception");
		doThrow(new ConfigSaveException("test exception")).when(mockService).saveConfig(mockConfig);

		controller.saveConfig(mockConfig);
	}

	@Test
	public void shouldUpdateConfig() throws Exception {
		UserConfig updatedConfig = UserConfig.builder()
			.bugzillaUrl("new url")
			.build();
		when(mockService.getConfig()).thenReturn(updatedConfig);

		ResponseEntity<Map<String, String>> updatedResponse = controller.updateConfig(updatedConfig);
		UserConfig listResponse = controller.getConfig().getBody();

		assertThat(updatedResponse.getStatusCode(), is(HttpStatus.OK));
		assertThat(updatedResponse.getBody().get("message"), is("Updated"));
		assertThat(listResponse.getBugzillaUrl(), is("new url"));
	}

	@Test
	public void shouldThrowExceptionWhenTryingToUpdateConfigThatDoesntExist() throws ConfigNotFoundException, ConfigSaveException {
		expectedException.expect(ConfigNotFoundException.class);
		expectedException.expectMessage("No config recorded in the database. Perhaps you need to save some config first?");
		doThrow(new ConfigNotFoundException()).when(mockService).updateConfig(mockConfig);

		controller.updateConfig(mockConfig);
	}
}
