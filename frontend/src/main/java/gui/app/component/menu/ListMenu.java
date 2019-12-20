package gui.app.component.menu;

import java.io.File;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import common.error.Errors;
import common.message.ApiRequestor;
import common.message.Endpoints;
import common.message.MessageBox;
import gui.app.common.GuiConstants;
import gui.app.common.GuiMethods;
import gui.app.common.RequestType;
import gui.app.component.dialog.bug.AddBugListDialog;
import gui.app.main.BugzillaLive;
import gui.app.theme.Icons;

public class ListMenu
{
	public Menu listMenu = new Menu("Lists");

	public ListMenu()
	{
		MenuItem createList = new MenuItem("Create List...");
		MenuItem externalList = new MenuItem("External List...");
		
		Menu delete = new Menu("Delete");		
		
		SeparatorMenuItem separator = new SeparatorMenuItem();

		createList.setOnAction(e -> new AddBugListDialog());
		
		externalList.setOnAction(e ->
		{
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Open Bug List");
			
			File file = chooser.showOpenDialog(BugzillaLive.getMainStage());
			
			try
			{
				switchList(file.getName());
			} 
			catch (Exception ex)
			{
				MessageBox.showExceptionDialog(Errors.REQUEST, ex);
				return;
			}
		});
		
		listMenu.setGraphic(new Icons().createListIcon());

		// Even though it gets added later, not doing this will cause an empty list
		listMenu.getItems().add(createList);
		listMenu.getItems().add(externalList);
		listMenu.getItems().add(delete);
		listMenu.getItems().add(separator);

		listMenu.setOnMenuValidation(e ->
		{
			listMenu.getItems().clear();
			listMenu.getItems().add(createList);
			listMenu.getItems().add(externalList);
			listMenu.getItems().add(delete);
			listMenu.getItems().add(separator);

			try
			{
				populateMenuWithLists(listMenu, true);
	
				delete.getItems().clear();
				populateMenuWithLists(delete, false);
			} 
			catch (Exception ex)
			{
				MessageBox.showExceptionDialog(Errors.REQUEST, ex);
				return;
			}
		});
	}
	
	private void populateMenuWithLists(Menu menu, boolean changeListMenu) throws Exception
	{
		String response = ApiRequestor.request(Endpoints.LISTS);
		
		if (MessageBox.showErrorIfResponseNot200(response))
		{
			return;
		}
		
		JSONObject json = new JSONObject(response);
		JSONArray lists = json.getJSONArray("lists");
		
		for (int i = 0; i < lists.length(); i++)
		{
			String filename = lists.get(i).toString();
			
			CheckMenuItem item = new CheckMenuItem(filename);

			if (filename.equals(GuiConstants.CURRENT_LIST_FILE))
			{
				item.setSelected(true);
			}

			if (changeListMenu)
			{
				item.setOnAction(e -> 
				{
					try
					{
						switchList(filename);
					} 
					catch (Exception ex)
					{
						MessageBox.showExceptionDialog(Errors.REQUEST, ex);
						return;
					}
				});
			}
			else
			{
				if (!item.isSelected())
				{
					item.setOnAction(e-> 
					{
						try
						{
							deleteList(item.getText());
						} 
						catch (Exception ex)
						{
							MessageBox.showExceptionDialog(Errors.REQUEST, ex);
							return;
						}
					});
					menu.getItems().remove(item);
				}
				else
				{
					item.setOnAction(e-> MessageBox.showDialog("This list is in use. Please change list and try again."));
				}
			}
			menu.getItems().add(item);
		}
	}
			
	private void deleteList(String filename) throws Exception
	{
		String name = filename.split("\\.")[0];
		String response = ApiRequestor.request(Endpoints.LIST_DELETE(name));
		MessageBox.showErrorIfResponseNot200(response);
	}
	
	private void switchList(String filename) throws Exception
	{
		GuiConstants.REQUEST_TYPE = RequestType.LIST;
		GuiConstants.CURRENT_LIST_FILE = filename;
		GuiMethods.clearTable();
		GuiMethods.requestRefreshOfBugsInList();
	}

	public Menu getMenu()
	{
		return listMenu;
	}
}