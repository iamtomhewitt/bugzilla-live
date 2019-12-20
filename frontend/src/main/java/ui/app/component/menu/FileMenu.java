package ui.app.component.menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ui.app.component.dialog.ColourChooser;
import ui.app.component.dialog.RefreshRateDialog;
import ui.app.theme.Icons;
import ui.app.theme.Themes;

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
		
		prebuiltThemes.getItems().addAll(arc, craftCms, ember, facebook, killBill, lightning, material, mint, pastel, python);		
		themes.getItems().addAll(colourChooser, prebuiltThemes);		
		fileMenu.getItems().addAll(new ListMenu().getMenu(), themes, refreshTime);
	}
	
	public Menu getMenu()
	{
		return fileMenu;
	}
}