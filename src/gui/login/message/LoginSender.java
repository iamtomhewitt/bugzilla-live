package gui.login.message;

import java.io.File;
import java.io.FileWriter;

import common.common.Folders;
import common.exception.JsonTransformationException;
import common.exception.MessageSenderException;
import common.message.Message;
import common.message.MessageSender;
import gui.login.log.LoginLogger;

/**
 * Sends request messages for the login service. Mainly config requests to populate the fields on startup, or to send information to the main application,
 * to be used later.
 * 
 * @author Tom Hewitt
 * @since 2.0.0
 */
public class LoginSender implements MessageSender
{	
	@Override
	public void sendRequestMessage(Message message) throws MessageSenderException
	{
		try
		{
			LoginLogger.getInstance().print("Sending request message: " + message.getMessage() + "|" + message.getOperation());			

			File messageFile = new File(Folders.MESSAGE_FOLDER + message.getFileExtension());
			FileWriter file = new FileWriter(messageFile);
			file.write(message.toJson());
			file.close();

			LoginLogger.getInstance().print("Message sent");
		}
		catch (JsonTransformationException e)
		{
			LoginLogger.getInstance().printStackTrace(e);
		}
		catch (Exception e)
		{
			throw new MessageSenderException(e.getMessage());
		}
	}
}
