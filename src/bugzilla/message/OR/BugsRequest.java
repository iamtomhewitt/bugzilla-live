package bugzilla.message.OR;

import java.util.List;

import org.json.simple.JSONObject;

/**
 * A request to get ORs for a set of numbers. <p>
 * For example, specify "12345,12334,12445" to retrieve details for OR12345, OR12334 and OR12445.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class ORsRequest extends ORRequest
{
	// List of numbers as single comma separated value
	private String numberList = "";

	public ORsRequest(List<String> numbers, String username, String password, String apiKey)
	{
		this.setMessage("orrequest");
		this.setFileExtension(".orrequest");
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
