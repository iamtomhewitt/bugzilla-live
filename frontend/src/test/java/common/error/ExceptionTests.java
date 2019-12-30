package common.error;

import org.junit.Test;

import common.bug.Bug;
import common.error.JsonTransformationException;
import common.message.ApiRequestor;
import common.message.ApiRequestor.ApiRequestType;
import common.utility.JacksonAdapter;

public class ExceptionTests
{
	@Test(expected = RequestException.class)
	public void requestIncorrectUrlThrowsError() throws RequestException
	{
		String url = "some incorrect url";
		ApiRequestor.request(ApiRequestType.GET, url);
	}

	@Test(expected = JsonTransformationException.class)
	public void incorrectJsonThrowsCorrectException() throws JsonTransformationException
	{
		String json = "incorrect json";
		JacksonAdapter.fromJson(json, Bug.class);
	} 
}