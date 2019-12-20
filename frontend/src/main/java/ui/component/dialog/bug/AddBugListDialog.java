package ui.component.dialog.bug;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.common.UiConstants;
import ui.common.UiMethods;
import ui.component.WindowsBar;
import ui.theme.Icons;
import ui.theme.UiBuilder;
import ui.theme.Sizes.Size;
import common.error.Errors;
import common.error.RequestException;
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
		createButton.setOnAction(e -> 
		{
			try
			{
				add(bugField, fileNameField);
			}
			catch (RequestException e1)
			{
				MessageBox.showExceptionDialog(Errors.REQUEST, e1);
			}
		});
		
		HBox buttons = new HBox(createButton);
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);
		
		fileNameField.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				try
				{
					add(bugField, fileNameField);
				}
				catch (RequestException e1)
				{
					MessageBox.showExceptionDialog(Errors.REQUEST, e1);
				}
			}
		});
		
		bugField.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				try
				{
					add(bugField, fileNameField);
				}
				catch (RequestException e1)
				{
					MessageBox.showExceptionDialog(Errors.REQUEST, e1);
				}
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
	
	private void add(TextField bugField, TextField fileNameField) throws RequestException
	{
		if (!bugField.getText().matches(UiConstants.BUG_REGEX))
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

		UiMethods.clearTable();
		UiConstants.CURRENT_LIST_FILE = fileNameField.getText();
		UiMethods.requestRefreshOfBugsInList();

		stage.close();
	}
}
