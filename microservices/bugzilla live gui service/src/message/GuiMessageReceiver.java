package message;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import bugzilla.common.Errors;
import bugzilla.common.MessageBox;
import bugzilla.common.message.MessageReceiver;
import bugzilla.exception.MessageProcessorException;
import bugzilla.exception.MessageReceiverException;
import common.GuiMethods;
import log.GuiLogger;
import processor.ConfigProcessor;
import processor.DocumentProcessor;
import processor.ListProcessor;
import processor.ORProcessor;

public class GuiMessageReceiver extends MessageReceiver
{
	public GuiMessageReceiver()
	{
		GuiLogger.getInstance().print("GUI Receiver started");
		this.setFileTypes(Arrays.asList(".orresponse", ".listresponse", ".configresponse", ".documentresponse"));
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

				case "orresponse":
					new ORProcessor().process(jsonObject);
					break;
					
				case "documentresponse":
					new DocumentProcessor().process(jsonObject);
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
