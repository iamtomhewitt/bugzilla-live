package ui.component.dialog.bug;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

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
import ui.component.WindowsBar;
import ui.theme.Fonts;
import ui.theme.Icons;
import ui.theme.UiBuilder;
import ui.theme.Sizes.Size;
import common.error.Errors;
import common.error.RequestException;
import common.message.ApiRequestor;
import common.message.ApiRequestor.ApiRequestType;
import common.message.Endpoints;
import common.message.MessageBox;
import common.utility.UiConstants;

public class ChangeBugStatusDialog extends UiBuilder
{
	private Stage stage = new Stage();
	
	private String selectedStatus = "";
	private String selectedResolution = "";

	public ChangeBugStatusDialog(String number, String status)
	{
		Map<String, String> statusMap = this.generateStatusMap();
		List<String> resolutionList = this.generateResolutionList();
		
		selectedStatus = statusMap.get(status);
		selectedResolution = resolutionList.get(0);
		
		Label statusLabel 		= createTitle("Status\t\t", Fonts.FONT_SIZE_LARGE);
		Label resolutionLabel 	= createTitle("Resolution\t", Fonts.FONT_SIZE_LARGE);

		TextArea comment = new TextArea();
		comment.setMinHeight(150);
		comment.setFont(Font.font(Fonts.FONT, FontPosture.REGULAR, Fonts.FONT_SIZE_NORMAL));
		comment.setWrapText(true);
		
		ComboBox<String> resolutionComboBox = createComboBox();
		resolutionComboBox.getItems().addAll(resolutionList);
		resolutionComboBox.setOnAction(e ->
		{
			selectedResolution = resolutionComboBox.getSelectionModel().getSelectedItem();
			comment.setText("Updated to " + selectedStatus + " (" + selectedResolution + ").");
		});

		ComboBox<String> statusComboBox = createComboBox();
		statusComboBox.getItems().addAll(statusMap.get(status));
		statusComboBox.setOnAction(e ->
		{
			selectedStatus = statusComboBox.getSelectionModel().getSelectedItem();
			comment.setText("Updated to " + selectedStatus + " (" + selectedResolution + ").");
		});

		Button submitButton = createButton("Submit", Size.SMALL, ButtonType.PRIMARY);
		submitButton.setOnAction(e ->
		{
			if (comment.getText().isEmpty() || selectedStatus.isEmpty())
			{
				MessageBox.showDialog(Errors.MISSING_FIELD);
				return;
			}
			if (MessageBox.showConfirmDialog("Are you sure you want to update this bug to " + selectedStatus + "("+selectedResolution+") ?"))
			{				
				try 
				{
					JSONObject response = ApiRequestor.request(ApiRequestType.PUT, Endpoints.BUGS_CHANGE_STATUS(number, selectedStatus, selectedResolution, comment.getText(), UiConstants.APIKEY));
					MessageBox.showErrorIfResponseNot200(response);
				} 
				catch (RequestException e1) 
				{
					MessageBox.showExceptionDialog(Errors.REQUEST, e1);
				}
				
				stage.close();
			}
		});
		
		HBox statusHbox = new HBox(statusLabel, statusComboBox);
		statusHbox.setPadding(new Insets(10));
		statusHbox.setSpacing(10);
		statusHbox.setAlignment(Pos.CENTER);
		
		HBox resolutionHbox = new HBox(resolutionLabel, resolutionComboBox);
		resolutionHbox.setPadding(new Insets(10));
		resolutionHbox.setSpacing(10);
		resolutionHbox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(statusHbox, resolutionHbox, comment, submitButton);
		vbox.setPadding(new Insets(5));
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);	

		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, vbox, "Bug " + number + " Change Status"), 375, 400);
		stage.setTitle("Bug" + number + " Change Status");
		stage.getIcons().add(new Icons().createChangeStatusIcon().getImage());
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}
	
	private Map<String, String> generateStatusMap()
	{
		Map<String, String> statusMap = new HashMap<String, String>();
		statusMap.put("UNCONFIRMED", "RESOLVED");
		statusMap.put("RESOLVED", "UNCONFIRMED");
		return statusMap;
	}
	
	private List<String> generateResolutionList()
	{
		return Arrays.asList("", "INVALID", "WONTFIX", "INACTIVE", "DUPLICATE", "WORKSFORME", "MOVED");
	}
}
