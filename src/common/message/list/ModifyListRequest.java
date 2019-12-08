package common.message.list;

import org.json.simple.JSONObject;

/**
 * A request message to modify a specified list.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class ModifyListRequest extends ListRequest
{
	private String append;
	private String remove;

	public ModifyListRequest(String filename, String append, String remove)
	{
		this.setMessage("listrequest");
		this.setFileExtension(".listrequest");
		this.setOperation("modify");
		this.setFilename(filename);
		this.append = append;
		this.remove = remove;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{
		JSONObject message = new JSONObject();
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("filename", this.getFilename());
		message.put("append", append);
		message.put("remove", remove);

		return message.toJSONString();
	}
}
