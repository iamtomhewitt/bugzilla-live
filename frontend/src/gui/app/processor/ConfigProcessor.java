package gui.app.processor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.utilities.Encryptor;
import gui.app.common.GuiConstants;
import gui.app.common.GuiMethods;
import gui.app.log.GuiLogger;
import gui.app.theme.Colours;

public class ConfigProcessor extends MessageProcessor
{
	@Override
	public void process(JSONObject message)
	{
		String operation = getPropertyAsString(message, "operation");
		GuiLogger.getInstance().print("operation: "+operation);
		
		if (operation.equals("notification"))
		{
			String successful = getPropertyAsString(message, "successful");	
			if (successful.equals("no"))
			{
				String reason = getPropertyAsString(message, "failurereason");
				showMicroserviceFailure("The config service has returned an error:", reason);
			}
		}
		else
		{
			JSONArray propertiesjsonArray = (JSONArray) message.get("properties");
			JSONObject property = (JSONObject) propertiesjsonArray.get(0);
			
			switch (operation)
			{
				case "getconfiguser":
					String username 	= getPropertyAsString(property, "username");
					String password 	= getPropertyAsString(property, "password");
					String apiKey 		= getPropertyAsString(property, "api_key");

					GuiConstants.USERNAME 	= username;
					GuiConstants.PASSWORD 	= Encryptor.decrypt(password);
					GuiConstants.APIKEY 	= apiKey;

					GuiLogger.getInstance().print("Saved user properties: '" + GuiConstants.USERNAME + "', '" + "*****', '" + GuiConstants.APIKEY + "'");

					GuiMethods.updateApplicationTitle(username);
					break;

				case "getconfigapplication":
					String refreshrate 	= getPropertyAsString(property, "refreshrate");
					String version 		= getPropertyAsString(property, "version");
					String bugzillaURL 	= getPropertyAsString(property, "bugzillaurl");

					GuiConstants.REFRESH_TIME 	= Integer.valueOf(refreshrate);
					GuiConstants.VERSION 		= version;
					GuiConstants.BUGZILLA_URL	= bugzillaURL;
					
					GuiLogger.getInstance().print("Saved application properties: '" + GuiConstants.REFRESH_TIME + "', '" + GuiConstants.VERSION + "', '" + GuiConstants.BUGZILLA_URL + "'");

					Colours.WINDOW 					= getColourProperty(property, "windowcolour", Colours.WINDOW);
					Colours.WINDOW_TEXT 			= getColourProperty(property, "windowtextcolour", Colours.WINDOW_TEXT);
					Colours.CRITICAL 				= getColourProperty(property, "criticalcolour", Colours.CRITICAL);
					Colours.HIGH 					= getColourProperty(property, "highcolour", Colours.HIGH);
					Colours.MEDIUM 					= getColourProperty(property, "mediumcolour", Colours.MEDIUM);
					Colours.LOW 					= getColourProperty(property, "lowcolour", Colours.LOW);
					Colours.UNKNOWN 				= getColourProperty(property, "unknowncolour", Colours.UNKNOWN);
					Colours.CODED 					= getColourProperty(property, "codedcolour", Colours.CODED);
					Colours.BUILT 					= getColourProperty(property, "builtcolour", Colours.BUILT);
					Colours.RELEASED 				= getColourProperty(property, "releasedcolour", Colours.RELEASED);
					Colours.ADDRESSED 				= getColourProperty(property, "addressedcolour", Colours.ADDRESSED);
					Colours.FIXED 					= getColourProperty(property, "fixedcolour", Colours.FIXED);
					Colours.CLOSED 					= getColourProperty(property, "closedcolour", Colours.CLOSED);
					Colours.NOFAULT 				= getColourProperty(property, "nofaultcolour", Colours.NOFAULT);
					Colours.INFO_PANE_BACKGROUND 	= getColourProperty(property, "infopanebackgroundcolour", Colours.INFO_PANE_BACKGROUND);
					Colours.INFO_PANE_HEADING 		= getColourProperty(property, "infopaneheadingcolour", Colours.INFO_PANE_HEADING);
					Colours.INFO_PANE_SUBHEADING 	= getColourProperty(property, "infopanesubheadingcolour", Colours.INFO_PANE_SUBHEADING);
					break;

				default:
					GuiLogger.getInstance().print("Unknown operation: " + operation);
					/**
					 * TODO throw correct exception
					 */
					break;
			}
		}
	}
	
	/**
	 * @return An empty string if the property is null, otherwise the property.
	 */
	private String getPropertyAsString(JSONObject jsonObject, String propertyName)
	{
		return jsonObject.get(propertyName) == null ? "" : jsonObject.get(propertyName).toString();
	}
	
	private String getColourProperty(JSONObject jsonObject, String propertyName, String originalColour)
	{		
		String colourProperty = this.getPropertyAsString(jsonObject, propertyName);
		
		if (colourProperty.isEmpty())
		{
			colourProperty = originalColour;
		}
		
		return colourProperty;
	}
}
