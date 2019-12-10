package gui.app.message;

import java.io.File;
import java.io.FileWriter;


import common.exception.JsonTransformationException;
import common.exception.MessageSenderException;
import common.message.Message;
import common.message.MessageSender;
import gui.app.log.GuiLogger;

/**
 * Sends request messages for the GUI service. Mainly request messages for other services.
 * 
 * @author Tom Hewitt
 * @since 2.0.0
 */
public class GuiMessageSender implements MessageSender
{
	@Override
	public void sendRequestMessage(Message message) throws JsonTransformationException, MessageSenderException
	{
		try
		{
			GuiLogger.getInstance().print("Sending request message: " + message.getMessage() + ", Operation: " + message.getOperation());
			
			File messageFile = new File("" + message.getFileExtension());
			FileWriter file = new FileWriter(messageFile);
			file.write(message.toJson());
			file.close();
			
			GuiLogger.getInstance().print("Message sent");
		}
		catch (Exception e)
		{
			throw new MessageSenderException(e.getMessage());
		}
	}
}
