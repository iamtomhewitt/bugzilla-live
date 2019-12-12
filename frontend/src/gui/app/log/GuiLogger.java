package gui.app.log;

import java.io.File;


import common.Logger;

public class GuiLogger extends Logger
{
	private static GuiLogger instance = new GuiLogger();

	public static GuiLogger getInstance()
	{
		return instance;
	}

	public GuiLogger()
	{
		this.setDirectory("");
		this.setLogFile(new File(this.getDirectory() + this.getFilename()));
	}
}
