package common.common.bug;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import common.common.Folders;

/**
 * Data class of an attachment in a bug.
 *
 * @author Tom Hewitt
 * @since 2.0.0
 */
public class BugAttachment
{
	private String id;
	private String description;
	private String filename;
	private String data;

	public BugAttachment()
	{
		// Used for Jackson
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}
	
	/**
	 * Opens the specified attachment by converting the base64 data into a new file, which is then opened.
	 */
	public void open() throws IOException
	{
		String filePath = Folders.ATTACHMENTS_FOLDER + this.filename;

		try
		{
			// Decode the Base64 data, and write it into a file
			FileOutputStream fos = new FileOutputStream(filePath);
			fos.write(Base64.getDecoder().decode(this.getData()));
			fos.close();
			
			// Now try and open it on the desktop
			Desktop.getDesktop().open(new File(filePath));
		} 
		catch (IOException e)
		{
			// There may not be a program that cannot 'open' the file, so try editing it first before throwing a true exception.
			Desktop.getDesktop().edit(new File(filePath));
		}
	}

	@Override
	public String toString()
	{
		return "ID: " + this.id + "\nDescription: " + this.description + "\nFilename: " + this.filename + "\nData: " + this.data.substring(0, 10) + "...";
	}
}
