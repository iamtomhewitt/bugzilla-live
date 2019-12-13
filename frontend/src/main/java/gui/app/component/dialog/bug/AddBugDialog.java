package gui.app.component.dialog.bug;

import gui.app.common.GuiConstants;
import gui.app.common.GuiMethods;
import gui.app.common.RequestType;
import gui.app.component.WindowsBar;
import gui.app.theme.GuiStyler;
import gui.app.theme.Sizes;
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

import common.Errors;
import common.MessageBox;
import common.message.ApiRequestor;
import common.utilities.Icons;

/**
 * A simple dialog box that takes in an bug number and adds it to the current list of bugs that the GUI is displaying.
 * 
 * @author Tom Hewitt
 */
public class AddBugDialog
{		
	private Stage stage = new Stage();
	private VBox hbox = new VBox();
	private HBox buttons = new HBox();
	
	public AddBugDialog()
	{
		TextField input = new TextField();
		input.setPromptText("number");
		input.setTooltip(new Tooltip("Enter more than 1 bug by separating with a comma, for example '23001,23002'"));
		input.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
				add(input);
		});
		
		Button addButton = new Button("Add");
		addButton.setOnAction(e -> add(input));
		
		GuiStyler.styleTextField(input, Sizes.INPUT_WIDTH_LARGE, 30);
		GuiStyler.stylePrimaryButton(addButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		
		buttons.getChildren().addAll(addButton);
		buttons.setSpacing(10);
		buttons.setAlignment(Pos.CENTER);
		
		hbox.getChildren().addAll(input, buttons);
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(10);
		hbox.setPadding(new Insets(10));
		hbox.setStyle("-fx-background-color: white");

		Platform.runLater(() -> addButton.requestFocus());
        
		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, hbox, "Add Bug"), 275, 125);
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
				String url = String.format("/list/modify?name=%s&add=%s", filename, number);
				String response = ApiRequestor.request(url);
				
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