package gui.app.component.menu;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import common.Errors;
import common.MessageBox;
import common.message.ApiRequestor;
import common.utilities.Icons;
import gui.app.common.GuiConstants;
import gui.app.common.GuiMethods;
import gui.app.common.RequestType;
import gui.app.component.dialog.bug.AddBugListDialog;
import gui.app.main.BugzillaLive;

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
			switchList(file.getName());
		});
		
		listMenu.setGraphic(Icons.createListIcon());

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

			populateMenuWithLists(listMenu, true);

			delete.getItems().clear();
			populateMenuWithLists(delete, false);
		});
	}
	
	private void populateMenuWithLists(Menu menu, boolean changeListMenu)
	{
		String response = ApiRequestor.request("/list/lists");
		
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
				item.setOnAction(e -> switchList(filename));
			}
			else
			{
				if (!item.isSelected())
				{
					item.setOnAction(e-> deleteList(item.getText()));
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
	
		
	private void deleteList(String filename)
	{
		try 
		{
			String name = filename.split("\\.")[0];
			String url = String.format("/list/delete?name=%s", URLEncoder.encode(name, "UTF-8"));
			String response = ApiRequestor.request(url);
			
			int status = new JSONObject(response).getInt("status");
			
			if (status != HttpStatus.SC_OK) 
			{
				MessageBox.showDialog(new JSONObject(response).getJSONObject("error").get("message").toString());
			}
		}
		catch (UnsupportedEncodingException e) 
		{
			MessageBox.showExceptionDialog(Errors.CREATE_LIST, e);
		}
	}
	
	private void switchList(String filename)
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