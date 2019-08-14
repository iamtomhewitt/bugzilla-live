package processor;

import org.json.simple.JSONObject;

import bugzilla.common.Errors;
import bugzilla.common.MessageBox;
import bugzilla.exception.MessageProcessorException;
import component.InformationPane;
import component.dialog.OR.ORCommentDialog;
import javafx.application.Platform;
import log.GuiLogger;
import common.GuiMethods;

public class ORProcessor extends MessageProcessor
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
				showMicroserviceFailure("The OR service has returned an error:", reason);
			}
		}
		else if (operation.equals("orresponse"))
		{
			GuiMethods.updateORsInTable(message);
			InformationPane.getInstance().updateTexts();
		}
		else if (operation.equals("ordetail"))
		{
			Platform.runLater(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{																		
						new ORCommentDialog(message);
					}
					catch (Exception e)
					{
						MessageBox.showExceptionDialog(Errors.COMMENTS, e);
					}
				}
			});
		}	
		/**
		 * TODO:
		 * use a switch, and make the default throw a messageprocessorexception
		 */
	}
}
