package gui.app.theme;

public class RowColours
{
	public static String CRITICAL 		= "-fx-background-color: "+Colours.CRITICAL+"; -fx-font-weight: bold;";
	public static String MAJOR			= "-fx-background-color: "+Colours.MAJOR;
	public static String MINOR			= "-fx-background-color: "+Colours.MINOR;
	public static String NORMAL			= "-fx-background-color: "+Colours.NORMAL;
	
	public static String FIXED			= "-fx-background-color: "+Colours.FIXED;
	public static String CLOSED			= "-fx-background-color: "+Colours.RESOLVED;
	
	public static String WORKS_FOR_ME	= "-fx-background-color: "+Colours.WORKS_FOR_ME;
	public static String NOFAULT		= "-fx-background-color: "+Colours.NOFAULT;	
	
	public static String SELECTED		= "-fx-background-color: "+Colours.SELECTED;
	
	public static void updateColours()
	{
		CRITICAL 		= "-fx-background-color: "+Colours.CRITICAL+"; -fx-font-weight: bold;";
		MAJOR			= "-fx-background-color: "+Colours.MAJOR;
		MINOR			= "-fx-background-color: "+Colours.MINOR;
		NORMAL			= "-fx-background-color: "+Colours.NORMAL;
	
		FIXED			= "-fx-background-color: "+Colours.FIXED;
		CLOSED			= "-fx-background-color: "+Colours.RESOLVED;
		
		WORKS_FOR_ME	= "-fx-background-color: "+Colours.WORKS_FOR_ME;
		NOFAULT			= "-fx-background-color: "+Colours.NOFAULT;	
	}
}
