package gui.app.component.menu;

import java.awt.MouseInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gui.app.common.GuiConstants;
import gui.app.component.InformationPane;
import gui.app.component.dialog.bug.ChangeBugStatusDialog;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

import common.Errors;
import common.MessageBox;
import common.bug.Bug;
import common.message.ApiRequestor;
import common.utilities.Icons;
import common.utilities.Utilities;

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
			List<Bug> bugsToRemove = new ArrayList<Bug>(table.getSelectionModel().getSelectedItems());

			// Start at the end of the list and work backwards, otherwise the indexes of the
			// table shift and it will only delete the first one
			for (int i = bugsToRemove.size() - 1; i >= 0; i--)
			{
				table.getItems().remove(bugsToRemove.get(i));
				table.refresh();
				InformationPane.getInstance().updateTexts();
			}

			table.getSelectionModel().clearSelection();

			String numbers 	= String.join(",", bugNumbers);
			String filename = GuiConstants.CURRENT_LIST_FILE.split("\\.")[0];
			String url 		= String.format("/list/modify?name=%s&remove=%s", filename, numbers);
			String response = ApiRequestor.request(url);
			
			MessageBox.showErrorIfResponseNot200(response);
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
				String number = table.getSelectionModel().getSelectedItem().getId();
				//BugDetailRequest request = new BugDetailRequest(number, GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
				// TODO use ApiRequestor
			}
			catch (Exception ex)
			{
				MessageBox.showExceptionDialog(Errors.COMMENTS, ex);
			}
		});
		
		changeStatus.setGraphic(Icons.createChangeStatusIcon());
		changeStatus.setOnAction(e -> 
		{
			String number = table.getSelectionModel().getSelectedItem().getId();
			String status = table.getSelectionModel().getSelectedItem().getStatus();
			new ChangeBugStatusDialog(number, status);
		});
		
		copyBugTitle.setGraphic(Icons.createListIcon());
		copyBugTitle.setOnAction(e -> 
		{
			Bug bug = table.getSelectionModel().getSelectedItem();
			String title = "Bug" + bug.getId() + " - " + bug.getSummary();
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