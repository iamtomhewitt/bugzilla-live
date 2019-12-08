package component.menu;

import java.awt.MouseInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import component.InformationPane;
import component.dialog.bug.ChangeBugStatusDialog;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

import common.GuiConstants;
import common.common.Errors;
import common.common.MessageBox;
import common.common.bug.Bug;
import common.exception.JsonTransformationException;
import common.exception.MessageSenderException;
import common.message.bug.BugDetailRequest;
import common.message.list.ModifyListRequest;
import common.utilities.Icons;
import common.utilities.Utilities;
import message.GuiMessageSender;

public class BugContextMenu
{
	public BugContextMenu(TableView<Bug> table, List<String> bugNumbers)
	{
		ContextMenu contextMenu = new ContextMenu();

		String removeTitle 		= (bugNumbers.size() > 1) ? "Remove Bugs" : "Remove Bug" + bugNumbers.get(0);
		String firefoxTitle 	= (bugNumbers.size() > 1) ? "Open Bugs in Bugzilla" : "Open Bug" + bugNumbers.get(0) + " in Bugzilla";
		String copyTitle 		= (bugNumbers.size() > 1) ? "Copy Bugs" : "Copy Bug" + bugNumbers.get(0);

		MenuItem remove 		= new MenuItem(removeTitle);
		MenuItem firefox 		= new MenuItem(firefoxTitle);
		MenuItem copy 			= new MenuItem(copyTitle);
		MenuItem comment 		= new MenuItem("Show Comments");
		MenuItem changeStatus 	= new MenuItem("Change Bug Status");
		MenuItem copyBugTitle 	= new MenuItem("Copy Bug Title");

		remove.setGraphic(Icons.createRemoveIcon());
		remove.setOnAction(e ->
		{
			// Remove from the table view GUI
			List<Bug> orsToRemove = new ArrayList<Bug>(table.getSelectionModel().getSelectedItems());

			// Start at the end of the list and work backwards, otherwise the indexes of the
			// table shift and it will only delete the first one
			for (int i = orsToRemove.size() - 1; i >= 0; i--)
			{
				table.getItems().remove(orsToRemove.get(i));
				table.refresh();
				InformationPane.getInstance().updateTexts();
			}

			table.getSelectionModel().clearSelection();

			// Now remove from the config file
			for (String number : bugNumbers)
			{
				if (!number.equals(""))
				{
					ModifyListRequest request = new ModifyListRequest(GuiConstants.CURRENT_LIST_FILE.getAbsolutePath(), "", number);
					try 
					{
						new GuiMessageSender().sendRequestMessage(request);
					}
					catch(JsonTransformationException | MessageSenderException e1)
					{
						MessageBox.showExceptionDialog(Errors.GENERAL, e1);
					}
					
				}
			}
		});

		firefox.setGraphic(Icons.createFirefoxIcon());
		firefox.setOnAction(e ->
		{
			for (String i : bugNumbers)
			{
				if (!i.equals(""))
				{
					try 
					{
						Utilities.openBugInFirefox(GuiConstants.BUGZILLA_URL, i);
					} 
					catch (IOException e1) 
					{
						MessageBox.showExceptionDialog(Errors.FIREFOX, e1);
					}
				}
			}
		});

		copy.setGraphic(Icons.createListIcon());
		copy.setOnAction(e -> Utilities.copy(table));

		comment.setGraphic(Icons.createCommentIcon());
		comment.setOnAction(e ->
		{
			try
			{
				String number = table.getSelectionModel().getSelectedItem().getNumber();
				BugDetailRequest request = new BugDetailRequest(number, GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
				new GuiMessageSender().sendRequestMessage(request);
			}
			catch (Exception ex)
			{
				MessageBox.showExceptionDialog(Errors.COMMENTS, ex);
			}
		});
		
		changeStatus.setGraphic(Icons.createChangeStatusIcon());
		changeStatus.setOnAction(e -> 
		{
			String number = table.getSelectionModel().getSelectedItem().getNumber();
			String status = table.getSelectionModel().getSelectedItem().getStatus();
			new ChangeBugStatusDialog(number, status);
		});
		
		copyBugTitle.setGraphic(Icons.createListIcon());
		copyBugTitle.setOnAction(e -> 
		{
			Bug bug = table.getSelectionModel().getSelectedItem();
			String title = "Bug" + bug.getNumber() + " - " + bug.getSummary();
			Utilities.copy(title);
		});

		contextMenu.getItems().addAll(remove, firefox, copy, comment, changeStatus);

		if (bugNumbers.size() == 1)
		{
			contextMenu.getItems().add(copyBugTitle);
		}

		int x = MouseInfo.getPointerInfo().getLocation().x;
		int y = MouseInfo.getPointerInfo().getLocation().y;

		contextMenu.show(table, x, y);
	}
}