package gui.app.component.dialog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gui.app.common.GuiConstants;
import gui.app.component.WindowsBar;

import gui.app.theme.GuiStyler;
import gui.app.theme.Sizes;
import common.Errors;
import common.MessageBox;
import common.exception.JsonTransformationException;
import common.exception.MessageSenderException;
import common.message.config.ApplicationSaveRequest;
import common.utilities.Icons;
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
			if (String.valueOf(GuiConstants.REFRESH_TIME).equals(s))
				combo.getSelectionModel().select(values.indexOf(s));
		
		Button applyButton = new Button("Apply");
		applyButton.setOnAction(e ->
		{
			// Create a new request message telling the config service that we want to save the refresh rate
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("refreshrate", combo.getSelectionModel().getSelectedItem());

			ApplicationSaveRequest request = new ApplicationSaveRequest(properties);
			// TODO use APIReqeusotr
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
		
		GuiStyler.styleComboBox(combo);
		GuiStyler.stylePrimaryButton(applyButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);

		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, vbox, "Change Refresh Rate"), 225, 125);
		stage.setScene(scene);
		stage.getIcons().add(Icons.createRefreshIcon().getImage());
		stage.show();
		stage.centerOnScreen();
	}
}