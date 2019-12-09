package common.message.list;

import common.message.Message;

/**
 * A base class for which all list requests are made.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public abstract class ListRequest extends Message
{
	private String filename;

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}
}
