package ui.component.dialog;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import common.error.Errors;
import common.error.RequestException;
import common.message.ApiRequestor;
import common.message.ApiRequestor.ApiRequestType;
import common.message.Endpoints;
import common.message.MessageBox;
import common.utility.UiConstants;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.component.WindowsBar;
import ui.theme.Icons;
import ui.theme.UiBuilder;
import ui.theme.Sizes.Size;

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
			if (String.valueOf(UiConstants.REFRESH_TIME).equals(s))
			{
				combo.getSelectionModel().select(values.indexOf(s));
			}
		}
		
		Button applyButton = createButton("Apply", Size.SMALL, ButtonType.PRIMARY);
		applyButton.setOnAction(e ->
		{			
			JSONObject response;
			try
			{
				response = ApiRequestor.request(ApiRequestType.GET, Endpoints.CONFIG_SAVE("refreshRate", combo.getSelectionModel().getSelectedItem()));
			} 
			catch (RequestException ex)
			{
				MessageBox.showExceptionDialog(Errors.REQUEST, ex);
				return;
			}
			
			if (MessageBox.showErrorIfResponseNot200(response))
			{
				return;
			}

			UiConstants.REFRESH_TIME = Integer.valueOf(combo.getSelectionModel().getSelectedItem());
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