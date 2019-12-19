package bugzilla.common.message;

import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.message.Message;

/**
 * An abstract class for sending messages to each micro service. Each service that extends this class has its own <code>Message</code> sender, but
 * shares the message folder and the method for sending a response message.
 *
 * @author Tom Hewitt
 * @since 2.0.0
 */
public interface MessageSender
{	
	/**
	 * Sends a notification message to the message folder.
	 * @param message - The request message to send.
	 * @throws JsonTransformationException 
	 * @throws MessageSenderException 
	 */
	public abstract void sendRequestMessage(Message message) throws JsonTransformationException, MessageSenderException;
}
