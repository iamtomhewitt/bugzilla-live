package bugzilla.message.document;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import bugzilla.common.OR.OR;
import bugzilla.exception.JsonTransformationException;
import bugzilla.utilities.JacksonAdapter;

/**
 * A request to create an Excel document.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class ExcelRequest extends DocumentRequest
{
	private List<OR> ors;
	
	public ExcelRequest(List<OR> ors)
	{
		this.setMessage("documentrequest");
		this.setFileExtension(".documentrequest");
		this.setOperation("excel");
		this.ors = ors;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJson() throws JsonTransformationException 
	{
		try
		{
			JSONObject message = new JSONObject();
			message.put("message", this.getMessage());
			message.put("operation", this.getOperation());
	
			if (this.ors != null)
			{
				String ORsAsJson = JacksonAdapter.toJson(this.ors);
	
				// First parse ORsAsJson so that it formats correctly when it is written to the file
				JSONParser parser = new JSONParser();
				JSONArray ORs = (JSONArray) parser.parse(ORsAsJson);
	
				message.put("ORs", ORs);
			}
	
			return message.toJSONString();
		}
		catch (Exception e)
		{
			throw new JsonTransformationException(e.getMessage());
		}
	}
}
