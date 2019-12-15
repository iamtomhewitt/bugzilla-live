package common.message;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Endpoints
{
	private static final String localhost = "http://localhost:";
	private static final int port = 3001;

	public static final String GITHUB_RELEASES = "http://api.github.com/repos/iamtomhewitt/bugzilla-live/releases";

	public static final String LIST_CONTENTS(String listName) throws UnsupportedEncodingException
	{
		return String.format(localhost + port + "/list/%s/contents", URLEncoder.encode(listName, "UTF-8"));
	}

	public static final String LIST_MODIFY(String filename, String add, String remove) throws UnsupportedEncodingException
	{
		return String.format(localhost + port + "/list/modify?name=%s&add=%s&remove=%s", URLEncoder.encode(filename, "UTF-8"), add, remove);
	}

	public static final String LIST_ADD(String filename, String contents) throws UnsupportedEncodingException
	{
		return String.format(localhost + port + "/list/add?name=%s&contents=%s", URLEncoder.encode(filename, "UTF-8"), URLEncoder.encode(contents, "UTF-8"));
	}

	public static final String LIST_DELETE(String filename) throws UnsupportedEncodingException
	{
		return String.format(localhost + port + "/list/delete?name=%s", URLEncoder.encode(filename, "UTF-8"));
	}

	public static final String LISTS = localhost + port + "/list/lists";

	public static final String BUGS_NUMBERS(String numbers)
	{
		return String.format(localhost + port + "/bugs/numbers?numbers=%s", numbers);
	}

	public static final String BUGS_EMAIL(String email)
	{
		return String.format(localhost + port + "/bugs/email?email=%s", email);
	}

	public static final String BUGS_ATTACHMENTS(String number)
	{
		return String.format(localhost + port + "/bugs/%s/attachments", number);
	}

	public static final String BUGS_COMMENTS(String number)
	{
		return String.format(localhost + port + "/bugs/%s/comments", number);
	}

	public static final String CONFIG_SAVE(String key, String value)
	{
		return String.format(localhost + port + "/config/save?key=%s&value=%s", key, value);
	}

	public static final String CONFIG_GET = localhost + port + "/config/get";
}
