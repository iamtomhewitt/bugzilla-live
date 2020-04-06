package common.message;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import common.error.RequestException;

public class Endpoints
{
	private static final String localhost = "http://localhost:";
	private static final int port = 3001;

	public static final String GITHUB_RELEASES = "http://api.github.com/repos/iamtomhewitt/bugzilla-live/releases";
	public static final String GITHUB_WIKI = "https://github.com/iamtomhewitt/bugzilla-live/wiki";
	
	public static final String LIST_CONTENTS(String listName) throws RequestException
	{
		try
		{
			return String.format(localhost + port + "/list/%s/contents", URLEncoder.encode(listName, "UTF-8"));
		} 
		catch (UnsupportedEncodingException e)
		{
			throw new RequestException(e.getMessage());
		}
	}

	public static final String LIST_MODIFY(String filename, String add, String remove) throws RequestException
	{
		try
		{
			return String.format(localhost + port + "/list/modify?name=%s&add=%s&remove=%s", URLEncoder.encode(filename, "UTF-8"), add, remove);
		} 
		catch (UnsupportedEncodingException e)
		{
			throw new RequestException(e.getMessage());
		}
	}

	public static final String LIST_ADD(String filename, String contents) throws RequestException
	{
		try
		{
			return String.format(localhost + port + "/list/add?name=%s&contents=%s", URLEncoder.encode(filename, "UTF-8"), URLEncoder.encode(contents, "UTF-8"));
		} 
		catch (UnsupportedEncodingException e)
		{
			throw new RequestException(e.getMessage());
		}
	}

	public static final String LIST_DELETE(String filename) throws RequestException
	{
		try
		{
			return String.format(localhost + port + "/list/delete?name=%s", URLEncoder.encode(filename, "UTF-8"));
		} 
		catch (UnsupportedEncodingException e)
		{
			throw new RequestException(e.getMessage());
		}
	}

	public static final String LISTS = localhost + port + "/list/lists";

	public static final String BUGS_NUMBERS(String numbers)
	{
		return String.format(localhost + port + "/bugs/numbers?numbers=%s", numbers);
	}

	public static final String BUGS_USERNAME(String username)
	{
		return String.format(localhost + port + "/bugs/username?username=%s", username);
	}

	public static final String BUGS_ATTACHMENTS(String number)
	{
		return String.format(localhost + port + "/bugs/%s/attachments", number);
	}

	public static final String BUGS_COMMENTS(String number)
	{
		return String.format(localhost + port + "/bugs/%s/comments", number);
	}

	public static final String BUGS_ADD_COMMENTS(String number, String comment, String apiKey) throws RequestException
	{
		try 
		{
			return String.format(localhost + port + "/bugs/%s/comments/add?apiKey=%s&comment=%s", number, apiKey, URLEncoder.encode(comment, "UTF-8"));
		} 
		catch (UnsupportedEncodingException e) 
		{
			throw new RequestException(e.getMessage());
		}
	}
	
	public static String BUGS_CHANGE_STATUS(String number, String status, String resolution, String comment, String apiKey) throws RequestException 
	{
		try 
		{
			return String.format(localhost + port + "/bugs/%s/status/change?status=%s&resolution=%s&comment=%s&apiKey=%s", number, status, resolution, URLEncoder.encode(comment, "UTF-8"), apiKey);
		} 
		catch (UnsupportedEncodingException e) 
		{
			throw new RequestException(e.getMessage());
		}
	}

	public static final String CONFIG_SAVE(String key, String value) throws RequestException
	{
		try 
		{
			return String.format(localhost + port + "/config/save?key=%s&value=%s", key, URLEncoder.encode(value, "UTF-8"));
		} 
		catch (UnsupportedEncodingException e)
		{
			throw new RequestException(e.getMessage());
		}
	}

	public static final String CONFIG_GET = localhost + port + "/config/get";

	

}
