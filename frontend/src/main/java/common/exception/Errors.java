package common.exception;

/**
 * A set of different error messages.
 *
 * @author Tom Hewitt
 * @since 2.0.0
 */
public class Errors 
{
	public static final String BROWSER 			= "There was a problem trying to use the browser.";
	public static final String CREATE_LIST 		= "Could not create new list.";
	public static final String HELP 			= "Could not open Help.";
	public static final String COMMENTS 		= "Could not open comments for bug.";
	public static final String REFRESH 			= "Could not refresh table.";
	public static final String GENERAL 			= "A general error has occurred.";
	public static final String MISSING_FIELD 	= "There is a missing field.";
	public static final String INVALID_BUG		= "Not a valid bug.";
	public static final String EXCEL 			= "Could not export table to Excel.";
	public static final String JACKSON_FROM		= "Problem parsing JSON into Object.";
	public static final String JACKSON_TO		= "Problem turning Object into JSON.";
	public static final String ATTACHMENT 		= "Could not open the attachment.";
	public static final String REQUEST 			= "Could not send request to the backend service.";
}
