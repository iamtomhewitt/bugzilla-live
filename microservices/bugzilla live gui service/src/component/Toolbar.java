package component;

import component.menu.FileMenu;
import component.menu.HelpMenu;
import component.menu.SortMenu;
import javafx.scene.control.MenuBar;

/** 
 * Custom menu bar at the top of the GUI such as File, Sort etc.
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