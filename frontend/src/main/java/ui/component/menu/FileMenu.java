package ui.component.menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ui.component.dialog.ColourChooser;
import ui.component.dialog.RefreshRateDialog;
import ui.theme.Icons;
import ui.theme.Themes;

public class FileMenu
{
	private Menu fileMenu = new Menu("File");

	public FileMenu()
	{
		MenuItem refreshTime = new MenuItem("Change Refresh Time", new Icons().createRefreshIcon());
		refreshTime.setOnAction(e -> new RefreshRateDialog());
		
		Menu themes 		= new Menu("Themes", new Icons().createThemeIcon());
		Menu prebuiltThemes = new Menu("Built In Themes");

		MenuItem colourChooser = new MenuItem("Choose Colours");
		colourChooser.setOnAction(e -> new ColourChooser());
		
		MenuItem arc 		= new MenuItem("Arc");
		MenuItem craftCms 	= new MenuItem("CraftCMS");
		MenuItem ember 		= new MenuItem("Ember");
		MenuItem facebook 	= new MenuItem("Facebook");
		MenuItem killBill 	= new MenuItem("Kill Bill");
		MenuItem lightning 	= new MenuItem("Lightning");
		MenuItem material 	= new MenuItem("Material");
		MenuItem mint 		= new MenuItem("Mint");
		MenuItem pastel 	= new MenuItem("Pastel");
		MenuItem python 	= new MenuItem("Python");
	
		arc.setOnAction(e -> Themes.Arc());
		craftCms.setOnAction(e -> Themes.CraftCms());
		ember.setOnAction(e -> Themes.Ember());
		facebook.setOnAction(e -> Themes.Facebook());
		killBill.setOnAction(e -> Themes.KillBill());
		lightning.setOnAction(e -> Themes.Lightning());
		material.setOnAction(e -> Themes.Material());
		mint.setOnAction(e -> Themes.Mint());
		pastel.setOnAction(e -> Themes.Pastel());
		python.setOnAction(e -> Themes.Python());
		
		prebuiltThemes.getItems().addAll(arc, craftCms, ember, facebook, killBill, lightning, material, mint, pastel, python);		
		themes.getItems().addAll(colourChooser, prebuiltThemes);		
		fileMenu.getItems().addAll(new ListMenu().getMenu(), themes, refreshTime);
	}
	
	public Menu getMenu()
	{
		return fileMenu;
	}
}