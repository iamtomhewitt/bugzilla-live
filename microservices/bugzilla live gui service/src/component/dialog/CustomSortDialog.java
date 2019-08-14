package component.dialog;

import java.util.Collections;

import bugzilla.common.OR.OR;
import bugzilla.utilities.Icons;
import component.ORTable;
import component.WindowsBar;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import common.GuiMethods;

import theme.GuiStyler;
import theme.Sizes;

public class CustomSortDialog extends VBox
{
	private ObservableList<TableColumn<OR, ?>> tableViewColumns = ORTable.getInstance().getTableView().getColumns();
	
	private ComboBox<String> primaryComboBox = new ComboBox<String>();
	private ComboBox<String> secondaryComboBox = new ComboBox<String>();
	
	private Button applyButton = new Button("Apply");

	private Stage stage = new Stage();
	
	private Label primaryLabel = new Label("Primary");
	private Label secondaryLabel = new Label("Secondary");
	
	public CustomSortDialog()
	{
		populateComboBox(primaryComboBox);
		populateComboBox(secondaryComboBox);

		applyButton.setPrefWidth(Sizes.BUTTON_WIDTH_SMALL);
		applyButton.setOnAction(e ->
		{
			String primary = "";
			String secondary = "";
			
			primary 	= primaryComboBox.getSelectionModel().getSelectedItem();
			secondary 	= secondaryComboBox.getSelectionModel().getSelectedItem();
			
			primary		= primary.equals("Subsystem") ? "Product" : primary;
			secondary 	= secondary.equals("Subsystem") ? "Product" : secondary;

			GuiMethods.sortORs(true, primary, secondary);
			stage.close();
		});
		
		GuiStyler.stylePrimaryButton(applyButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiStyler.styleComboBox(primaryComboBox);
		GuiStyler.styleComboBox(secondaryComboBox);
		GuiStyler.styleTitle(primaryLabel);
		GuiStyler.styleTitle(secondaryLabel);
		
		VBox primary = new VBox(primaryLabel, primaryComboBox);
		VBox secondary = new VBox(secondaryLabel, secondaryComboBox);
		
		primary.setAlignment(Pos.CENTER);
		secondary.setAlignment(Pos.CENTER);
		
		this.getChildren().addAll(primary, secondary, applyButton);
		this.setSpacing(20);
		this.setPadding(new Insets(10));
		this.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, this, "Custom Sort"), 175, 250);
		stage.setScene(scene);
		stage.getIcons().add(Icons.createListIcon().getImage());
		stage.show();
		stage.centerOnScreen();
	}

	private void populateComboBox(ComboBox<String> comboBox)
	{
		for (int i = 0; i < tableViewColumns.size(); i++)
		{
			String columnName = tableViewColumns.get(i).getText();
			if (columnName.equals("Product"))
				columnName = "Subsystem";
			
			comboBox.getItems().add(columnName);
		}
		
		Collections.sort(comboBox.getItems());
	}
}
