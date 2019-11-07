package processor;

import org.json.simple.JSONObject;

import bugzilla.common.Errors;
import bugzilla.common.MessageBox;
import bugzilla.exception.MessageProcessorException;
import component.InformationPane;
import component.dialog.bug.BugCommentDialog;
import javafx.application.Platform;
import log.GuiLogger;
import common.GuiMethods;

public class BugProcessor extends MessageProcessor
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
				showMicroserviceFailure("The bug service has returned an error:", reason);
			}
		}
		else if (operation.equals("bugresponse"))
		{
			GuiMethods.updateBugsInTable(message);
			InformationPane.getInstance().updateTexts();
		}
		else if (operation.equals("bugdetail"))
		{
			Platform.runLater(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{																		
						new BugCommentDialog(message);
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
