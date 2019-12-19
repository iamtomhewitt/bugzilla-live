package component.dialog.unittest;

import bugzilla.common.UnitTestStep;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class UnitTestTable
{
	private TableView<UnitTestStep> table = new TableView<UnitTestStep>();
	private ObservableList<UnitTestStep> data = FXCollections.observableArrayList(new UnitTestStep("1.", "Action", "Expected Result"));

	@SuppressWarnings("unchecked")
	public UnitTestTable()
	{
		TableColumn<UnitTestStep, String> stepColumn = createColumn("Step", "step");
		stepColumn.setOnEditCommit((CellEditEvent<UnitTestStep, String> t) -> ((UnitTestStep) t.getTableView().getItems().get(t.getTablePosition().getRow())).setStep(t.getNewValue()));

		TableColumn<UnitTestStep, String> actionColumn = createColumn("Action", "action");
		actionColumn.setOnEditCommit((CellEditEvent<UnitTestStep, String> t) -> ((UnitTestStep) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAction(t.getNewValue()));

		TableColumn<UnitTestStep, String> expectedResultColumn = createColumn("Expected Result", "expectedResult");
		expectedResultColumn.setOnEditCommit((CellEditEvent<UnitTestStep, String> t) -> ((UnitTestStep) t.getTableView().getItems().get(t.getTablePosition().getRow())).setExpectedResult(t.getNewValue()));
		
		stepColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		actionColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.38));
		expectedResultColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.38));
		
		// Add menu item to remove a step
		table.setRowFactory(new Callback<TableView<UnitTestStep>, TableRow<UnitTestStep>>()
		{
			@Override
			public TableRow<UnitTestStep> call(TableView<UnitTestStep> tableView)
			{
				final TableRow<UnitTestStep> row = new TableRow<>();
				final ContextMenu contextMenu = new ContextMenu();
				final MenuItem removeMenuItem = new MenuItem("Remove");
				removeMenuItem.setOnAction(e -> table.getItems().remove(row.getItem()));
				contextMenu.getItems().add(removeMenuItem);
				
				// Set context menu on row, but use a binding to make it only show for non-empty rows
				row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
				return row;
			}
		});

		stepColumn.setResizable(false);
        actionColumn.setResizable(false);
        expectedResultColumn.setResizable(false);

        table.setEditable(true);
		table.setMinWidth(400);
		table.setMinHeight(470);
		table.setMaxHeight(470);
		table.getColumns().addAll(stepColumn, actionColumn, expectedResultColumn);
		table.setItems(data);
	}

	private TableColumn<UnitTestStep, String> createColumn(String columnName, String property)
	{
		TableColumn<UnitTestStep, String> column = new TableColumn<UnitTestStep, String>(columnName);
		column.setCellValueFactory(new PropertyValueFactory<>(property));
		column.setCellFactory(tablecol -> 
		{
	        TableCell<UnitTestStep, String> cell = new WrappingTextFieldTableCell<UnitTestStep>();
	        Text text = new Text();
	        text.setText(text.toString());
	        text.wrappingWidthProperty().bind(cell.widthProperty());
	        return cell;
	    });

		return column;
	}

	public TableView<UnitTestStep> getTableView()
	{
		return table;
	}

	public ObservableList<UnitTestStep> getData()
	{
		return data;
	}
}
