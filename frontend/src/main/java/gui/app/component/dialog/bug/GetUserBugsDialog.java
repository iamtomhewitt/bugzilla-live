package gui.app.component.dialog.bug;

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

import common.exception.Errors;
import common.message.ApiRequestor;
import common.message.Endpoints;
import common.message.MessageBox;
import gui.app.common.GuiConstants;
import gui.app.common.GuiMethods;
import gui.app.common.RequestType;
import gui.app.component.WindowsBar;
import gui.app.theme.UiBuilder;
import gui.app.theme.Fonts;
import gui.app.theme.Icons;
import gui.app.theme.Sizes.Size;

public class GetUserBugsDialog extends UiBuilder
{
	private TextField emailField = new TextField();
	private VBox vbox = new VBox();	

	public GetUserBugsDialog()
	{
		Stage stage = new Stage();
		
		Label title = createTitle("User Bugs", Fonts.FONT_SIZE_LARGE);

		emailField = createTextField("username", Size.LARGE);
		emailField.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				stage.close();
				execute();
			}
		});

		Button getBugsButton = createButton("Get Bugs", Size.SMALL, ButtonType.PRIMARY);
		Button myBugsButton = createButton("Get My Bugs", Size.SMALL, ButtonType.PRIMARY);

		getBugsButton.setOnAction(e -> 
		{
			stage.close();
			execute();
		});
		
		myBugsButton.setOnAction(e ->
		{
			GuiConstants.REQUEST_TYPE = RequestType.CURRENT_USER;
			GuiConstants.CURRENT_LIST_FILE = null;
			
			try
			{
				GuiMethods.requestRefreshOfCurrentUserBugs();
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
	
	private void execute()
	{
		if (emailField.getText().isEmpty())
		{
			MessageBox.showDialog(Errors.MISSING_FIELD);
			return;
		}
		
		GuiConstants.CURRENT_LIST_FILE = null;
		GuiConstants.REQUEST_TYPE = RequestType.USER;
				
		String email = emailField.getText();
		String response;
		try
		{
			GuiMethods.clearTable();

			response = ApiRequestor.request(Endpoints.BUGS_EMAIL(email));
			
			if (MessageBox.showErrorIfResponseNot200(response))
			{
				return;
			}
			
			GuiMethods.updateBugsInTable(response);
		} 
		catch (Exception e)
		{
			MessageBox.showExceptionDialog(Errors.REQUEST, e);
			return;
		}
	}
}