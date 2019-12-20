package ui.app.component.dialog.bug;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.app.common.UiConstants;
import ui.app.common.UiMethods;
import ui.app.common.RequestType;
import ui.app.component.WindowsBar;
import ui.app.theme.Icons;
import ui.app.theme.UiBuilder;
import ui.app.theme.Sizes.Size;
import common.error.Errors;
import common.error.RequestException;
import common.message.ApiRequestor;
import common.message.Endpoints;
import common.message.MessageBox;

/**
 * A simple dialog box that takes in an bug number and adds it to the current list of bugs that the UI is displaying.
 * 
 * @author Tom Hewitt
 */
public class AddBugDialog extends UiBuilder
{		
	private Stage stage = new Stage();
	private HBox buttons = new HBox();
	
	public AddBugDialog()
	{
		TextField input = createTextField("number", Size.LARGE);
		input.setTooltip(new Tooltip("Enter more than 1 bug by separating with a comma, for example '23001,23002'"));
		input.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				try
				{
					add(input);
				}
				catch (RequestException e1)
				{
					MessageBox.showExceptionDialog(Errors.REQUEST, e1);
				}
			}
		});
		
		Button addButton = createButton("Add", Size.SMALL, ButtonType.PRIMARY);
		addButton.setOnAction(e -> 
		{
			try
			{
				add(input);
			}
			catch (RequestException e1)
			{
				MessageBox.showExceptionDialog(Errors.REQUEST, e1);
			}
		});
				
		buttons.getChildren().addAll(addButton);
		buttons.setSpacing(10);
		buttons.setAlignment(Pos.CENTER);
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(input, buttons);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		vbox.setPadding(new Insets(10));
		vbox.setStyle("-fx-background-color: white");

		Platform.runLater(() -> addButton.requestFocus());
        
		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, vbox, "Add Bug"), 275, 125);
		stage.getIcons().add(new Icons().createAddIcon().getImage());
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
	}
	
	private void add(TextField input) throws RequestException
	{
		String filename = UiConstants.CURRENT_LIST_FILE.split("\\.")[0];
		String number = input.getText();

		if (!number.matches(UiConstants.BUG_REGEX))
		{
			MessageBox.showDialog("Bug '" + number + "'" + Errors.INVALID_BUG);
			return;
		}
		
		if (UiConstants.REQUEST_TYPE.equals(RequestType.USER))
		{
			MessageBox.showDialog("Cannot add a bug as a list is not being used.");
			return;
		}
		
		if (!number.isEmpty())
		{
			String response = ApiRequestor.request(Endpoints.LIST_MODIFY(filename, number, ""));

			MessageBox.showErrorIfResponseNot200(response);

			// Now refresh the list to pick up the new bug
			UiMethods.requestRefreshOfBugsInList();
			stage.close();
		}
	}	
}