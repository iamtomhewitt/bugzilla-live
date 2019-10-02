package bugzilla.message.bug;

import org.json.simple.JSONObject;

/**
 * A request for detail of a bug (attachments and comments).
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class BugDetailRequest extends BugRequest
{
	private String number;
	
	public BugDetailRequest(String number, String username, String password, String apiKey)
	{
		this.setMessage("bugrequest");
		this.setFileExtension(".bugrequest");
		this.setOperation("detail");
		this.setUsername(username);
		this.setPassword(password);
		this.setApiKey(apiKey);
		this.number = number;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{
		JSONObject message = new JSONObject();
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("number", this.number);
		message.put("username", this.getUsername());
		message.put("password", this.getPassword());
		message.put("apiKey", this.getApiKey());

		return message.toJSONString();
	}
}
