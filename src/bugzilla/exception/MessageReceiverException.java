package bugzilla.exception;

/**
 * An exception to be thrown when there is a problem receiving messages from the messages folder.
 * 
 * @author Tom Hewitt
 */
public class MessageReceiverException extends Exception
{
	private static final long serialVersionUID = 1L;

	public MessageReceiverException(String message) 
	{
		super(message);
	}
}
