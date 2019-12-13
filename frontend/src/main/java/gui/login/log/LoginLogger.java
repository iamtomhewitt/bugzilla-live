package gui.login.log;

import java.io.File;


import common.Logger;

/**
 * Logs activity of the login service.
 * 
 * @author Tom Hewitt
 * @since 2.0.0
 */
public class LoginLogger extends Logger
{
	private static LoginLogger instance = new LoginLogger();

	public static LoginLogger getInstance()
	{
		return instance;
	}

	public LoginLogger()
	{
		this.setDirectory("");
		this.setLogFile(new File(this.getDirectory() + this.getFilename()));
	}
}