package component.dialog.bug;

import bugzilla.common.Errors;
import bugzilla.common.MessageBox;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.message.bug.UserBugsRequest;
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
import common.GuiConstants;
import common.GuiMethods;
import common.RequestType;
import message.GuiMessageSender;
import theme.GuiStyler;
import theme.Sizes;

public class GetUserBugsDialog
{
	private Stage stage 				= new Stage();	
	private TextField firstNameField 	= new TextField();
	private TextField lastNameField  	= new TextField();	
	private VBox vbox 					= new VBox();	
	private VBox fields 				= new VBox();
	private HBox buttons 				= new HBox();
	private Button getBugsButton		= new Button();

	public GetUserBugsDialog(Stage s)
	{
		this.stage = s;
		
		Label title = new Label("User Bugs");

		firstNameField.setPromptText("first name");
		firstNameField.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
				execute();
		});
		
		lastNameField.setPromptText("last name");
		lastNameField.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
				execute();
		});

		getBugsButton = new Button("Get User Bugs");
		getBugsButton.setOnAction(e -> execute());
		
		Button myBugsButton = new Button("Get My Bugs");
		myBugsButton.setOnAction(e ->
		{
			GuiConstants.REQUEST_TYPE = RequestType.CURRENT_USER;
			GuiConstants.CURRENT_LIST_FILE = null;
			
			try
			{
				UserBugsRequest request = new UserBugsRequest(GuiConstants.USERNAME, GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
				new GuiMessageSender().sendRequestMessage(request);
			}
			catch (JsonTransformationException | MessageSenderException e1)
			{
				MessageBox.showExceptionDialog(Errors.GENERAL, e1);
			}
			GuiMethods.clearTable();
			stage.close();
		});
		
		GuiStyler.stylePrimaryButton(getBugsButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiStyler.stylePrimaryButton(myBugsButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiStyler.styleTextField(firstNameField, Sizes.INPUT_WIDTH_LARGE, 30);
		GuiStyler.styleTextField(lastNameField, Sizes.INPUT_WIDTH_LARGE, 30);
		GuiStyler.styleTitle(title);
		
		fields.getChildren().addAll(firstNameField, lastNameField);
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
		if (firstNameField.getText().equals("") || lastNameField.getText().equals(""))
		{
			MessageBox.showDialog(Errors.MISSING_FIELD);
			return;
		}
		
		GuiConstants.CURRENT_LIST_FILE = null;
		GuiConstants.REQUEST_TYPE = RequestType.USER;
		
		String username = firstNameField.getText() + "." + lastNameField.getText();
		
		try
		{
			UserBugsRequest request = new UserBugsRequest(username, GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
			new GuiMessageSender().sendRequestMessage(request);
		}
		catch (JsonTransformationException | MessageSenderException e1)
		{
			MessageBox.showExceptionDialog(Errors.GENERAL, e1);
		}
		
		GuiMethods.clearTable();
		
		stage.close();
	}

	public VBox getVbox()
	{
		return vbox;
	}
}
