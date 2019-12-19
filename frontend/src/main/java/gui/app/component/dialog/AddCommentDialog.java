package gui.app.component.dialog;

import gui.app.component.WindowsBar;
import gui.app.theme.Fonts;
import gui.app.theme.UiBuilder;
import gui.app.theme.Icons;
import gui.app.theme.Sizes;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import common.message.MessageBox;

public class AddCommentDialog extends VBox 
{
	private Stage stage = new Stage();

	public AddCommentDialog(String number, double xPosition, Stage parentStage)
	{		
		TextArea comment = new TextArea("Enter your comment.");
		comment.setMinHeight(300);
		comment.setFont(Font.font(Fonts.FONT, FontPosture.REGULAR, Fonts.FONT_SIZE_NORMAL));
		comment.setWrapText(true);

		Button submitButton = new Button("Submit");
		submitButton.setOnAction(e ->
		{
			if (comment.getText().isEmpty())
			{
				MessageBox.showDialog("Comment cannot be empty!");
				return;
			}
			if (MessageBox.showConfirmDialog("Add comment? This cannot be undone."))
			{
				// TODO use ApiRequestor
				stage.close();
				parentStage.close();
			}
		});

		this.getChildren().addAll(comment, submitButton);
		this.setPadding(new Insets(10));
		this.setSpacing(15);
		this.setAlignment(Pos.CENTER);

		UiBuilder.stylePrimaryButton(submitButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);

		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, this, "Bug" + number + " Add Comment"), 375, 475);
		stage.setTitle("Add New Comment");
		stage.getIcons().add(new Icons().createAddIcon().getImage());
		stage.setX(xPosition);
		stage.setScene(scene);
		stage.show();
	}
}