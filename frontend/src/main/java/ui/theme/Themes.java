package ui.theme;

import org.json.JSONObject;

import common.error.Errors;
import common.error.RequestException;
import common.message.ApiRequestor;
import common.message.ApiRequestor.ApiRequestType;
import common.message.Endpoints;
import common.message.MessageBox;
import common.utility.UiMethods;
import ui.component.InformationPane;

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
		Colours.setInfoPaneBackground(background);
		Colours.setInfoPaneHeading(heading);
		Colours.setInfoPaneSubheading(text);
		Colours.setWindow(background);
		Colours.setWindowText(text);
		
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
		json.put("critical", Colours.getCritical());		
		json.put("major", Colours.getMajor());	
		json.put("minor", Colours.getMinor());
		json.put("normal", Colours.getNormal());
		json.put("trivial", Colours.getTrivial());
		json.put("fixed", Colours.getFixed());
		json.put("resolved", Colours.getResolved());
		json.put("worksForMe", Colours.getWorksForMe());
		json.put("noFault", Colours.getNoFault());
		json.put("selected", Colours.getSelected());			
		json.put("blank", Colours.getBlank());
		json.put("window", Colours.getWindow());
		json.put("windowText", Colours.getWindowText());
		json.put("infoPaneBackground", Colours.getInfoPaneBackground());
		json.put("infoPaneHeading", Colours.getInfoPaneHeading());
		json.put("infoPaneSubheading", Colours.getInfoPaneSubheading());

		ApiRequestor.request(ApiRequestType.GET, Endpoints.CONFIG_SAVE("colours", json.toString()));
	}
	
	public static void updateColours(JSONObject colours)
	{
		Colours.setWindow(colours.getString("window"));
		Colours.setWindowText(colours.getString("windowText"));
		Colours.setCritical(colours.getString("critical"));
		Colours.setMajor(colours.getString("major"));
		Colours.setMinor(colours.getString("minor"));
		Colours.setNormal(colours.getString("normal"));
		Colours.setWorksForMe(colours.getString("worksForMe"));
		Colours.setFixed(colours.getString("fixed"));
		Colours.setResolved(colours.getString("resolved"));
		Colours.setNoFault(colours.getString("noFault"));
		Colours.setInfoPaneBackground(colours.getString("infoPaneBackground"));
		Colours.setInfoPaneHeading(colours.getString("infoPaneHeading"));
		Colours.setInfoPaneSubheading(colours.getString("infoPaneSubheading"));
		
		UiMethods.updateColours();
		RowColours.updateColours();
		InformationPane.getInstance().updateBackgroundColour();
		InformationPane.getInstance().updateHeadingColour();
		InformationPane.getInstance().updateSubheadingColour();
	}
}