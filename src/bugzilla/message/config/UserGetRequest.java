package bugzilla.message.config;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * A request message for getting user specific properties, such as the username.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class UserGetRequest extends ConfigRequest
{	
	public UserGetRequest()
	{
		this.setMessage("configrequest");
		this.setFileExtension(".configrequest");
		this.setOperation("get");
		this.setPropertyType("user");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{		
		JSONArray propertiesArray = new JSONArray();
		JSONObject property = new JSONObject();
		
		property.put("username", "");
		property.put("password", "");
		property.put("api_key", "");
		
		propertiesArray.add(property);

		JSONObject message = new JSONObject();
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("propertyType", this.getPropertyType());
		message.put("properties", propertiesArray);

		return message.toJSONString();
	}
}
