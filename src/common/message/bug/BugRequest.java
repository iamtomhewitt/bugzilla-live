package common.message.bug;

import common.message.Message;

/**
 * A base request class for all kinds of bug requests.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public abstract class BugRequest extends Message
{
	private String username;
	private String password;
	private String apiKey;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getApiKey()
	{
		return apiKey;
	}

	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}
}
