package bugzilla.exception;

/**
 * An exception to be thrown when there is a problem generating a document in the document service.
 * 
 * @author Tom Hewitt
 */
public class GenerateDocumentException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public GenerateDocumentException(String message)
	{
		super(message);
	}
}