package gui.app.component.menu;

import gui.app.component.dialog.ColourChooser;
import gui.app.component.dialog.RefreshRateDialog;

import gui.app.theme.Themes;
import common.Errors;
import common.MessageBox;
import common.exception.JsonTransformationException;
import common.exception.MessageSenderException;
import common.utilities.Icons;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class FileMenu
{
	private Menu fileMenu = new Menu("File");

	public FileMenu()
	{
		MenuItem refreshTime = new MenuItem("Change Refresh Time", Icons.createRefreshIcon());
		refreshTime.setOnAction(e -> new RefreshRateDialog());
		
		Menu themes 		= new Menu("Themes", Icons.createThemeIcon());
		Menu prebuiltThemes = new Menu("Built In Themes");

		MenuItem colourChooser = new MenuItem("Choose Colours");
		colourChooser.setOnAction(e -> new ColourChooser());
		
		MenuItem arc = new MenuItem("Arc");
		arc.setOnAction(e -> Themes.Arc());
		
		MenuItem craftCms = new MenuItem("CraftCMS");
		craftCms.setOnAction(e -> Themes.CraftCms());
		
		MenuItem ember = new MenuItem("Ember");
		ember.setOnAction(e -> Themes.Ember());
		
		MenuItem facebook = new MenuItem("Facebook");
		facebook.setOnAction(e -> Themes.Facebook());
		
		MenuItem killBill = new MenuItem("Kill Bill");
		killBill.setOnAction(e -> Themes.KillBill());
		
		MenuItem lightning = new MenuItem("Lightning");
		lightning.setOnAction(e -> Themes.Lightning());
		
		MenuItem material = new MenuItem("Material");
		material.setOnAction(e -> Themes.Material());
		
		MenuItem mint = new MenuItem("Mint");
		mint.setOnAction(e -> Themes.Mint());
		
		MenuItem pastel = new MenuItem("Pastel");
		pastel.setOnAction(e -> Themes.Pastel());
		
		MenuItem python = new MenuItem("Python");
		python.setOnAction(e -> Themes.Python());
		
		MenuItem reloadConfig = new MenuItem("Reload Config", Icons.createRefreshIcon());
		reloadConfig.setOnAction(e ->
		{			
//			try
//			{
//				// TODO use ApiRequestor			
//				// TODO use ApiRequestor
//			}
//			catch (JsonTransformationException | MessageSenderException e1)
//			{
//				MessageBox.showExceptionDialog(Errors.GENERAL, e1);
//			}
		});
		
		prebuiltThemes.getItems().addAll(arc, craftCms, ember, facebook, killBill, lightning, material, mint, pastel, python);		
		themes.getItems().addAll(colourChooser, prebuiltThemes);		
		fileMenu.getItems().addAll(new ListMenu().getMenu(), themes, refreshTime, reloadConfig);
	}
	
	public Menu getMenu()
	{
		return fileMenu;
	}
}