package bugzilla.message.list;

import org.json.simple.JSONObject;

/**
 * A request to create a list with a specified filename and contents.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class CreateListRequest extends ListRequest
{
	private String fileContents;
	
	public CreateListRequest(String filename, String fileContents)
	{
		this.setMessage("listrequest");
		this.setFileExtension(".listrequest");
		this.setOperation("create");
		this.setFilename(filename);
		this.fileContents = fileContents;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{
		JSONObject message = new JSONObject();
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("filename", this.getFilename());
		message.put("filecontents", this.fileContents);

		return message.toJSONString();
	}
}
