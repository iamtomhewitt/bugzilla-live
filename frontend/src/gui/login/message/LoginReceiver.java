package gui.login.message;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import common.common.Errors;
import common.common.MessageBox;
import common.message.MessageReceiver;
import common.utilities.Encryptor;
import gui.login.log.LoginLogger;
import javafx.application.Platform;

/**
 * Processes config response files for the login service - so that the fields can be pre-populated.
 * 
 * @author Tom Hewitt
 * @since 2.0.0
 */
public class LoginReceiver extends MessageReceiver
{
	private String retrivedUsername;
	private String retrievedPassword;
	private String retrievedApiKey;
	private String retrievedBugzillaUrl;
		
	public LoginReceiver()
	{
		LoginLogger.getInstance().print("Login receiver started");		
		this.setFileTypes(Arrays.asList(".configresponse"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(File file)
	{
		LoginLogger.getInstance().print("Message received: "+file.getName());
		try
		{
			while(!file.renameTo(file)) 
			{
		        // Cannot read from file, windows still working on it.
		        Thread.sleep(10);
		    }
			
			String contents = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);

			// Now parse the contents
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(contents);
		
			JSONArray propertiesjsonArray = (JSONArray) jsonObject.get("properties");
			
			// We could receive a general response message, so this could be null
			if (propertiesjsonArray != null)
			{
				JSONObject property = (JSONObject) propertiesjsonArray.get(0);

				Map<String, String> properties = new HashMap<String, String>();
				Iterator<String> keys = property.keySet().iterator();
				while (keys.hasNext())
				{
					String key = keys.next();
					String value = property.get(key) == null ? "" : property.get(key).toString();
					properties.put(key, value);
					
					switch (key)
					{
						case "password":
							String pass = value.isEmpty() ? "" : Encryptor.decrypt(value);
							this.retrievedPassword = pass;
							break;

						case "username":
							this.retrivedUsername = value;
							break;

						case "api_key":
							this.retrievedApiKey = value;
							break;
							
						case "bugzillaurl":
							this.retrievedBugzillaUrl = value;
							break;
							
						default:
							// A different property, e.g. colours, so ignore
							break;
					}
				}
			}

		} 
		catch (IOException | ParseException | InterruptedException e)
		{
			showExceptionMessageBox(Errors.GENERAL, e);
		} 
	}

	public String getRetrivedUsername()
	{
		return retrivedUsername;
	}

	public String getRetrievedPassword()
	{
		return retrievedPassword;
	}

	public String getRetrievedApiKey()
	{
		return retrievedApiKey;
	}
	
	public String getRetrievedBugzillaUrl()
	{
		return retrievedBugzillaUrl;
	}
	
	private void showExceptionMessageBox(String message, Exception e)
	{
		Platform.runLater(new Runnable()
		{			
			@Override
			public void run()
			{
				MessageBox.showExceptionDialog(Errors.GENERAL, e);				
			}
		});
	}
}
