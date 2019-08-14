package component.menu;

import java.awt.MouseInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bugzilla.common.Errors;
import bugzilla.common.MessageBox;
import bugzilla.common.OR.OR;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.message.OR.ORDetailRequest;
import bugzilla.message.document.ExcelRequest;
import bugzilla.message.list.ModifyListRequest;
import bugzilla.utilities.Icons;
import bugzilla.utilities.Utilities;

import component.InformationPane;
import component.dialog.ReleaseNoteDialog;
import component.dialog.SubsystemTestDialog;
import component.dialog.OR.ChangeORStatusDialog;
import component.dialog.unittest.UnitTestDialog;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

import common.GuiConstants;

import message.GuiMessageSender;

public class ORContextMenu
{
	public ORContextMenu(TableView<OR> table, List<String> orNumbers)
	{
		ContextMenu contextMenu = new ContextMenu();

		String removeTitle 		= (orNumbers.size() > 1) ? "Remove ORs" : "Remove OR" + orNumbers.get(0);
		String firefoxTitle 	= (orNumbers.size() > 1) ? "Open ORs in Bugzilla" : "Open OR" + orNumbers.get(0) + " in Bugzilla";
		String copyTitle 		= (orNumbers.size() > 1) ? "Copy ORs" : "Copy OR" + orNumbers.get(0);

		MenuItem remove 		= new MenuItem(removeTitle);
		MenuItem firefox 		= new MenuItem(firefoxTitle);
		MenuItem copy 			= new MenuItem(copyTitle);
		MenuItem comment 		= new MenuItem("Show Comments");
		MenuItem releaseNote 	= new MenuItem("Create Release Note");
		MenuItem unitTest 		= new MenuItem("Create Unit Test");
		MenuItem subsystemTest 	= new MenuItem("Create Subsystem Test");
		MenuItem excel 			= new MenuItem("Export To Excel");
		MenuItem changeStatus 	= new MenuItem("Change OR Status");
		MenuItem copyORTitle 	= new MenuItem("Copy OR Title");

		Menu documentsMenu = new Menu("Documents");
		documentsMenu.setGraphic(Icons.createDocumentIcon());

		remove.setGraphic(Icons.createRemoveIcon());
		remove.setOnAction(e ->
		{
			// Remove from the table view GUI
			List<OR> orsToRemove = new ArrayList<OR>(table.getSelectionModel().getSelectedItems());

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
			for (String number : orNumbers)
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
			for (String i : orNumbers)
			{
				if (!i.equals(""))
				{
					try 
					{
						Utilities.openORInFirefox(GuiConstants.BUGZILLA_URL, i);
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
				ORDetailRequest request = new ORDetailRequest(number, GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
				new GuiMessageSender().sendRequestMessage(request);
			}
			catch (Exception ex)
			{
				MessageBox.showExceptionDialog(Errors.COMMENTS, ex);
			}
		});

		releaseNote.setOnAction(e ->
		{
			ObservableList<OR> ors = table.getSelectionModel().getSelectedItems();
			new ReleaseNoteDialog(ors);
		});

		unitTest.setOnAction(e -> new UnitTestDialog(table.getSelectionModel().getSelectedItem()));
		subsystemTest.setOnAction(e -> new SubsystemTestDialog(table.getSelectionModel().getSelectedItems()));
		
		excel.setOnAction(e ->
		{
			ObservableList<OR> ors = table.getItems();
			try
			{
				new GuiMessageSender().sendRequestMessage(new ExcelRequest(ors));
			}
			catch(JsonTransformationException | MessageSenderException e1)
			{
				MessageBox.showExceptionDialog(Errors.GENERAL, e1);
			}
		});
		
		changeStatus.setGraphic(Icons.createChangeStatusIcon());
		changeStatus.setOnAction(e -> 
		{
			String number = table.getSelectionModel().getSelectedItem().getNumber();
			String status = table.getSelectionModel().getSelectedItem().getStatus();
			new ChangeORStatusDialog(number, status);
		});
		
		copyORTitle.setGraphic(Icons.createListIcon());
		copyORTitle.setOnAction(e -> 
		{
			OR or = table.getSelectionModel().getSelectedItem();
			String title = "OR" + or.getNumber() + " - " + or.getSummary();
			Utilities.copy(title);
		});

		documentsMenu.getItems().addAll(excel, releaseNote, subsystemTest);
		contextMenu.getItems().addAll(remove, firefox, copy, comment, documentsMenu, changeStatus);

		if (orNumbers.size() == 1)
		{
			documentsMenu.getItems().add(unitTest);
			contextMenu.getItems().add(copyORTitle);
		}

		int x = MouseInfo.getPointerInfo().getLocation().x;
		int y = MouseInfo.getPointerInfo().getLocation().y;

		contextMenu.show(table, x, y);
	}
}