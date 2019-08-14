package component.dialog;

import bugzilla.common.Errors;
import bugzilla.common.Fonts;
import bugzilla.common.MessageBox;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.message.OR.ORCommentRequest;
import bugzilla.utilities.Icons;
import component.WindowsBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import common.GuiConstants;
import common.ORTemplates;
import message.GuiMessageSender;
import theme.GuiStyler;
import theme.Sizes;

public class AddCommentDialog extends VBox 
{
	private Stage stage = new Stage();

	public AddCommentDialog(String number, double xPosition, Stage parentStage)
	{		
		TextArea comment = new TextArea("Enter your comment.");
		comment.setMinHeight(300);
		comment.setFont(Font.font(Fonts.FONT, FontPosture.REGULAR, Fonts.FONT_SIZE_NORMAL));
		comment.setWrapText(true);

		Label templateLabel = new Label("Template");

		ComboBox<String> templates = new ComboBox<String>();
		templates.getItems().addAll("Coded", "None");
		templates.setOnAction(e ->
		{
			String template = templates.getSelectionModel().getSelectedItem();
			
			switch (template)
			{
				case "Coded":
					comment.setText(ORTemplates.CODED);
					break;
					
				case "None":
					comment.setText(ORTemplates.NONE);
					break;
					
				default:
					comment.setText(ORTemplates.NONE);
					break;
			}
		});
		
		HBox templatesHbox = new HBox(templateLabel, templates);
		templatesHbox.setSpacing(10);
		templatesHbox.setAlignment(Pos.CENTER);

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
				try
				{
					ORCommentRequest request = new ORCommentRequest.Builder().withApiKey(GuiConstants.APIKEY)
																				.withComment(comment.getText())
																				.withORNumber(number)
																				.withPassword(GuiConstants.PASSWORD)
																				.withUsername(GuiConstants.USERNAME)
																				.build();
					new GuiMessageSender().sendRequestMessage(request);
				}
				catch (JsonTransformationException | MessageSenderException e1)
				{
					MessageBox.showExceptionDialog(Errors.GENERAL, e1);
				}
				stage.close();
				parentStage.close();
			}
		});

		this.getChildren().addAll(templatesHbox, comment, submitButton);
		this.setPadding(new Insets(10));
		this.setSpacing(15);
		this.setAlignment(Pos.CENTER);

		GuiStyler.stylePrimaryButton(submitButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiStyler.styleComboBox(templates);
		GuiStyler.styleTitle(templateLabel);

		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, this, "OR" + number + " Add Comment"), 375, 475);
		stage.setTitle("Add New Comment");
		stage.getIcons().add(Icons.createAddIcon().getImage());
		stage.setX(xPosition);
		stage.setScene(scene);
		stage.show();
	}
}