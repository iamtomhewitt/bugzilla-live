package bugzilla.common.message;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import bugzilla.common.Folders;
import bugzilla.exception.MessageReceiverException;

/**
 * An abstract class for processing messages in the message folder. <p>
 * Each service that implements <code>MessageReceiver</code> has a list of file types it is allowed to process - so that each service
 * does not process messages made for other services (e.g. the List service cannot processes messages for the Bug service). 
 *
 * @author Tom Hewitt
 * @since 2.0.0
 */
public abstract class MessageReceiver
{
	private List<String> fileTypes;
	
	private boolean stop = false;
	
	public void start() throws MessageReceiverException
	{
		try
		{
			// Process left over files
			File[] files = new File(getMessageFolder()).listFiles();
			for (File file : files)
			{
				String fileExtension = "." + file.getName().split("\\.")[1];
					
				if (file.isFile() && fileTypes.contains(fileExtension))
				{
					processMessage(file);
					Files.delete(file.toPath());
				}
			}			
				
			WatchService watchService = FileSystems.getDefault().newWatchService();
	
			Path path = Paths.get(getMessageFolder());
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
	
			WatchKey key;
	
			while ((key = watchService.take()) != null && !stop)
			{
				for (WatchEvent<?> event : key.pollEvents())
				{
					String filename = event.context().toString();
					String fileExtension = "." + filename.split("\\.")[1];
	
					if (fileTypes.contains(fileExtension) && event.kind().name().equals("ENTRY_CREATE"))
					{
						File file = new File(getMessageFolder() + filename);
						processMessage(file);
	
						Files.delete(file.toPath());
					}
				}
				key.reset();
			}
		}
		catch (Exception e)
		{
			throw new MessageReceiverException(e.getMessage());
		}
	}
	
	/**
	 * Processes the request file in the message folder, if the extension of the file matches <code>fileTypes</code>
	 */
	public abstract void processMessage(File file) throws MessageReceiverException;

	public void stop()
	{
		this.stop = true;
	}
	
	public String getMessageFolder()
	{
		return Folders.MESSAGE_FOLDER;
	}

	public List<String> getFileTypes()
	{
		return fileTypes;
	}

	public void setFileTypes(List<String> fileTypes)
	{
		this.fileTypes = fileTypes;
	}	
}
