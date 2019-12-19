package bugzilla.message.document;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import bugzilla.common.bug.Bug;
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
	private List<Bug> bugs;
	
	public ExcelRequest(List<Bug> bugs)
	{
		this.setMessage("documentrequest");
		this.setFileExtension(".documentrequest");
		this.setOperation("excel");
		this.bugs = bugs;
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
	
			if (this.bugs != null)
			{
				String bugsAsJson = JacksonAdapter.toJson(this.bugs);
	
				// First parse bugsAsJson so that it formats correctly when it is written to the file
				JSONParser parser = new JSONParser();
				JSONArray bugs = (JSONArray) parser.parse(bugsAsJson);
	
				message.put("bugs", bugs);
			}
	
			return message.toJSONString();
		}
		catch (Exception e)
		{
			throw new JsonTransformationException(e.getMessage());
		}
	}
}
