package gui.app.component.menu;

import java.io.IOException;

import common.common.Errors;
import common.common.MessageBox;
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
		MenuItem howTo = new MenuItem("Show Help", Icons.createHelpIcon());
		howTo.setOnAction(e -> 
		{
			try 
			{
				Utilities.openUrlInFirefox("");
			} 
			catch (IOException e1) 
			{
				MessageBox.showExceptionDialog(Errors.FIREFOX, e1);
			}
		});
		
		MenuItem about = new MenuItem("About", Icons.createAboutIcon());
		about.setOnAction(e -> new AboutDialog());
		
		helpMenu.getItems().addAll(about, howTo);
	}

	public Menu getMenu()
	{
		return helpMenu;
	}
}