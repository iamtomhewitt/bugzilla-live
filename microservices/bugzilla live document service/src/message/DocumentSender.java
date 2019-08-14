package message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import bugzilla.common.Folders;
import bugzilla.message.document.DocumentResponse;
import log.DocumentLogger;

/**
 * Sends document response messages to the message folder.
 * 
 * @author Tom Hewitt
 * @since 2.3.0
 */
public class DocumentSender
{
	public static void sendResponseMessage(boolean successful, String reason, String filePath)
	{
		try
		{
			DocumentLogger.getInstance().print("Sending response message");

			DocumentResponse response = new DocumentResponse(successful, reason, filePath);

			File messageFile = new File(Folders.MESSAGE_FOLDER + response.getFileExtension());
			FileWriter file = new FileWriter(messageFile);
			file.write(response.toJson());
			file.close();

			DocumentLogger.getInstance().print("Response message sent");
		}
		catch (IOException e)
		{
			DocumentLogger.getInstance().printStackTrace(e);
		}
	}
}
