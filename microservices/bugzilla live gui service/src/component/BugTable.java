package component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bugzilla.common.Errors;
import bugzilla.common.Fonts;
import bugzilla.common.MessageBox;
import bugzilla.common.bug.Bug;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.message.bug.BugDetailRequest;
import bugzilla.utilities.Utilities;
import component.menu.BugContextMenu;
import theme.RowColours;
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
import common.GuiConstants;
import message.GuiMessageSender;

/**
 * The main table in the middle of the GUI that holds information about all the bugs.
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
		
		int X_SMALL = 3;
		int SMALL = 5;
		int MEDIUM = 10;
		int LARGE = 20;

		TableColumn<Bug, String> numberColumn 	= createColumn("Number", "number", X_SMALL);
		TableColumn<Bug, String> statusColumn 	= createColumn("Status", "status", SMALL);
		TableColumn<Bug, String> assignedColumn 	= createColumn("Assigned To", "assignedTo", SMALL);
		TableColumn<Bug, String> productColumn 	= createColumn("Subsystem", "product", SMALL);
		TableColumn<Bug, String> componentColumn = createColumn("Component", "component", SMALL);
		TableColumn<Bug, String> severity 		= createColumn("Severity", "severity", SMALL);
		TableColumn<Bug, String> summaryColumn 	= createColumn("Summary", "summary", LARGE);
		TableColumn<Bug, String> genFromColumn 	= createColumn("Generated From", "generatedFrom", MEDIUM);
		TableColumn<Bug, String> intExtColumn 	= createColumn("Int/Ext", "internalExternal", SMALL);
		TableColumn<Bug, String> systemColumn 	= createColumn("Environment", "system", SMALL);
		TableColumn<Bug, String> segmentColumn 	= createColumn("Segment Release", "segmentRelease", SMALL);
		TableColumn<Bug, String> lastUpdatedColumn = createColumn("Last Updated", "lastUpdated", SMALL);

		tableView = new TableView<>();
		tableView.getColumns().addAll(numberColumn, statusColumn, assignedColumn, productColumn, componentColumn, severity, summaryColumn, genFromColumn, intExtColumn, systemColumn, segmentColumn, lastUpdatedColumn);
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
					try
					{
						BugDetailRequest request = new BugDetailRequest(row.getItem().getNumber(), GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
						new GuiMessageSender().sendRequestMessage(request);
					}
					catch (JsonTransformationException | MessageSenderException e)
					{
						MessageBox.showExceptionDialog(Errors.COMMENTS + row.getItem().getNumber(), e);
					}
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
						selectedBugNumbers.add(bug.getNumber());

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
								link.setText(bug.getNumber());
								link.setUnderline(true);
								link.setOnAction(e ->
								{
									try 
									{
										Utilities.openBugInFirefox(GuiConstants.BUGZILLA_URL, bug.getNumber());
									} 
									catch (IOException e1) 
									{
										MessageBox.showExceptionDialog(Errors.FIREFOX, e1);
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
		// Colour based on if fixed or not first
		if (bug.getStatus().equalsIgnoreCase("Fixed"))
		{
			n.setStyle(RowColours.FIXED);
		}
		else if (bug.getStatus().equalsIgnoreCase("Coded"))
		{
			n.setStyle(RowColours.CODED);
		}
		else if (bug.getStatus().equalsIgnoreCase("Closed"))
		{
			n.setStyle(RowColours.CLOSED);
		}
		else if (bug.getStatus().equalsIgnoreCase("Addressed"))
		{
			n.setStyle(RowColours.ADDRESSED);
		}
		else if (bug.getStatus().equalsIgnoreCase("No Fault"))
		{
			((Labeled) n).setTextFill(Color.WHITE);
			link.setTextFill(Color.WHITE);
			n.setStyle(RowColours.NOFAULT);
		}
		else if (bug.getStatus().equalsIgnoreCase("Released"))
		{
			n.setStyle(RowColours.RELEASED);
		}
		else if (bug.getStatus().equalsIgnoreCase("Built"))
		{
			n.setStyle(RowColours.BUILT);
		}

		// Now the status
		else if (bug.getSeverity().equalsIgnoreCase("Critical"))
		{
			((Labeled) n).setTextFill(Color.WHITE);
			link.setTextFill(Color.WHITE);
			n.setStyle(RowColours.CRITICAL);
		}
		else if (bug.getSeverity().equalsIgnoreCase("High"))
		{
			n.setStyle(RowColours.HIGH);
		}
		else if (bug.getSeverity().equalsIgnoreCase("Medium"))
		{
			n.setStyle(RowColours.MEDIUM);
		}
		else if (bug.getSeverity().equalsIgnoreCase("Low"))
		{
			n.setStyle(RowColours.LOW);
		}
		else if (bug.getSeverity().equalsIgnoreCase("Unknown"))
		{
			n.setStyle(RowColours.UNKNOWN);
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