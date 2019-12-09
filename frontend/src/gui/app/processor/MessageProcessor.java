package gui.app.processor;

import org.json.simple.JSONObject;

import common.MessageBox;
import common.exception.MessageProcessorException;
import javafx.application.Platform;

/**
 * Base class for processing <code>JSON</code> messages from the message folder.
 * @author Tom Hewitt
 */
public abstract class MessageProcessor
{
	public abstract void process(JSONObject message) throws MessageProcessorException;
	
	protected void showMicroserviceFailure(String message, String reason)
	{
		Platform.runLater(new Runnable()
		{								
			@Override
			public void run()
			{
				MessageBox.showDialog(message+"\n\n"+reason);									
			}
		});
	}
}
