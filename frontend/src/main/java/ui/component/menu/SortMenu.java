package ui.component.menu;

import common.utility.UiMethods;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ui.component.BugTable;
import ui.component.dialog.CustomSortDialog;

public class SortMenu
{
	public Menu sortMenu = new Menu("Sort");
	
	public SortMenu()
	{
		MenuItem customSort = new MenuItem("Custom...");
		customSort.setOnAction(e -> new CustomSortDialog());
		
		Menu numberMenu 		= createSortingSubMenu("Bug Number", "number");
		Menu statusMenu 		= createSortingSubMenu("Status", "status");
		Menu assignedMenu 		= createSortingSubMenu("Assigned To", "assignedTo");
		Menu severityMenu 		= createSortingSubMenu("Severity", "severity");
		Menu summaryMenu 		= createSortingSubMenu("Summary", "summary");
		Menu lastUpdatedMenu 	= createSortingSubMenu("Last Updated", "lastUpdated");

		sortMenu.getItems().addAll(customSort, numberMenu, statusMenu, assignedMenu, severityMenu, summaryMenu, lastUpdatedMenu);
	}
	
	public Menu getMenu()
	{
		return sortMenu;
	}

	private Menu createSortingSubMenu(String menuName, String sortingProperty)
	{
		Menu m = new Menu(menuName);
		MenuItem ascending = new MenuItem("Ascending");
		MenuItem descending = new MenuItem("Descending");

		ascending .setOnAction(e -> UiMethods.sortBugs(BugTable.getInstance().getTableView().getItems(), false, sortingProperty));		
		descending.setOnAction(e -> UiMethods.sortBugs(BugTable.getInstance().getTableView().getItems(), true, sortingProperty));

		m.getItems().addAll(ascending, descending);
		return m;
	}
}