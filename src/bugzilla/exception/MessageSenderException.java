package bugzilla.exception;

/**
 * An exception to be thrown when a message cannot be sent to the message folder.
 * 
 * @author Tom Hewitt
 */
public class MessageSenderException extends Exception
{
	private static final long serialVersionUID = 1L;

	public MessageSenderException(String message) 
	{
		super(message);
	}
}
