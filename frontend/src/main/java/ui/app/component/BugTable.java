package ui.app.component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Labeled;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;
import ui.app.component.menu.BugContextMenu;
import ui.app.theme.Fonts;
import ui.app.theme.RowColours;
import common.bug.Bug;
import common.error.Errors;
import common.message.MessageBox;
import common.utility.Utilities;

/**
 * The main table in the middle of the UI that holds information about all the bugs.
 * 
 * @author Tom Hewitt
 */
public class BugTable
{
	private TableView<Bug> tableView;
	
	private static BugTable instance;

	@SuppressWarnings("unchecked")
	public BugTable()
	{
		instance = this;
		
		int SMALL = 5;
		int LARGE = 20;

		TableColumn<Bug, String> numberColumn 		= createColumn("Number", "id", SMALL);
		TableColumn<Bug, String> statusColumn 		= createColumn("Status", "status", SMALL);
		TableColumn<Bug, String> assignedColumn 	= createColumn("Assigned To", "assignedTo", SMALL);
		TableColumn<Bug, String> componentColumn 	= createColumn("Component", "component", SMALL);
		TableColumn<Bug, String> severity 			= createColumn("Severity", "severity", SMALL);
		TableColumn<Bug, String> summaryColumn 		= createColumn("Summary", "summary", LARGE);
		TableColumn<Bug, String> lastUpdatedColumn 	= createColumn("Last Updated", "lastUpdated", SMALL);

		tableView = new TableView<>();
		tableView.getColumns().addAll(numberColumn, statusColumn, assignedColumn, componentColumn, severity, summaryColumn, lastUpdatedColumn);
		tableView.setEditable(false);
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setRowFactory(tv ->
		{
			TableRow<Bug> row = new TableRow<>();
			row.setOnMouseClicked(event ->
			{
				// Double Click
				if (event.getClickCount() == 2 && (!row.isEmpty()))
				{
					// TODO use ApiRequestor
				}
			});

			return row;
		});

		tableView.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent e)
			{
				ObservableList<Bug> selectedBugs = tableView.getSelectionModel().getSelectedItems();

				// Right mouse click
				if (e.getButton() == MouseButton.SECONDARY)
				{
					List<String> selectedBugNumbers = new ArrayList<String>();

					for (Bug bug : selectedBugs)
						selectedBugNumbers.add(bug.getId());

					if (tableView.getItems().size() > 0)
						new BugContextMenu(tableView, selectedBugNumbers);
				}
			}
		});		
	}

	private TableColumn<Bug, String> createColumn(String columnLabel, String bugProperty, int widthPercentage)
	{
		TableColumn<Bug, String> column = new TableColumn<>(columnLabel);

		column.setMaxWidth(1f * Integer.MAX_VALUE * widthPercentage);
		column.setEditable(false);
		column.setCellValueFactory(new PropertyValueFactory<>(bugProperty));
		column.setCellFactory(new Callback<TableColumn<Bug, String>, TableCell<Bug, String>>()
		{
			@Override
			public TableCell<Bug, String> call(TableColumn<Bug, String> param)
			{
				return new TableCell<Bug, String>()
				{
					@SuppressWarnings("unchecked")
					@Override
					protected void updateItem(String item, boolean empty)
					{
						if (item == null) {
							return;
						}
						
						if (!empty)
						{
							super.updateItem(item, empty);
							int index = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();

							Bug bug = param.getTableView().getItems().get(index);
							TableRow<Bug> row = getTableRow();
							Hyperlink link = new Hyperlink();

							// First update hyperlink
							if (columnLabel.equals("Number"))
							{
								link.setText(bug.getId());
								link.setUnderline(true);
								link.setOnAction(e ->
								{
									try 
									{
										Utilities.openBugInBrowser(bug.getId());
									} 
									catch (IOException | URISyntaxException e1) 
									{
										MessageBox.showExceptionDialog(Errors.BROWSER, e1);
									}
								});
								setGraphic(link);
							}
							else
							{
								setText(column.getCellObservableValue(index).getValue());
							}

							// Set default colours
							setTextFill(Color.BLACK);
							link.setTextFill(Color.BLACK);
							setFont(Font.font("System", Fonts.FONT_SIZE_NORMAL));
							setAlignment(Pos.CENTER);
							
							colour(this, bug, link);

							if (row != null)
							{
								row.selectedProperty().addListener((obs, prevSelected, nowSelected) ->
								{
									if (row.getItem() != null)
									{
										if (prevSelected)
										{
											setTextFill(Color.BLACK);
											link.setTextFill(Color.BLACK);
											colour(this, row.getItem(), link);
										}
										else if (nowSelected)
										{
											setStyle(RowColours.SELECTED);
											setTextFill(Color.WHITE);
											link.setTextFill(Color.WHITE);
										}
									}
								});
							}
						}
					}
				};
			}
		});
		return column;
	}

	private void colour(Node n, Bug bug, Hyperlink link)
	{
		String status = bug.getStatus();
		String severity = bug.getSeverity();
		
		// Colour based on if fixed or not first
		if (status.equalsIgnoreCase("Fixed"))
		{
			n.setStyle(RowColours.FIXED);
		}
		else if (status.equalsIgnoreCase("Closed"))
		{
			n.setStyle(RowColours.CLOSED);
		}
		else if (status.equalsIgnoreCase("Worksforme"))
		{
			n.setStyle(RowColours.WORKS_FOR_ME);
		}
		else if (status.equalsIgnoreCase("Wontfix") || status.equalsIgnoreCase("Duplicate"))
		{
			((Labeled) n).setTextFill(Color.WHITE);
			link.setTextFill(Color.WHITE);
			n.setStyle(RowColours.NOFAULT);
		}

		// Now the severity
		else if (severity.equalsIgnoreCase("Blocker") || severity.equalsIgnoreCase("Critical"))
		{
			((Labeled) n).setTextFill(Color.WHITE);
			link.setTextFill(Color.WHITE);
			n.setStyle(RowColours.CRITICAL);
		}
		else if (severity.equalsIgnoreCase("Major"))
		{
			n.setStyle(RowColours.MAJOR);
		}
		else if (severity.equalsIgnoreCase("Minor"))
		{
			n.setStyle(RowColours.MINOR);
		}
		else if (severity.equalsIgnoreCase("Normal"))
		{
			n.setStyle(RowColours.NORMAL);
		}
	}
	
	public TableView<Bug> getTableView()
	{
		return tableView;
	}
	
	public static BugTable getInstance()
	{
		return instance;
	}
}