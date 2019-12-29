package common.message;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import common.error.RequestException;

public class MessageTests 
{	
	@Test
	public void correctGithubReleasesEndpoint()
	{
		assertEquals("http://api.github.com/repos/iamtomhewitt/bugzilla-live/releases", Endpoints.GITHUB_RELEASES);
	}
	
	@Test
	public void correctListContentsEndpoint() throws Exception
	{
		assertEquals("http://localhost:3001/list/test/contents", Endpoints.LIST_CONTENTS("test"));
	}
	
	@Test
	public void correctListModifyEndpoint() throws Exception
	{
		assertEquals("http://localhost:3001/list/modify?name=test&add=1&remove=2", Endpoints.LIST_MODIFY("test", "1", "2"));
	}

	@Test
	public void correctListAddEndpoint() throws Exception
	{
		assertEquals("http://localhost:3001/list/add?name=test&contents=12345", Endpoints.LIST_ADD("test", "12345"));
	}
	
	@Test
	public void correctListDeleteEndpoint() throws Exception
	{
		assertEquals("http://localhost:3001/list/delete?name=test", Endpoints.LIST_DELETE("test"));
	}

	@Test
	public void correctListsEndpoint() throws UnsupportedEncodingException
	{
		assertEquals("http://localhost:3001/list/lists", Endpoints.LISTS);
	}

	@Test
	public void correctBugNumbersEndpoint() throws UnsupportedEncodingException
	{
		assertEquals("http://localhost:3001/bugs/numbers?numbers=12345", Endpoints.BUGS_NUMBERS("12345"));
	}

	@Test
	public void correctBugEmailEndpoint() throws UnsupportedEncodingException
	{
		assertEquals("http://localhost:3001/bugs/email?email=someone@example.com", Endpoints.BUGS_EMAIL("someone@example.com"));
	}

	@Test
	public void correctBugAttachmentEndpoint() throws UnsupportedEncodingException
	{
		assertEquals("http://localhost:3001/bugs/12345/attachments", Endpoints.BUGS_ATTACHMENTS("12345"));
	}

	@Test
	public void correctBugCommentsEndpoint() throws UnsupportedEncodingException
	{
		assertEquals("http://localhost:3001/bugs/12345/comments", Endpoints.BUGS_COMMENTS("12345"));
	}

	@Test
	public void correctConfigSaveEndpoint() throws Exception
	{
		assertEquals("http://localhost:3001/config/save?key=someKey&value=someValue", Endpoints.CONFIG_SAVE("someKey", "someValue"));
	}

	@Test
	public void correctConfigGetEndpoint() throws UnsupportedEncodingException
	{
		assertEquals("http://localhost:3001/config/get", Endpoints.CONFIG_GET);
	}

	@Test
	public void correctWikiEndpoint() throws UnsupportedEncodingException
	{
		assertEquals("https://github.com/iamtomhewitt/bugzilla-live/wiki", Endpoints.GITHUB_WIKI);
	}

	@Test
	public void correctAddCommentEndpoint() throws RequestException
	{
		assertEquals("http://localhost:3001/bugs/12345/comments/add?apiKey=key&comment=Comment", Endpoints.BUGS_ADD_COMMENTS("12345", "Comment", "key"));
	}
}
