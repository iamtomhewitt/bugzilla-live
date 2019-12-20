package common.error;

import org.junit.Test;

import common.bug.Bug;
import common.exception.JsonTransformationException;
import common.message.ApiRequestor;
import common.utility.JacksonAdapter;

public class ExceptionTests
{
	@Test(expected = Exception.class)
	public void requestIncorrectUrlThrowsError() throws Exception
	{
		String url = "some incorrect url";
		ApiRequestor.request(url);
	}

	@Test(expected = JsonTransformationException.class)
	public void incorrectJsonThrowsCorrectException() throws JsonTransformationException
	{
		String json = "incorrect json";
		JacksonAdapter.fromJson(json, Bug.class);
	} 
}