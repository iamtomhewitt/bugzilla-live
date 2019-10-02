package bugzilla.message.OR;

import org.json.simple.JSONObject;

/**
 * A request to retrieve ORs for a specific user. This request can be tailored for either a specific user, or by the currently logged in user.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class UserORsRequest extends ORRequest
{
	private String user;

	public UserORsRequest(String userToGetORsFor, String username, String password, String apiKey)
	{
		this.setMessage("orrequest");
		this.setFileExtension(".orrequest");
		this.setOperation("user");
		this.setUsername(username);
		this.setPassword(password);
		this.setApiKey(apiKey);
		this.user = userToGetORsFor;
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
