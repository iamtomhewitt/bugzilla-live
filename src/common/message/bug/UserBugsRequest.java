package common.message.bug;

import org.json.simple.JSONObject;

/**
 * A request to retrieve bugs for a specific user. This request can be tailored for either a specific user, or by the currently logged in user.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class UserBugsRequest extends BugRequest
{
	private String user;

	public UserBugsRequest(String userToGetBugsFor, String username, String password, String apiKey)
	{
		this.setMessage("bugrequest");
		this.setFileExtension(".bugrequest");
		this.setOperation("user");
		this.setUsername(username);
		this.setPassword(password);
		this.setApiKey(apiKey);
		this.user = userToGetBugsFor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{
		JSONObject message = new JSONObject();
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("user", this.user);
		message.put("username", this.getUsername());
		message.put("password", this.getPassword());
		message.put("apiKey", this.getApiKey());

		return message.toJSONString();
	}
}
