package gui.app.theme;

import java.util.HashMap;
import java.util.Map;

import common.common.Errors;
import common.common.MessageBox;
import common.exception.JsonTransformationException;
import common.exception.MessageSenderException;
import common.message.config.ApplicationSaveRequest;
import gui.app.common.GuiMethods;
import gui.app.message.GuiMessageSender;

public class Themes
{
	public static void Arc()
	{
		applyTheme("#373d48", "#5294e2", "#FFFFFF");
	}
	
	public static void CraftCms()
	{
		applyTheme("#333f4e", "#da513d", "#FFFFFF");
	}
	
	public static void Ember()
	{
		applyTheme("#faf4f1", "#e46651", "#717171");
	}
	
	public static void Facebook()
	{
		applyTheme("#4e69a2", "#38528b", "#FFFFFF");
	}
	
	public static void KillBill()
	{
		applyTheme("#ffff00", "#ff0000", "#000000");
	}
	
	public static void Lightning()
	{
		applyTheme("#f4f6f9", "#6f809b", "#16325c");
	}
	
	public static void Material()
	{
		applyTheme("#263238", "#40c4ff", "#FFFFFF");
	}
	
	public static void Mint()
	{
		applyTheme("#212420", "#87cf3e", "#FFFFFF");
	}
	
	public static void Pastel()
	{
		applyTheme("#b19cd9", "#fec8d8", "#FFFFFF");
	}
	
	public static void Python()
	{
		applyTheme("#306998", "#ffd43b", "#FFFFFF");
	}
	
	private static void applyTheme(String background, String heading, String text)
	{
		Colours.INFO_PANE_BACKGROUND 	= background;
		Colours.INFO_PANE_HEADING 		= heading;
		Colours.INFO_PANE_SUBHEADING 	= text;
		Colours.WINDOW 					= background;
		Colours.WINDOW_TEXT 			= text;
		
		GuiMethods.updateColours();
		
		// Now send a config request to save the colours
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("windowcolour", Colours.WINDOW);
		properties.put("windowtextcolour", Colours.WINDOW_TEXT);
		properties.put("criticalcolour", Colours.CRITICAL);
		properties.put("highcolour", Colours.HIGH);
		properties.put("mediumcolour", Colours.MEDIUM);
		properties.put("lowcolour", Colours.LOW);
		properties.put("unknowncolour", Colours.UNKNOWN);
		properties.put("codedcolour", Colours.CODED);
		properties.put("builtcolour", Colours.BUILT);
		properties.put("releasedcolour", Colours.RELEASED);
		properties.put("addressedcolour", Colours.ADDRESSED);
		properties.put("fixedcolour", Colours.FIXED);
		properties.put("closedcolour", Colours.CLOSED);
		properties.put("nofaultcolour", Colours.NOFAULT);
		properties.put("infopanebackgroundcolour", Colours.INFO_PANE_BACKGROUND);
		properties.put("infopaneheadingcolour", Colours.INFO_PANE_HEADING);
		properties.put("infopanesubheadingcolour", Colours.INFO_PANE_SUBHEADING);

		try
		{
			ApplicationSaveRequest request = new ApplicationSaveRequest(properties);
			new GuiMessageSender().sendRequestMessage(request);
		}
		catch (JsonTransformationException | MessageSenderException e)
		{
			MessageBox.showExceptionDialog(Errors.GENERAL, e);
		}
	}
}
