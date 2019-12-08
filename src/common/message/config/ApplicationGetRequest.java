package common.message.config;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * A request message for application properties.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class ApplicationGetRequest extends ConfigRequest
{	
	public ApplicationGetRequest()
	{
		this.setMessage("configrequest");
		this.setFileExtension(".configrequest");
		this.setOperation("get");
		this.setPropertyType("application");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{		
		JSONArray propertiesArray = new JSONArray();
		JSONObject property = new JSONObject();
		
		property.put("refreshrate", "");
		property.put("version", "");
		property.put("bugzillaurl", "");
		property.put("windowcolour", "");		
		property.put("windowtextcolour", "");	
		property.put("criticalcolour", "");
		property.put("highcolour", "");
		property.put("mediumcolour", "");
		property.put("lowcolour", "");
		property.put("unknowncolour", "");
		property.put("codedcolour", "");
		property.put("builtcolour", "");
		property.put("releasedcolour", "");
		property.put("addressedcolour", "");
		property.put("fixedcolour", "");
		property.put("closedcolour", "");
		property.put("nofaultcolour", "");			
		property.put("infopanebackgroundcolour", "");
		property.put("infopaneheadingcolour", "");
		property.put("infopanesubheadingcolour", "");
		
		propertiesArray.add(property);

		JSONObject message = new JSONObject();
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("propertyType", this.getPropertyType());
		message.put("properties", propertiesArray);

		return message.toJSONString();
	}
}
