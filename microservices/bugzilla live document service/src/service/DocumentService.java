package service;

import bugzilla.exception.MessageReceiverException;
import log.DocumentLogger;
import message.DocumentReceiver;

/**
 * The entry point for the document service.
 * 
 * @author Tom Hewitt
 * @since 2.3.0
 */
public class DocumentService
{
	public DocumentService()
	{
		DocumentLogger.getInstance().print("Document service has started");
		DocumentReceiver messageReceiver = new DocumentReceiver();
		
		try 
		{
			messageReceiver.start();
		} 
		catch (MessageReceiverException e) 
		{
			DocumentLogger.getInstance().printStackTrace(e);
		}
	}
	
	public static void main(String[] args)
	{
		new DocumentLogger();
		new DocumentService();
	}
}