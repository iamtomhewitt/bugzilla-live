package ui.component.dialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import ui.component.WindowsBar;
import ui.theme.Fonts;
import ui.theme.Icons;
import ui.theme.UiBuilder;
import ui.theme.Sizes.Size;

import org.json.JSONObject;

import common.error.Errors;
import common.error.RequestException;
import common.message.ApiRequestor;
import common.message.ApiRequestor.ApiRequestType;
import common.message.Endpoints;
import common.message.MessageBox;
import common.utility.UiConstants;

public class AddCommentDialog extends UiBuilder
{
	public AddCommentDialog(String number, double xPosition, Stage parentStage)
	{		
		Stage stage = new Stage();

		TextArea comment = new TextArea("Enter your comment.");
		comment.setMinHeight(300);
		comment.setFont(Font.font(Fonts.FONT, FontPosture.REGULAR, Fonts.FONT_SIZE_NORMAL));
		comment.setWrapText(true);

		Button submitButton = createButton("Submit", Size.SMALL, ButtonType.PRIMARY);
		submitButton.setOnAction(e ->
		{
			if (comment.getText().isEmpty())
			{
				MessageBox.showDialog("Comment cannot be empty!");
				return;
			}
			if (MessageBox.showConfirmDialog("Add comment? This cannot be undone."))
			{
				try 
				{
					String url = Endpoints.BUGS_ADD_COMMENTS(number, comment.getText(), UiConstants.APIKEY);
					JSONObject response = ApiRequestor.request(ApiRequestType.POST, url);
					MessageBox.showErrorIfResponseNot200(response);
				} 
				catch (RequestException e1) 
				{
					MessageBox.showExceptionDialog(Errors.REQUEST, e1);
				}
				stage.close();
				parentStage.close();
			}
		});

		VBox vbox = new VBox(comment, submitButton);
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(15);
		vbox.setAlignment(Pos.CENTER);

		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, vbox, "Bug" + number + " Add Comment"), 375, 475);
		stage.setTitle("Add New Comment");
		stage.getIcons().add(new Icons().createAddIcon().getImage());
		stage.setX(xPosition);
		stage.setScene(scene);
		stage.show();
	}
}