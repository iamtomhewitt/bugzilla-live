package gui.app.common;

/**
 * Keeps track of specific information to be used when making requests.
 * 
 * @author Tom Hewitt
 */
public class GuiConstants
{
	public static String USERNAME;
	public static String PASSWORD;
	public static String APIKEY;
	public static String BUGZILLA_URL;
	public static String PREFILTERED_BUG_DATA;
	public static String BUG_REGEX				= "[0-9]+(,[0-9]+)*";
	public static String CURRENT_LIST_FILE;
		
	public static int REFRESH_TIME;
	
	public static RequestType REQUEST_TYPE;
	
	public static boolean PAUSED = false;	
}
