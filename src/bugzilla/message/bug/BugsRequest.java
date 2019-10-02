package bugzilla.message.bug;

import java.util.List;

import org.json.simple.JSONObject;

/**
 * A request to get bugs for a set of numbers. <p>
 * For example, specify "12345,12334,12445" to retrieve details for 12345, 12334 and 12445.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class BugsRequest extends BugRequest
{
	// List of numbers as single comma separated value
	private String numberList = "";

	public BugsRequest(List<String> numbers, String username, String password, String apiKey)
	{
		this.setMessage("bugrequest");
		this.setFileExtension(".bugrequest");
		this.setOperation("numbers");		
		this.setUsername(username);
		this.setPassword(password);
		this.setApiKey(apiKey);
		
		for (String number : numbers)
			this.numberList += number + ",";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{
		JSONObject message = new JSONObject();
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("numbers", this.numberList);
		message.put("username", this.getUsername());
		message.put("password", this.getPassword());
		message.put("apiKey", this.getApiKey());

		return message.toJSONString();
	}
}
