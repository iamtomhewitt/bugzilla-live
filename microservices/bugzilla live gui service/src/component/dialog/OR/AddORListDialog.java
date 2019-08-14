package component.dialog.OR;

import java.io.File;

import bugzilla.common.Errors;
import bugzilla.common.Folders;
import bugzilla.common.MessageBox;
import bugzilla.message.list.CreateListRequest;
import bugzilla.utilities.Icons;
import component.WindowsBar;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import common.GuiConstants;
import common.GuiMethods;
import message.GuiMessageSender;
import theme.GuiStyler;
import theme.Sizes;

/**
 * A dialog box used for creating a new list of ORs in the form of a .txt file.
 * 
 * @author Tom Hewitt
 */
public class AddORListDialog 
{
	private Stage stage = new Stage();	
	private VBox vbox = new VBox();

	public AddORListDialog()
	{		
		TextField fileNameField = new TextField();
		TextField orField = new TextField();
		
		fileNameField.setPromptText("filename");
		orField.setPromptText("OR number");
		
		Button createButton = new Button("Create");
		createButton.setOnAction(e -> add(orField, fileNameField));
		
		HBox buttons = new HBox(createButton);
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);
		
		fileNameField.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
				add(orField, fileNameField);
		});
		orField.setOnKeyPressed(e->
		{
			if (e.getCode() == KeyCode.ENTER)
				add(orField, fileNameField);
		});
		
		vbox.getChildren().addAll(fileNameField, orField, buttons);
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: white");
		
		GuiStyler.stylePrimaryButton(createButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiStyler.styleTextField(orField, Sizes.INPUT_WIDTH_LARGE, Sizes.INPUT_HEIGHT_SMALL);
		GuiStyler.styleTextField(fileNameField, Sizes.INPUT_WIDTH_LARGE, Sizes.INPUT_HEIGHT_SMALL);
		
		Platform.runLater(() -> createButton.requestFocus());
		
		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, vbox, "Add OR List"), 275, 175);	
		stage.getIcons().add(Icons.createListIcon().getImage());
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
	}
	
	private void add(TextField orField, TextField fileNameField)
	{
		try
		{
			if (!orField.getText().matches(GuiConstants.OR_REGEX))
			{
				MessageBox.showDialog("OR '"+orField.getText()+"'" + Errors.INVALID_OR);
				return;
			}
			if (orField.getText().isEmpty() || fileNameField.getText().isEmpty())
			{
				MessageBox.showDialog(Errors.MISSING_FIELD);
				return;
			}
			
			CreateListRequest request = new CreateListRequest(Folders.LISTS_FOLDER + fileNameField.getText() + ".txt", orField.getText());
			new GuiMessageSender().sendRequestMessage(request);
			
			GuiMethods.clearTable();
			
			Thread.sleep(100);
			GuiConstants.CURRENT_LIST_FILE = new File(Folders.LISTS_FOLDER + fileNameField.getText() + ".txt");
			
			GuiMethods.requestRefreshOfORsInList();

			stage.close();
		}
		catch (Exception e)
		{
			MessageBox.showExceptionDialog(Errors.CREATE_LIST, e);
		}
	}
}
