package bugzilla.message.bug;

import org.json.simple.JSONObject;

/**
 * A request for bugs for a particular subsystem.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class SubsystemBugsRequest extends BugRequest
{
	private String subsystem;

	public SubsystemBugsRequest(String subsystem, String username, String password, String apiKey)
	{
		this.setMessage("bugrequest");
		this.setFileExtension(".bugrequest");
		this.setOperation("subsystem");
		this.setUsername(username);
		this.setPassword(password);
		this.setApiKey(apiKey);
		this.subsystem = subsystem;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{
		JSONObject message = new JSONObject();
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("subsystem", this.subsystem);
		message.put("username", this.getUsername());
		message.put("password", this.getPassword());
		message.put("apiKey", this.getApiKey());

		return message.toJSONString();
	}
}
