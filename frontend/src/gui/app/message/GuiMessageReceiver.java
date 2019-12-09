package gui.app.message;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.common.Errors;
import common.common.MessageBox;
import common.exception.MessageProcessorException;
import common.exception.MessageReceiverException;
import common.message.MessageReceiver;
import gui.app.common.GuiMethods;
import gui.app.log.GuiLogger;
import gui.app.processor.BugProcessor;
import gui.app.processor.ConfigProcessor;
import gui.app.processor.ListProcessor;

public class GuiMessageReceiver extends MessageReceiver
{
	public GuiMessageReceiver()
	{
		GuiLogger.getInstance().print("GUI Receiver started");
		this.setFileTypes(Arrays.asList(".bugresponse", ".listresponse", ".configresponse"));
	}	

	@Override
	public void processMessage(File file) throws MessageReceiverException
	{
		try
		{
			while(!file.renameTo(file)) 
			{
		        // Cannot read from file, windows still working on it.
		        Thread.sleep(10);
		    }
			
			String fileContents = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);

			// Now parse the contents
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(fileContents);

			String messageType = jsonObject.get("message").toString().toLowerCase();		
			String operation   = jsonObject.get("operation").toString().toLowerCase();	
			
			GuiLogger.getInstance().print("Message received: '"+messageType+"'");
			
			switch (messageType)
			{
				case "configresponse":
					new ConfigProcessor().process(jsonObject);
					break;
					
				case "listresponse":
					new ListProcessor().process(jsonObject);
					break;

				case "bugresponse":
					new BugProcessor().process(jsonObject);
					break;

				default:
					GuiLogger.getInstance().print("Unknown message: " + messageType);
					break;
			}
			
			switch (operation)
			{
				case "getconfigapplication":
					GuiMethods.updateColours();
					break;
					
				default:
					break;
			}
			
		}
		catch (MessageProcessorException e)
		{
			MessageBox.showExceptionDialog(Errors.GENERAL, e);
		} 
		catch (Exception e)
		{
			throw new MessageReceiverException(e.getMessage());
		}
	}
}
