package ui.theme;

import org.json.JSONObject;

import common.error.Errors;
import common.error.RequestException;
import common.message.ApiRequestor;
import common.message.Endpoints;
import common.message.MessageBox;
import common.utility.UiMethods;


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
		
		UiMethods.updateColours();
		
		try 
		{
			saveColoursToConfig();
		} 
		catch (RequestException e) 
		{
			MessageBox.showExceptionDialog(Errors.REQUEST, e);
		}
	}
	
	public static void saveColoursToConfig() throws RequestException
	{
		JSONObject json = new JSONObject();
		json.put("windowColour", Colours.WINDOW);		
		json.put("windowTextColour", Colours.WINDOW_TEXT);	
		json.put("criticalColour", Colours.CRITICAL);
		json.put("highColour", Colours.MAJOR);
		json.put("mediumColour", Colours.MINOR);
		json.put("lowColour", Colours.NORMAL);
		json.put("addressedColour", Colours.WORKS_FOR_ME);
		json.put("fixedColour", Colours.FIXED);
		json.put("closedColour", Colours.RESOLVED);
		json.put("noFaultColour", Colours.NOFAULT);			
		json.put("infopaneBackgroundColour", Colours.INFO_PANE_BACKGROUND);
		json.put("infopaneHeadingColour", Colours.INFO_PANE_HEADING);
		json.put("infopaneSubheadingColour", Colours.INFO_PANE_SUBHEADING);
								
		ApiRequestor.request(Endpoints.CONFIG_SAVE("colours", json.toString()));
	}
}
