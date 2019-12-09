package gui.app.theme;

public class RowColours
{
	public static String CRITICAL 	= "-fx-background-color: "+Colours.CRITICAL+"; -fx-font-weight: bold;";
	public static String HIGH		= "-fx-background-color: "+Colours.HIGH;
	public static String MEDIUM		= "-fx-background-color: "+Colours.MEDIUM;
	public static String LOW		= "-fx-background-color: "+Colours.LOW;
	public static String UNKNOWN	= "-fx-background-color: "+Colours.UNKNOWN;
	
	public static String CODED		= "-fx-background-color: "+Colours.CODED;
	public static String BUILT		= "-fx-background-color: "+Colours.BUILT;
	public static String RELEASED	= "-fx-background-color: "+Colours.RELEASED;	
	public static String FIXED		= "-fx-background-color: "+Colours.FIXED;
	public static String CLOSED		= "-fx-background-color: "+Colours.CLOSED;
	
	public static String ADDRESSED	= "-fx-background-color: "+Colours.ADDRESSED;
	public static String NOFAULT	= "-fx-background-color: "+Colours.NOFAULT;	
	
	public static String SELECTED	= "-fx-background-color: "+Colours.SELECTED;
	
	public static void updateColours()
	{
		CRITICAL 	= "-fx-background-color: "+Colours.CRITICAL+"; -fx-font-weight: bold;";
		HIGH		= "-fx-background-color: "+Colours.HIGH;
		MEDIUM		= "-fx-background-color: "+Colours.MEDIUM;
		LOW			= "-fx-background-color: "+Colours.LOW;
		UNKNOWN		= "-fx-background-color: "+Colours.UNKNOWN;
		
		CODED		= "-fx-background-color: "+Colours.CODED;
		BUILT		= "-fx-background-color: "+Colours.BUILT;
		RELEASED	= "-fx-background-color: "+Colours.RELEASED;	
		FIXED		= "-fx-background-color: "+Colours.FIXED;
		CLOSED		= "-fx-background-color: "+Colours.CLOSED;
		
		ADDRESSED	= "-fx-background-color: "+Colours.ADDRESSED;
		NOFAULT		= "-fx-background-color: "+Colours.NOFAULT;	
	}
}
