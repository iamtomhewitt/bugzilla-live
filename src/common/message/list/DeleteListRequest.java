package common.message.list;

import org.json.simple.JSONObject;

/**
 * A request to delete a specified list.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class DeleteListRequest extends ListRequest
{
	public DeleteListRequest(String filename)
	{
		this.setMessage("listrequest");
		this.setFileExtension(".listrequest");
		this.setOperation("delete");
		this.setFilename(filename);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{
		JSONObject message = new JSONObject();
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("filename", this.getFilename());

		return message.toJSONString();
	}
}
