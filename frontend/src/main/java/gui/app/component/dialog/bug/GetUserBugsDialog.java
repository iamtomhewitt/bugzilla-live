package gui.app.component.dialog.bug;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import gui.app.theme.GuiBuilder;
import gui.app.theme.Sizes;

public class GetUserBugsDialog
{
	private Stage stage 				= new Stage();	
	private TextField usernameField 	= new TextField();
	private VBox vbox 					= new VBox();	
	private VBox fields 				= new VBox();
	private HBox buttons 				= new HBox();
	private Button getBugsButton		= new Button();

	public GetUserBugsDialog(Stage s)
	{
		this.stage = s;
		
		Label title = new Label("User Bugs");

		usernameField.setPromptText("username");
		usernameField.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
				execute();
		});

		getBugsButton = new Button("Get Bugs");
		getBugsButton.setOnAction(e -> execute());
		
		Button myBugsButton = new Button("Get My Bugs");
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
		
		GuiBuilder.stylePrimaryButton(getBugsButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiBuilder.stylePrimaryButton(myBugsButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiBuilder.styleTextField(usernameField, Sizes.INPUT_WIDTH_LARGE, 30);
		GuiBuilder.styleTitle(title);
		
		fields.getChildren().addAll(usernameField);
		fields.setAlignment(Pos.CENTER);
		fields.setSpacing(10);
		fields.setPadding(new Insets(10));
		
		buttons.getChildren().addAll(getBugsButton, myBugsButton);
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);

		vbox = new VBox(title, fields, buttons);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		
		Platform.runLater(() -> getBugsButton.requestFocus());
	}
	
	private void execute()
	{
		if (usernameField.getText().isEmpty())
		{
			MessageBox.showDialog(Errors.MISSING_FIELD);
			return;
		}
		
		GuiConstants.CURRENT_LIST_FILE = null;
		GuiConstants.REQUEST_TYPE = RequestType.USER;
				
		// TODO get actual email
		String email = "leif@ogre.com";
		String response;
		try
		{
			response = ApiRequestor.request(Endpoints.BUGS_EMAIL(email));
		} 
		catch (Exception e)
		{
			MessageBox.showExceptionDialog(Errors.REQUEST, e);
			return;
		}
					
		MessageBox.showErrorIfResponseNot200(response);
		
		GuiMethods.clearTable();
		
		stage.close();
	}

	public VBox getVbox()
	{
		return vbox;
	}
}