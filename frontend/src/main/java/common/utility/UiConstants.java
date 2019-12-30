package common.utility;

import common.message.RequestType;

/**
 * Keeps track of specific information to be used when making requests.
 * 
 * @author Tom Hewitt
 */
public class UiConstants
{
	public static String USER_EMAIL;
	public static String APIKEY;
	public static String BUGZILLA_URL;
	public static String PREFILTERED_BUG_DATA;
	public static String BUG_REGEX				= "[0-9]+(,[0-9]+)*";
	public static String EMAIL_REGEX			= "^(.+)@(.+)$";
	public static String CURRENT_LIST;
		
	public static int REFRESH_TIME;
	
	public static RequestType REQUEST_TYPE;
	
	public static boolean PAUSED = false;	
}
