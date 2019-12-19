package bugzilla.exception;

/**
 * An exception to be thrown when processing the contents of a received message.
 * 
 * @author Tom Hewitt
 */
public class MessageProcessorException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	public MessageProcessorException(String message)
	{
		super(message);
	}
}
