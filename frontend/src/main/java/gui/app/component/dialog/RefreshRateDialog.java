package gui.app.component.dialog;

import java.util.Arrays;
import java.util.List;
import gui.app.common.GuiConstants;
import gui.app.component.WindowsBar;

import gui.app.theme.GuiBuilder;
import gui.app.theme.Icons;
import gui.app.theme.Sizes;
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

public class RefreshRateDialog
{
	private Stage stage = new Stage();
	private VBox vbox = new VBox();
	
	public RefreshRateDialog()
	{		
		List<String> values = Arrays.asList("30", "60", "90", "120", "150", "180", "210", "240", "270", "300");
		
		ComboBox<String> combo = new ComboBox<String>();
		combo.getItems().addAll(values);
		
		// Show the currently selected rate first
		for (String s : values)
		{
			if (String.valueOf(GuiConstants.REFRESH_TIME).equals(s))
			{
				combo.getSelectionModel().select(values.indexOf(s));
			}
		}
		
		Button applyButton = new Button("Apply");
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
		
		HBox times = new HBox(combo,new Label("seconds"));
		times.setAlignment(Pos.CENTER);
		times.setSpacing(10);
		
		HBox buttons = new HBox(applyButton);
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);

		vbox.getChildren().addAll(times, buttons);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		vbox.setStyle("-fx-background-color: white");
		
		GuiBuilder.styleComboBox(combo);
		GuiBuilder.stylePrimaryButton(applyButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);

		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, vbox, "Change Refresh Rate"), 225, 125);
		stage.setScene(scene);
		stage.getIcons().add(new Icons().createRefreshIcon().getImage());
		stage.show();
		stage.centerOnScreen();
	}
}