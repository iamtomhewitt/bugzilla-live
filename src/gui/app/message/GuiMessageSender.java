package message;

import java.io.File;
import java.io.FileWriter;

import common.common.Folders;
import common.exception.JsonTransformationException;
import common.exception.MessageSenderException;
import common.message.Message;
import common.message.MessageSender;
import log.GuiLogger;

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
			
			File messageFile = new File(Folders.MESSAGE_FOLDER + message.getFileExtension());
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
