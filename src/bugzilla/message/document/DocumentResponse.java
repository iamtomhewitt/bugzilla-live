package bugzilla.message.document;

import org.json.simple.JSONObject;

import bugzilla.message.Message;

/**
 * A response message for created documents, which includes the file path of the created document.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class DocumentResponse extends Message
{
	private String successful;
	private String failureReason;
	private String filePath;
	
	public DocumentResponse(boolean successful, String failureReason, String filePath)
	{
		this.setMessage("documentresponse");
		this.setFileExtension(".documentresponse");
		this.setOperation("documentresponse");
		this.successful = successful ? "yes" : "no";
		this.failureReason = failureReason;
		this.filePath = filePath;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{
		JSONObject message = new JSONObject();
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("successful", this.successful);
		message.put("failurereason", this.failureReason);
		message.put("filepath", this.filePath);
		
		return message.toJSONString();
	}
}
