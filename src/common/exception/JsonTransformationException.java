package common.exception;

/**
 * A custom exception to be used when trying to wrap or unwrap objects using the Jackson library.
 * 
 * @author Tom Hewitt
 */
public class JsonTransformationException extends Exception 
{
	private static final long serialVersionUID = 1L;
		
	public JsonTransformationException(String message) 
	{
		super(message);
	}
}
