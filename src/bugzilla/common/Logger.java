package bugzilla.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * A simple logger that each micro service extends to write to their own file.
 *
 * @author Tom Hewitt
 * @since 2.0.0
 */
public abstract class Logger
{
	private String directory;
	private String filename = "log.log";
	private File logFile;
	
	public void print(String msg) 
	{
		try 
		{
			boolean append = true;
						
			if (!logFile.exists())
			{				
				logFile.getParentFile().mkdirs();
				logFile.createNewFile();
			}
			
			PrintWriter out = new PrintWriter(new FileWriter(logFile, append));
			
			System.out.println(Calendar.getInstance().getTime()+ " | " + msg);
			out.println(Calendar.getInstance().getTime()+ " | " + msg);
			out.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void printStackTrace(Exception e)
	{
		try
		{
			e.printStackTrace();
			PrintWriter pw = new PrintWriter(new FileWriter(logFile, true));
			e.printStackTrace(pw);
			pw.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public String getDirectory()
	{
		return directory;
	}

	public void setDirectory(String directory)
	{
		this.directory = directory;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public File getLogFile()
	{
		return logFile;
	}

	public void setLogFile(File logFile)
	{
		this.logFile = logFile;
	}
}
