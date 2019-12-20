package gui.app.component.dialog.bug;

import gui.app.common.GuiConstants;
import gui.app.common.GuiMethods;
import gui.app.component.WindowsBar;

import gui.app.theme.UiBuilder;
import gui.app.theme.Icons;
import gui.app.theme.Sizes.Size;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import common.error.Errors;
import common.message.ApiRequestor;
import common.message.Endpoints;
import common.message.MessageBox;

/**
 * A dialog box used for creating a new list of bugs in the form of a .bugList file.
 * 
 * @author Tom Hewitt
 */
public class AddBugListDialog extends UiBuilder
{
	private Stage stage = new Stage();	

	public AddBugListDialog()
	{		
		TextField fileNameField = createTextField("filename", Size.LARGE);
		TextField bugField = createTextField("number(s)", Size.LARGE);
				
		Button createButton = createButton("Create", Size.SMALL, ButtonType.PRIMARY);
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
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(fileNameField, bugField, buttons);
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: white");
		
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
			
			String response = ApiRequestor.request(Endpoints.LIST_ADD(fileNameField.getText(), bugField.getText()));
			
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
