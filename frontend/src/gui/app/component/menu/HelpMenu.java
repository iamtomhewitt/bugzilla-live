package gui.app.component.menu;

import java.io.IOException;
import java.net.URISyntaxException;

import common.Errors;
import common.MessageBox;
import common.utilities.Icons;
import common.utilities.Utilities;
import gui.app.component.dialog.AboutDialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class HelpMenu
{
	private Menu helpMenu = new Menu("Help");

	public HelpMenu()
	{
		MenuItem howTo = new MenuItem("Show Help", new Icons().createHelpIcon());
		howTo.setOnAction(e -> 
		{
			try 
			{
				Utilities.openUrlInBrowser("");
			} 
			catch (IOException | URISyntaxException e1) 
			{
				MessageBox.showExceptionDialog(Errors.BROWSER, e1);
			}
		});
		
		MenuItem about = new MenuItem("About", new Icons().createAboutIcon());
		about.setOnAction(e -> new AboutDialog());
		
		helpMenu.getItems().addAll(about, howTo);
	}

	public Menu getMenu()
	{
		return helpMenu;
	}
}