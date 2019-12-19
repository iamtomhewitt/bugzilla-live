package bugzilla.message.document;

import bugzilla.message.Message;

/**
 * An abstract request class for all types of document requests.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
abstract class DocumentRequest extends Message
{
	private String fileLocation;
	private String filename;
	private String documentTitle;
	private String classification;

	public String getFileLocation()
	{
		return fileLocation;
	}

	public void setFileLocation(String fileLocation)
	{
		this.fileLocation = fileLocation;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getDocumentTitle()
	{
		return documentTitle;
	}

	public void setDocumentTitle(String documentTitle)
	{
		this.documentTitle = documentTitle;
	}

	public String getClassification()
	{
		return classification;
	}

	public void setClassification(String classification)
	{
		this.classification = classification;
	}
}
