package gui.app.component.dialog;

import java.util.Arrays;
import java.util.List;
import gui.app.common.GuiConstants;
import gui.app.component.WindowsBar;

import gui.app.theme.UiBuilder;
import gui.app.theme.Sizes.Size;
import gui.app.theme.Icons;
import common.exception.Errors;
import common.message.ApiRequestor;
import common.message.Endpoints;
import common.message.MessageBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RefreshRateDialog extends UiBuilder
{	
	public RefreshRateDialog()
	{		
		Stage stage = new Stage();

		List<String> values = Arrays.asList("30", "60", "90", "120", "150", "180", "210", "240", "270", "300");
		
		ComboBox<String> combo = createComboBox(values.stream().toArray(String[]::new));
		combo.getItems().addAll(values);
		
		// Show the currently selected rate first
		for (String s : values)
		{
			if (String.valueOf(GuiConstants.REFRESH_TIME).equals(s))
			{
				combo.getSelectionModel().select(values.indexOf(s));
			}
		}
		
		Button applyButton = createButton("Apply", Size.SMALL, ButtonType.PRIMARY);
		applyButton.setOnAction(e ->
		{			
			String response;
			try
			{
				response = ApiRequestor.request(Endpoints.CONFIG_SAVE("refreshRate", combo.getSelectionModel().getSelectedItem()));
			} 
			catch (Exception ex)
			{
				MessageBox.showExceptionDialog(Errors.REQUEST, ex);
				return;
			}
			
			if (MessageBox.showErrorIfResponseNot200(response))
			{
				return;
			}

			GuiConstants.REFRESH_TIME = Integer.valueOf(combo.getSelectionModel().getSelectedItem());
			stage.close();
		});
		
		HBox times = new HBox(combo, new Label("seconds"));
		times.setAlignment(Pos.CENTER);
		times.setSpacing(10);
		
		HBox buttons = new HBox(applyButton);
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);

		VBox vbox = new VBox(times, buttons);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		vbox.setStyle("-fx-background-color: white");
		
		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, vbox, "Change Refresh Rate"), 225, 125);
		stage.setScene(scene);
		stage.getIcons().add(new Icons().createRefreshIcon().getImage());
		stage.show();
		stage.centerOnScreen();
	}
}