package gui.app.component.dialog.bug;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gui.app.component.WindowsBar;
import gui.app.theme.Fonts;
import gui.app.theme.UiBuilder;
import gui.app.theme.Icons;
import gui.app.theme.Sizes;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import common.exception.Errors;
import common.message.MessageBox;

public class ChangeBugStatusDialog extends VBox 
{
	private Stage stage = new Stage();
	
	private String selectedStatus = "";

	public ChangeBugStatusDialog(String number, String status)
	{
		Map<String, List<String>> statusMap = this.generateStatusMap();
		
		Label statusLabel = new Label("Status");

		TextArea comment = new TextArea();
		comment.setMinHeight(250);
		comment.setFont(Font.font(Fonts.FONT, FontPosture.REGULAR, Fonts.FONT_SIZE_NORMAL));
		comment.setWrapText(true);

		//selectedStatus = statusMap.get(status).get(0);

		ComboBox<String> statusComboBox = new ComboBox<String>();
		statusComboBox.getItems().addAll(statusMap.get(status));
		statusComboBox.setOnAction(e ->
		{
			selectedStatus = statusComboBox.getSelectionModel().getSelectedItem();
			comment.setText("Updated to " + selectedStatus + ".");
		});

		Button submitButton = new Button("Submit");
		submitButton.setOnAction(e ->
		{
			if (comment.getText().isEmpty() || selectedStatus.isEmpty())
			{
				MessageBox.showDialog(Errors.MISSING_FIELD);
				return;
			}
			if (MessageBox.showConfirmDialog("Are you sure you want to update this bug to " + selectedStatus + "?"))
			{				
				// TODO use ApiRequestor
				stage.close();
			}
		});

		HBox statusHBox = new HBox(statusLabel, statusComboBox);
		statusHBox.setPadding(new Insets(10));
		statusHBox.setSpacing(10);
		statusHBox.setAlignment(Pos.CENTER);

		this.getChildren().addAll(statusHBox, comment, submitButton);
		this.setPadding(new Insets(5));
		this.setSpacing(10);
		this.setAlignment(Pos.CENTER);

		UiBuilder.stylePrimaryButton(submitButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		UiBuilder.styleTitle(statusLabel);
		UiBuilder.styleComboBox(statusComboBox);

		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, this, "Bug" + number + " Change Status"), 375, 400);
		stage.setTitle("Bug" + number + " Change Status");
		stage.getIcons().add(new Icons().createChangeStatusIcon().getImage());
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}
	
	private Map<String, List<String>> generateStatusMap()
	{
		Map<String, List<String>> statusMap = new HashMap<String, List<String>>();
		statusMap.put("Investigation", 	Arrays.asList("Diagnosed"));
		statusMap.put("Diagnosed", 		Arrays.asList("Investigation", "Coded", "Addressed", "No Fault"));
		statusMap.put("Coded", 			Arrays.asList("Investigaton", "Built"));
		statusMap.put("Built", 			Arrays.asList("Investigation", "Released"));
		statusMap.put("Released", 		Arrays.asList("Fixed"));
		return statusMap;
	}
}
