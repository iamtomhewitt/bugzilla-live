package bugzilla.message;

import java.util.Calendar;

import bugzilla.exception.JsonTransformationException;

/**
 * A base class that creates a request message for services
 * and then using <code>toJson()</code> converts it to a <code>JSON</code> string.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public abstract class Message
{
	private String message;
	private String operation;
	private String fileExtension;
	
	/**
	 * Converts this message object to JSON.
	 */
	public abstract String toJson() throws JsonTransformationException;
	
	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public String getOperation()
	{
		return operation;
	}
	
	public void setOperation(String operation)
	{
		this.operation = operation;
	}
	
	public String getFileExtension()
	{
		return fileExtension;
	}
	
	public void setFileExtension(String fileExtension)
	{
		this.fileExtension = Calendar.getInstance().getTimeInMillis()+fileExtension;
	}
}
