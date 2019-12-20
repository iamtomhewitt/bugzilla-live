package gui.app.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import gui.app.log.GuiLogger;

/**
 * A simple logger that writes to file.
 * @since 2.0.0
 */
public class GuiLogger
{
	private String directory = "logs/";
	private String filename = "log.log";
	private File logFile;
	
	private static GuiLogger instance = new GuiLogger();

	public static GuiLogger getInstance()
	{
		return instance;
	}

	public GuiLogger()
	{
		this.logFile = new File(directory + filename);
	}
	
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
}
