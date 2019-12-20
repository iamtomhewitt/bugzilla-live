package ui.theme;

import javafx.scene.paint.Color;

public class Colours
{
	public static String CRITICAL 				= "#ff0000";
	public static String MAJOR 					= "#ffc000";
	public static String MINOR 					= "#ffff00";
	public static String NORMAL					= "#91dcff";
	public static String TRIVIAL 				= "#d0d0d0";

	public static String FIXED 					= "#00ff00";
	public static String RESOLVED 				= "#00ff2a";

	public static String WORKS_FOR_ME			= "#00ffc6";
	public static String NOFAULT 				= "#005bff";

	public static String SELECTED 				= "#000000";

	public static String BLANK 					= "#ffffff";

	public static String WINDOW 				= "#242a3a";
	public static String WINDOW_TEXT			= "#FFFFFF";
	
	public static String INFO_PANE_BACKGROUND	= "#242a3a";
	public static String INFO_PANE_HEADING		= "#587293";
	public static String INFO_PANE_SUBHEADING	= "#FFFFFF";

	public static String toHex(Color c)
	{
		return String.format("#%02X%02X%02X", (int) (c.getRed() * 255), (int) (c.getGreen() * 255), (int) (c.getBlue() * 255));
	}
}