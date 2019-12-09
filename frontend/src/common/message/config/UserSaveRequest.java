package common.message.config;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * A request message for saving user properties to file.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class UserSaveRequest extends ConfigRequest
{
	public UserSaveRequest(Map<String, String> properties)
	{
		this.setMessage("configrequest");
		this.setFileExtension(".configrequest");
		this.setOperation("save");
		this.setPropertyType("user");
		this.setProperties(properties);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{
		JSONArray propertiesArray = new JSONArray();
		JSONObject property = new JSONObject();

		Iterator<Entry<String, String>> iterator = this.getProperties().entrySet().iterator();
		while (iterator.hasNext())
		{
			Map.Entry<String, String> pair = (Map.Entry<String, String>) iterator.next();
			property.put(pair.getKey(), pair.getValue());
			iterator.remove();
		}

		propertiesArray.add(property);

		JSONObject message = new JSONObject();
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("propertyType", this.getPropertyType());
		message.put("properties", propertiesArray);

		return message.toJSONString();
	}
}
