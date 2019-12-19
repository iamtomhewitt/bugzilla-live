package log;

import java.io.File;

import bugzilla.common.Folders;
import bugzilla.common.Logger;

/**
 * Logs the document service.
 * 
 * @author Tom Hewitt
 * @since 2.3.0
 */
public class DocumentLogger extends Logger
{	
	private static DocumentLogger instance = new DocumentLogger();
	
	public static DocumentLogger getInstance()
	{
		return instance;
	}
	
	public DocumentLogger()
	{
		this.setDirectory(Folders.DOCUMENT_SERVICE_FOLDER);
		this.setLogFile(new File(this.getDirectory()+this.getFilename()));
	}
}
