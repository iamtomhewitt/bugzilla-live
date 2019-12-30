package ui.theme;

public class RowColours
{
	public static String CRITICAL 		= "-fx-background-color: "+Colours.getCritical()+"; -fx-font-weight: bold;";
	public static String MAJOR			= "-fx-background-color: "+Colours.getMajor();
	public static String MINOR			= "-fx-background-color: "+Colours.getMinor();
	public static String NORMAL			= "-fx-background-color: "+Colours.getNormal();
	
	public static String FIXED			= "-fx-background-color: "+Colours.getFixed();
	public static String CLOSED			= "-fx-background-color: "+Colours.getResolved();
	
	public static String WORKS_FOR_ME	= "-fx-background-color: "+Colours.getWorksForMe();
	public static String NOFAULT		= "-fx-background-color: "+Colours.getNoFault();	
	
	public static String SELECTED		= "-fx-background-color: "+Colours.getSelected();
	
	public static void updateColours()
	{
		CRITICAL 		= "-fx-background-color: "+Colours.getCritical()+"; -fx-font-weight: bold;";
		MAJOR			= "-fx-background-color: "+Colours.getMajor();
		MINOR			= "-fx-background-color: "+Colours.getMinor();
		NORMAL			= "-fx-background-color: "+Colours.getNormal();
	
		FIXED			= "-fx-background-color: "+Colours.getFixed();
		CLOSED			= "-fx-background-color: "+Colours.getResolved();
		
		WORKS_FOR_ME	= "-fx-background-color: "+Colours.getWorksForMe();
		NOFAULT			= "-fx-background-color: "+Colours.getNoFault();	
	}
}
