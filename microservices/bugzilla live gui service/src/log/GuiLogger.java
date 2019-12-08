package log;

import java.io.File;

import common.common.Folders;
import common.common.Logger;

public class GuiLogger extends Logger
{
	private static GuiLogger instance = new GuiLogger();

	public static GuiLogger getInstance()
	{
		return instance;
	}

	public GuiLogger()
	{
		this.setDirectory(Folders.GUI_SERVICE_FOLDER);
		this.setLogFile(new File(this.getDirectory() + this.getFilename()));
	}
}
