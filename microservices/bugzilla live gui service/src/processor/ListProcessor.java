package processor;

import org.json.simple.JSONObject;

import bugzilla.exception.MessageProcessorException;
import log.GuiLogger;

public class ListProcessor extends MessageProcessor
{
	@Override
	public void process(JSONObject message) throws MessageProcessorException
	{
		String operation = message.get("operation").toString();
		GuiLogger.getInstance().print("operation: "+operation);
		
		if (operation.equals("notification"))
		{
			String successful = message.get("successful").toString();	
			if (successful.equals("no"))
			{
				String reason = message.get("failurereason").toString();
				showMicroserviceFailure("The list manager service has returned an error:", reason);
			}
		}
		else
		{
			throw new MessageProcessorException("Unknown operation: " + operation);
		}		
	}
}
