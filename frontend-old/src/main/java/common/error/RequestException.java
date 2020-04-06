package common.error;

/**
 * A custom exception to be used when using the ApiRequestor.
 * 
 * @author Tom Hewitt
 */
public class RequestException extends Exception 
{
	private static final long serialVersionUID = 1L;
		
	public RequestException(String message) 
	{
		super(message);
	}
}
