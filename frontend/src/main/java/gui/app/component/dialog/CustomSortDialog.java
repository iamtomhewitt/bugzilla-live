package gui.app.component.dialog;

import java.util.Collections;

import gui.app.common.GuiMethods;
import gui.app.component.BugTable;
import gui.app.component.WindowsBar;
import gui.app.theme.UiBuilder;
import gui.app.theme.Sizes.Size;
import gui.app.theme.Fonts;
import gui.app.theme.Icons;
import gui.app.theme.Sizes;
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

import common.bug.Bug;

public class CustomSortDialog extends UiBuilder
{
	private ObservableList<TableColumn<Bug, ?>> tableViewColumns = BugTable.getInstance().getTableView().getColumns();
				
	public CustomSortDialog()
	{
		Stage stage = new Stage();
		
		ComboBox<String> primaryComboBox 	= createComboBox("");
		ComboBox<String> secondaryComboBox 	= createComboBox("");
		
		Label primaryLabel = createTitle("Primary", Fonts.FONT_SIZE_NORMAL);
		Label secondaryLabel = createTitle("Secondary", Fonts.FONT_SIZE_NORMAL);

		Button applyButton = createButton("Apply", Size.SMALL, ButtonType.PRIMARY);
		applyButton.setPrefWidth(Sizes.BUTTON_WIDTH_SMALL);
		applyButton.setOnAction(e ->
		{
			String primary = "";
			String secondary = "";
			
			primary 	= primaryComboBox.getSelectionModel().getSelectedItem();
			secondary 	= secondaryComboBox.getSelectionModel().getSelectedItem();

			GuiMethods.sortBugs(BugTable.getInstance().getTableView().getItems(), true, primary, secondary);
			
			stage.close();
		});

		
		populateComboBox(primaryComboBox);
		populateComboBox(secondaryComboBox);
		
		VBox primary = new VBox(primaryLabel, primaryComboBox);
		VBox secondary = new VBox(secondaryLabel, secondaryComboBox);
		
		primary.setAlignment(Pos.CENTER);
		secondary.setAlignment(Pos.CENTER);
		
		VBox vbox = new VBox(primary, secondary, applyButton);
		vbox.setSpacing(20);
		vbox.setPadding(new Insets(10));
		vbox.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, vbox, "Custom Sort"), 175, 250);
		stage.setScene(scene);
		stage.getIcons().add(new Icons().createListIcon().getImage());
		stage.show();
		stage.centerOnScreen();
	}

	private void populateComboBox(ComboBox<String> comboBox)
	{
		for (int i = 0; i < tableViewColumns.size(); i++)
		{
			String columnName = tableViewColumns.get(i).getText();			
			comboBox.getItems().add(columnName);
		}
		
		Collections.sort(comboBox.getItems());
	}
}
