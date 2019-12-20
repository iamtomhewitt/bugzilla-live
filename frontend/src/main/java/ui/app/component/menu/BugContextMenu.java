package ui.app.component.menu;

import java.awt.MouseInfo;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import ui.app.common.GuiConstants;
import ui.app.component.InformationPane;
import ui.app.component.dialog.bug.BugCommentDialog;
import ui.app.component.dialog.bug.ChangeBugStatusDialog;
import ui.app.theme.Icons;
import common.bug.Bug;
import common.error.Errors;
import common.message.ApiRequestor;
import common.message.Endpoints;
import common.message.MessageBox;
import common.utility.Utilities;

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

		remove.setGraphic(new Icons().createRemoveIcon());
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
			String response;
			
			try
			{
				response = ApiRequestor.request(Endpoints.LIST_MODIFY(filename, "", numbers));
			} 
			catch (Exception e1)
			{
				MessageBox.showExceptionDialog(Errors.REQUEST, e1);
				return;
			}

			MessageBox.showErrorIfResponseNot200(response);
		});

		firefox.setGraphic(new Icons().createBrowserIcon());
		firefox.setOnAction(e ->
		{
			for (String i : bugNumbers)
			{
				if (!i.equals(""))
				{
					try 
					{
						Utilities.openBugInBrowser(i);
					} 
					catch (IOException | URISyntaxException e1) 
					{
						MessageBox.showExceptionDialog(Errors.BROWSER, e1);
					}
				}
			}
		});

		copy.setGraphic(new Icons().createListIcon());
		copy.setOnAction(e -> Utilities.copy(table));

		comment.setGraphic(new Icons().createCommentIcon());
		comment.setOnAction(e ->
		{
			try
			{
				String number = table.getSelectionModel().getSelectedItem().getId();
				new BugCommentDialog(number);
			}
			catch (Exception ex)
			{
				MessageBox.showExceptionDialog(Errors.COMMENTS, ex);
			}
		});
		
		changeStatus.setGraphic(new Icons().createChangeStatusIcon());
		changeStatus.setOnAction(e -> 
		{
			String number = table.getSelectionModel().getSelectedItem().getId();
			String status = table.getSelectionModel().getSelectedItem().getStatus();
			new ChangeBugStatusDialog(number, status);
		});
		
		copyBugTitle.setGraphic(new Icons().createListIcon());
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