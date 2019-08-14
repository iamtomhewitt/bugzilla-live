package component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bugzilla.common.Errors;
import bugzilla.common.Fonts;
import bugzilla.common.MessageBox;
import bugzilla.common.OR.OR;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.message.OR.ORDetailRequest;
import bugzilla.utilities.Utilities;
import component.menu.ORContextMenu;
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
 * The main table in the middle of the GUI that holds information about all the ORs.
 * 
 * @author Tom Hewitt
 */
public class ORTable
{
	private TableView<OR> tableView;
	
	private static ORTable instance;

	@SuppressWarnings("unchecked")
	public ORTable()
	{
		instance = this;
		
		int X_SMALL = 3;
		int SMALL = 5;
		int MEDIUM = 10;
		int LARGE = 20;

		TableColumn<OR, String> numberColumn 	= createColumn("Number", "number", X_SMALL);
		TableColumn<OR, String> statusColumn 	= createColumn("Status", "status", SMALL);
		TableColumn<OR, String> assignedColumn 	= createColumn("Assigned To", "assignedTo", SMALL);
		TableColumn<OR, String> productColumn 	= createColumn("Subsystem", "product", SMALL);
		TableColumn<OR, String> componentColumn = createColumn("Component", "component", SMALL);
		TableColumn<OR, String> severity 		= createColumn("Severity", "severity", SMALL);
		TableColumn<OR, String> summaryColumn 	= createColumn("Summary", "summary", LARGE);
		TableColumn<OR, String> genFromColumn 	= createColumn("Generated From", "generatedFrom", MEDIUM);
		TableColumn<OR, String> intExtColumn 	= createColumn("Int/Ext", "internalExternal", SMALL);
		TableColumn<OR, String> systemColumn 	= createColumn("Environment", "system", SMALL);
		TableColumn<OR, String> segmentColumn 	= createColumn("Segment Release", "segmentRelease", SMALL);
		TableColumn<OR, String> lastUpdatedColumn = createColumn("Last Updated", "lastUpdated", SMALL);

		tableView = new TableView<>();
		tableView.getColumns().addAll(numberColumn, statusColumn, assignedColumn, productColumn, componentColumn, severity, summaryColumn, genFromColumn, intExtColumn, systemColumn, segmentColumn, lastUpdatedColumn);
		tableView.setEditable(false);
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setRowFactory(tv ->
		{
			TableRow<OR> row = new TableRow<>();
			row.setOnMouseClicked(event ->
			{
				// Double Click
				if (event.getClickCount() == 2 && (!row.isEmpty()))
				{
					try
					{
						ORDetailRequest request = new ORDetailRequest(row.getItem().getNumber(), GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
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
				ObservableList<OR> selectedORs = tableView.getSelectionModel().getSelectedItems();

				// Right mouse click
				if (e.getButton() == MouseButton.SECONDARY)
				{
					List<String> selectedORNumbers = new ArrayList<String>();

					for (OR or : selectedORs)
						selectedORNumbers.add(or.getNumber());

					if (tableView.getItems().size() > 0)
						new ORContextMenu(tableView, selectedORNumbers);
				}
			}
		});		
	}

	private TableColumn<OR, String> createColumn(String columnLabel, String ORProperty, int widthPercentage)
	{
		TableColumn<OR, String> column = new TableColumn<>(columnLabel);

		column.setMaxWidth(1f * Integer.MAX_VALUE * widthPercentage);
		column.setEditable(false);
		column.setCellValueFactory(new PropertyValueFactory<>(ORProperty));
		column.setCellFactory(new Callback<TableColumn<OR, String>, TableCell<OR, String>>()
		{
			@Override
			public TableCell<OR, String> call(TableColumn<OR, String> param)
			{
				return new TableCell<OR, String>()
				{
					@SuppressWarnings("unchecked")
					@Override
					protected void updateItem(String item, boolean empty)
					{
						if (!empty)
						{
							super.updateItem(item, empty);
							int index = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();

							OR or = param.getTableView().getItems().get(index);
							TableRow<OR> row = getTableRow();
							Hyperlink link = new Hyperlink();

							// First update hyperlink
							if (columnLabel.equals("Number"))
							{
								link.setText(or.getNumber());
								link.setUnderline(true);
								link.setOnAction(e ->
								{
									try 
									{
										Utilities.openORInFirefox(GuiConstants.BUGZILLA_URL, or.getNumber());
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
							
							colour(this, or, link);

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

	private void colour(Node n, OR or, Hyperlink link)
	{
		// Colour based on if fixed or not first
		if (or.getStatus().equalsIgnoreCase("Fixed"))
		{
			n.setStyle(RowColours.FIXED);
		}
		else if (or.getStatus().equalsIgnoreCase("Coded"))
		{
			n.setStyle(RowColours.CODED);
		}
		else if (or.getStatus().equalsIgnoreCase("Closed"))
		{
			n.setStyle(RowColours.CLOSED);
		}
		else if (or.getStatus().equalsIgnoreCase("Addressed"))
		{
			n.setStyle(RowColours.ADDRESSED);
		}
		else if (or.getStatus().equalsIgnoreCase("No Fault"))
		{
			((Labeled) n).setTextFill(Color.WHITE);
			link.setTextFill(Color.WHITE);
			n.setStyle(RowColours.NOFAULT);
		}
		else if (or.getStatus().equalsIgnoreCase("Released"))
		{
			n.setStyle(RowColours.RELEASED);
		}
		else if (or.getStatus().equalsIgnoreCase("Built"))
		{
			n.setStyle(RowColours.BUILT);
		}

		// Now the status
		else if (or.getSeverity().equalsIgnoreCase("Critical"))
		{
			((Labeled) n).setTextFill(Color.WHITE);
			link.setTextFill(Color.WHITE);
			n.setStyle(RowColours.CRITICAL);
		}
		else if (or.getSeverity().equalsIgnoreCase("High"))
		{
			n.setStyle(RowColours.HIGH);
		}
		else if (or.getSeverity().equalsIgnoreCase("Medium"))
		{
			n.setStyle(RowColours.MEDIUM);
		}
		else if (or.getSeverity().equalsIgnoreCase("Low"))
		{
			n.setStyle(RowColours.LOW);
		}
		else if (or.getSeverity().equalsIgnoreCase("Unknown"))
		{
			n.setStyle(RowColours.UNKNOWN);
		}
	}
	
	public TableView<OR> getTableView()
	{
		return tableView;
	}
	
	public static ORTable getInstance()
	{
		return instance;
	}
}