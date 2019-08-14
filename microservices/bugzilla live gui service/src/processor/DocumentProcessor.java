package processor;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;

import bugzilla.exception.MessageProcessorException;
import log.GuiLogger;

public class DocumentProcessor extends MessageProcessor
{
	@Override
	public void process(JSONObject message) throws MessageProcessorException
	{
		String operation 	= message.get("operation").toString();
		String successful 	= message.get("successful").toString();

		GuiLogger.getInstance().print("operation: " + operation);

		if (successful.equals("no"))
		{
			String reason = message.get("failurereason").toString();
			showMicroserviceFailure("The document service has returned an error:", reason);
			return;
		}

		String filePath = message.get("filepath").toString();
		
		try 
		{
			Desktop.getDesktop().open(new File(filePath));
		} 
		catch (IOException e) 
		{
			throw new MessageProcessorException(e.getMessage());
		}
	}
}
