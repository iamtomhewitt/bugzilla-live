package component.menu;

import java.io.File;

import bugzilla.common.Errors;
import bugzilla.common.Folders;
import bugzilla.common.MessageBox;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.message.list.DeleteListRequest;
import bugzilla.utilities.Icons;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.FileChooser;
import main.BugzillaLive;
import common.GuiConstants;
import common.GuiMethods;
import common.RequestType;
import component.dialog.bug.AddBugListDialog;
import message.GuiMessageSender;

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
			switchList(file);
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
		File folder = new File(Folders.LISTS_FOLDER);
		String[] files = folder.list();

		for (int i = 0; i < files.length; i++)
		{
			CheckMenuItem list = new CheckMenuItem(files[i].split(".txt")[0]);
			File configFile = new File(Folders.LISTS_FOLDER + files[i]);

			if (GuiConstants.CURRENT_LIST_FILE != null && configFile.getName().equals(GuiConstants.CURRENT_LIST_FILE.getName()))
			{
				list.setSelected(true);
			}

			if (changeListMenu)
			{
				list.setOnAction(e -> switchList(configFile));
			}
			else
			{
				if (!list.isSelected())
				{
					list.setOnAction(e-> deleteList(list.getText()));
					menu.getItems().remove(list);
				}
				else
				{
					list.setOnAction(e-> MessageBox.showDialog("This list is in use. Please change list and try again."));
				}
			}
			menu.getItems().add(list);
		}
	}
	
		
	private void deleteList(String filename)
	{
		try
		{
			DeleteListRequest request = new DeleteListRequest(Folders.LISTS_FOLDER + filename + ".txt");
			new GuiMessageSender().sendRequestMessage(request);
		}
		catch (JsonTransformationException | MessageSenderException e)
		{
			MessageBox.showExceptionDialog(Errors.GENERAL, e);
		}
	}
	
	private void switchList(File file)
	{
		GuiConstants.REQUEST_TYPE = RequestType.LIST;
		GuiConstants.CURRENT_LIST_FILE = file;
		GuiMethods.clearTable();
		GuiMethods.requestRefreshOfBugsInList();
	}

	public Menu getMenu()
	{
		return listMenu;
	}
}
