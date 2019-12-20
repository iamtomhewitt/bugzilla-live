package ui.app.component.dialog.bug;

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
import ui.app.common.UiConstants;
import ui.app.common.UiMethods;
import ui.app.common.RequestType;
import ui.app.component.WindowsBar;
import ui.app.theme.Fonts;
import ui.app.theme.Icons;
import ui.app.theme.UiBuilder;
import ui.app.theme.Sizes.Size;
import common.error.Errors;
import common.error.RequestException;
import common.message.ApiRequestor;
import common.message.Endpoints;
import common.message.MessageBox;

public class GetBugsDialog extends UiBuilder
{
	private TextField emailField = new TextField();
	private VBox vbox = new VBox();	

	public GetBugsDialog()
	{
		Stage stage = new Stage();
		
		Label title = createTitle("User Bugs", Fonts.FONT_SIZE_LARGE);

		emailField = createTextField("email address", Size.LARGE);
		emailField.setOnKeyPressed(e->
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
			}
		});

		Button getBugsButton = createButton("Get Bugs", Size.SMALL, ButtonType.PRIMARY);
		Button myBugsButton = createButton("Get My Bugs", Size.SMALL, ButtonType.PRIMARY);

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
		});
		
		myBugsButton.setOnAction(e ->
		{
			UiConstants.REQUEST_TYPE = RequestType.CURRENT_USER;
			UiConstants.CURRENT_LIST_FILE = null;
			
			try
			{
				UiMethods.requestRefreshOfCurrentUserBugs();
			} 
			catch (Exception e1)
			{
				MessageBox.showExceptionDialog(Errors.REQUEST, e1);
				return;
			}
			
			stage.close();
		});
				
		VBox fields = new VBox(emailField);
		fields.setAlignment(Pos.CENTER);
		fields.setSpacing(10);
		fields.setPadding(new Insets(10));
		
		HBox buttons = new HBox(getBugsButton, myBugsButton);
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
	
	private void execute() throws RequestException
	{
		if (emailField.getText().isEmpty())
		{
			MessageBox.showDialog(Errors.MISSING_FIELD);
			return;
		}
		
		UiConstants.CURRENT_LIST_FILE = null;
		UiConstants.REQUEST_TYPE = RequestType.USER;
				
		String email = emailField.getText();
		String response;
		
		if (!email.matches(UiConstants.EMAIL_REGEX))
		{
			MessageBox.showDialog(Errors.INVALID_EMAIL);
			return;
		}
		UiMethods.clearTable();

		response = ApiRequestor.request(Endpoints.BUGS_EMAIL(email));

		if (MessageBox.showErrorIfResponseNot200(response))
		{
			return;
		}

		UiMethods.updateBugsInTable(response);
	}
}