package ui.app.component;

import javafx.scene.control.MenuBar;
import ui.app.component.menu.FileMenu;
import ui.app.component.menu.HelpMenu;
import ui.app.component.menu.SortMenu;

/** 
 * Custom menu bar at the top of the UI such as File, Sort etc.
 * 
 * @author Tom Hewitt
*/
public class Toolbar
{
	private MenuBar menuBar = new MenuBar();
		
	public Toolbar()
	{		
		FileMenu f = new FileMenu();
		SortMenu s = new SortMenu();
		HelpMenu h = new HelpMenu();
		
		menuBar.getMenus().addAll(f.getMenu(), s.getMenu(), h.getMenu());
	}	
	
	public MenuBar getMenuBar()
	{
		return menuBar;
	}
}