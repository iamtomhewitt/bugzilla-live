package com.bugzillalive;

import com.bugzillalive.controller.ListController;
import com.bugzillalive.exception.ListAlreadyExistsException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.model.mongo.BugList;
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
public class ListTests {

	@InjectMocks
	private ListController controller;

	@Mock
	private ListService mockListService;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private BugList mockBugList;
	private BugList mockCurrentBugList;
	private List<BugList> mockBugLists;

	private final String LIST_NAME = "mock-list";

	@Before
	public void eachTest() {
		MockitoAnnotations.initMocks(this);

		mockBugList = BugList.builder()
			.content("12345")
			.name("mock-list")
			.isCurrent(false)
			.build();

		mockCurrentBugList = BugList.builder()
			.content("44556")
			.name("mock-current-list")
			.isCurrent(true)
			.build();

		mockBugLists = Arrays.asList(mockBugList, mockCurrentBugList);
	}

	@Test
	public void shouldReturnASingleBugList() throws Exception {
		when(mockListService.getList(LIST_NAME)).thenReturn(mockBugList);

		BugList list = controller.getList("mock-list").getBody();

		assertThat(list.getName(), is(LIST_NAME));
		assertThat(list.getContent(), is("12345"));
		assertThat(list.isCurrent(), is(false));
	}

	@Test
	public void shouldReturnMultipleBugLists() {
		when(mockListService.getAllLists()).thenReturn(mockBugLists);

		List<BugList> lists = controller.getAllList().getBody();

		assertThat(lists.size(), is(2));
	}

	@Test
	public void shouldThrowExceptionWhenGettingANonExistentList() throws ListNotFoundException {
		expectedException.expect(ListNotFoundException.class);
		expectedException.expectMessage("mock-list could not be found");
		when(mockListService.getList(LIST_NAME)).thenThrow(new ListNotFoundException(LIST_NAME));

		controller.getList(LIST_NAME);
	}

	@Test
	public void shouldSaveAList() throws ListAlreadyExistsException {
		ResponseEntity<Map<String, String>> response = controller.saveList(mockBugList);

		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().get("message"), is("Saved"));
	}

	@Test
	public void shouldThrowExceptionWhenTryingToSaveAListWithTheSameName() throws ListAlreadyExistsException {
		expectedException.expect(ListAlreadyExistsException.class);
		expectedException.expectMessage("Cannot save list, 'mock-list' already exists");
		doThrow(new ListAlreadyExistsException(LIST_NAME)).when(mockListService).saveList(mockBugList);

		controller.saveList(mockBugList);
	}

	@Test
	public void shouldUpdateAList() throws ListNotFoundException, ListAlreadyExistsException {
		BugList updatedList = BugList.builder()
			.content("new content")
			.name("mock-list")
			.isCurrent(false)
			.build();
		when(mockListService.getList(LIST_NAME)).thenReturn(updatedList);

		ResponseEntity<Map<String, String>> updatedResponse = controller.updateList(updatedList);
		BugList listResponse = controller.getList("mock-list").getBody();

		assertThat(updatedResponse.getStatusCode(), is(HttpStatus.OK));
		assertThat(updatedResponse.getBody().get("message"), is("Updated"));
		assertThat(listResponse.getContent(), is("new content"));
	}

	@Test
	public void shouldThrowExceptionWhenTryingToUpdateAListWithTheSameName() throws ListAlreadyExistsException {
		expectedException.expect(ListAlreadyExistsException.class);
		expectedException.expectMessage("Cannot save list, 'mock-list' already exists");
		doThrow(new ListAlreadyExistsException(LIST_NAME)).when(mockListService).updateList(mockBugList);

		controller.updateList(mockBugList);
	}

	@Test
	public void shouldDeleteList() throws ListNotFoundException {
		ResponseEntity<Map<String, String>> response = controller.deleteList(mockBugList.getName());

		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().get("message"), is("Deleted"));
	}

	@Test
	public void shouldThrowExceptionWhenTryingToDeleteANonExistentList() throws ListNotFoundException {
		expectedException.expect(ListNotFoundException.class);
		expectedException.expectMessage("mock-list could not be found");
		doThrow(new ListNotFoundException(LIST_NAME)).when(mockListService).deleteList(mockBugList.getName());

		controller.deleteList(mockBugList.getName());
	}
}
