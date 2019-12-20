package gui.app.component.dialog.bug;

import gui.app.common.GuiConstants;
import gui.app.common.GuiMethods;
import gui.app.common.RequestType;
import gui.app.component.WindowsBar;
import gui.app.theme.UiBuilder;
import gui.app.theme.Sizes.Size;
import gui.app.theme.Icons;
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
import common.error.Errors;
import common.message.ApiRequestor;
import common.message.Endpoints;
import common.message.MessageBox;

/**
 * A simple dialog box that takes in an bug number and adds it to the current list of bugs that the GUI is displaying.
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
				add(input);
			}
		});
		
		Button addButton = createButton("Add", Size.SMALL, ButtonType.PRIMARY);
		addButton.setOnAction(e -> add(input));
				
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
	
	private void add(TextField input)
	{
		try
		{
			String filename = GuiConstants.CURRENT_LIST_FILE.split("\\.")[0];
			String number = input.getText();
			
			if (!number.matches(GuiConstants.BUG_REGEX))
			{
				MessageBox.showDialog("Bug '" + number + "'" + Errors.INVALID_BUG);
				return;
			}
			if (GuiConstants.REQUEST_TYPE.equals(RequestType.USER))
			{
				MessageBox.showDialog("Cannot add a bug as a list is not being used.");
				return;
			}
			if (!number.isEmpty())
			{
				String response = ApiRequestor.request(Endpoints.LIST_MODIFY(filename, number, ""));
				
				MessageBox.showErrorIfResponseNot200(response);
				
				// Now refresh the list to pick up the new bug
				GuiMethods.requestRefreshOfBugsInList();
				stage.close();
			}
		}
		catch (Exception ex)
		{
			MessageBox.showExceptionDialog(Errors.GENERAL, ex);
		}
	}	
}