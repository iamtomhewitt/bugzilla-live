package gui.app.component.dialog.bug;

import java.net.URLEncoder;

import gui.app.common.GuiConstants;
import gui.app.common.GuiMethods;
import gui.app.component.WindowsBar;

import gui.app.theme.GuiStyler;
import gui.app.theme.Icons;
import gui.app.theme.Sizes;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import common.exception.Errors;
import common.message.ApiRequestor;
import common.message.MessageBox;

/**
 * A dialog box used for creating a new list of bugs in the form of a .bugList file.
 * 
 * @author Tom Hewitt
 */
public class AddBugListDialog 
{
	private Stage stage = new Stage();	
	private VBox vbox = new VBox();

	public AddBugListDialog()
	{		
		TextField fileNameField = new TextField();
		TextField bugField = new TextField();
		
		fileNameField.setPromptText("filename");
		bugField.setPromptText("Bug number");
		
		Button createButton = new Button("Create");
		createButton.setOnAction(e -> add(bugField, fileNameField));
		
		HBox buttons = new HBox(createButton);
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);
		
		fileNameField.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				add(bugField, fileNameField);
			}
		});
		
		bugField.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				add(bugField, fileNameField);
			}
		});
		
		vbox.getChildren().addAll(fileNameField, bugField, buttons);
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: white");
		
		GuiStyler.stylePrimaryButton(createButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiStyler.styleTextField(bugField, Sizes.INPUT_WIDTH_LARGE, Sizes.INPUT_HEIGHT_SMALL);
		GuiStyler.styleTextField(fileNameField, Sizes.INPUT_WIDTH_LARGE, Sizes.INPUT_HEIGHT_SMALL);
		
		Platform.runLater(() -> createButton.requestFocus());
		
		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, vbox, "Add Bug List"), 275, 175);	
		stage.getIcons().add(new Icons().createListIcon().getImage());
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
	}
	
	private void add(TextField bugField, TextField fileNameField)
	{
		try
		{
			if (!bugField.getText().matches(GuiConstants.BUG_REGEX))
			{
				MessageBox.showDialog("Bug '" + bugField.getText() + "'" + Errors.INVALID_BUG);
				return;
			}
			if (bugField.getText().isEmpty() || fileNameField.getText().isEmpty())
			{
				MessageBox.showDialog(Errors.MISSING_FIELD);
				return;
			}
			
			String url = String.format("/list/add?name=%s&contents=%s", URLEncoder.encode(fileNameField.getText(), "UTF-8"), URLEncoder.encode(bugField.getText(), "UTF-8"));
			String response = ApiRequestor.request(url);
			
			MessageBox.showErrorIfResponseNot200(response);
			
			GuiMethods.clearTable();
			GuiConstants.CURRENT_LIST_FILE = fileNameField.getText();
			GuiMethods.requestRefreshOfBugsInList();

			stage.close();
		}
		catch (Exception e)
		{
			MessageBox.showExceptionDialog(Errors.CREATE_LIST, e);
		}
	}
}
