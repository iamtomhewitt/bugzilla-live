package common.error;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import common.error.Errors;

public class ErrorTests
{
	@Test
	public void correctBrowserErrorMessage()
	{
		assertEquals("There was a problem trying to use the browser.", Errors.BROWSER);
	}
	
	@Test
	public void correctCreateListErrorMessage()
	{
		assertEquals("Could not create new list.", Errors.CREATE_LIST);
	}
	
	@Test
	public void correctHelpErrorMessage()
	{
		assertEquals("Could not open Help.", Errors.HELP);
	}
	
	@Test
	public void correctCommentsErrorMessage()
	{
		assertEquals("Could not open comments for bug.", Errors.COMMENTS);
	}
	
	@Test
	public void correctRefreshErrorMessage()
	{
		assertEquals("Could not refresh table.", Errors.REFRESH);
	}
	
	@Test
	public void correctGeneralErrorMessage()
	{
		assertEquals("A general error has occurred.", Errors.GENERAL);
	}
	
	@Test
	public void correctMissingFieldErrorMessage()
	{
		assertEquals("There is a missing field.", Errors.MISSING_FIELD);
	}
	
	@Test
	public void correctInvalidBugErrorMessage()
	{
		assertEquals("Not a valid bug.", Errors.INVALID_BUG);
	}
	
	@Test
	public void correctExcelErrorMessage()
	{
		assertEquals("Could not export table to Excel.", Errors.EXCEL);
	}
	
	@Test
	public void correctJacksonToJsonErrorMessage()
	{
		assertEquals("Problem turning Object into JSON.", Errors.JACKSON_TO);
	}
	
	@Test
	public void correctJacksonFromJsonErrorMessage()
	{
		assertEquals("Problem parsing JSON into Object.", Errors.JACKSON_FROM);
	}
	
	@Test
	public void correctAttachmentErrorMessage()
	{
		assertEquals("Could not open the attachment.", Errors.ATTACHMENT);
	}
	
	@Test
	public void correctRequestErrorMessage()
	{
		assertEquals("Could not send request to the backend service.", Errors.REQUEST);
	}
	
	@Test
	public void correctCannotConnectErrorMessage()
	{
		assertEquals("Cannot connect to the backend service. Please check it is running, and restart Bugzilla Live.", Errors.CANNOT_CONNECT);
	}
	
	@Test
	public void correctInvalidEmailErrorMessage()
	{
		assertEquals("Email is invalid.", Errors.INVALID_EMAIL);
	}
}
