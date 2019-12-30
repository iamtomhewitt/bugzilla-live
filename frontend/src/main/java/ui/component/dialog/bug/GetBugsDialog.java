package ui.component.dialog.bug;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.component.WindowsBar;
import ui.theme.Fonts;
import ui.theme.Icons;
import ui.theme.UiBuilder;
import ui.theme.Sizes.Size;

import org.json.JSONObject;

import common.error.Errors;
import common.error.JsonTransformationException;
import common.error.RequestException;
import common.message.ApiRequestor;
import common.message.ApiRequestor.ApiRequestType;
import common.message.Endpoints;
import common.message.MessageBox;
import common.message.RequestType;
import common.utility.UiConstants;
import common.utility.UiMethods;

public class GetBugsDialog extends UiBuilder
{
	private TextField usernameField = new TextField();

	public GetBugsDialog()
	{
		Stage stage = new Stage();
		VBox vbox = new VBox();	
		Label title = createTitle("User Bugs", Fonts.FONT_SIZE_LARGE);

		usernameField = createTextField("email address", Size.LARGE);
		usernameField.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				stage.close();
				try
				{
					execute();
				}
				catch (RequestException e1)
				{
					MessageBox.showExceptionDialog(Errors.REQUEST, e1);
				} 
				catch (JsonTransformationException e1)
				{
					MessageBox.showExceptionDialog(Errors.JACKSON_FROM, e1);
				}
			}
		});

		Button getBugsButton = createButton("Get Bugs", Size.SMALL, ButtonType.PRIMARY);
		getBugsButton.setOnAction(e -> 
		{
			stage.close();
			try
			{
				execute();
			}
			catch (RequestException e1)
			{
				MessageBox.showExceptionDialog(Errors.REQUEST, e1);
			} 
			catch (JsonTransformationException e1)
			{
				MessageBox.showExceptionDialog(Errors.JACKSON_FROM, e1);
			}
		});
				
		VBox fields = new VBox(usernameField);
		fields.setAlignment(Pos.CENTER);
		fields.setSpacing(10);
		fields.setPadding(new Insets(10));
		
		HBox buttons = new HBox(getBugsButton);
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);

		vbox = new VBox(title, fields, buttons);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		
		Platform.runLater(() -> getBugsButton.requestFocus());
		
        stage.setScene(new Scene(WindowsBar.createWindowsBar(stage, vbox, "Get Bugs"), 300, 325));
        stage.show();
        stage.getIcons().add(new Icons().createBugzillaIcon().getImage());
        stage.centerOnScreen();
	}
	
	private void execute() throws RequestException, JsonTransformationException
	{
		if (usernameField.getText().isEmpty())
		{
			MessageBox.showDialog(Errors.MISSING_FIELD);
			return;
		}
		
		UiConstants.CURRENT_LIST = null;
		UiConstants.REQUEST_TYPE = RequestType.USER;
						
		UiMethods.clearTable();

		String url = Endpoints.BUGS_USERNAME(usernameField.getText());
		JSONObject response = ApiRequestor.request(ApiRequestType.GET, url);

		if (MessageBox.showErrorIfResponseNot200(response))
		{
			return;
		}

		UiMethods.updateBugsInTable(response);
	}
}