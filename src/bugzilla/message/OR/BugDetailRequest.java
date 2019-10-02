package bugzilla.message.OR;

import org.json.simple.JSONObject;

/**
 * A request for detail of an OR (attachments and comments).
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class ORDetailRequest extends ORRequest
{
	private String number;
	
	public ORDetailRequest(String number, String username, String password, String apiKey)
	{
		this.setMessage("orrequest");
		this.setFileExtension(".orrequest");
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
