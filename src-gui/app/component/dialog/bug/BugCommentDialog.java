package component.dialog.bug;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.json.simple.JSONObject;

import common.GuiMethods;
import common.common.Errors;
import common.common.Fonts;
import common.common.MessageBox;
import common.common.bug.BugAttachment;
import common.common.bug.BugComment;
import common.exception.JsonTransformationException;
import common.utilities.Icons;
import common.utilities.JacksonAdapter;
import component.WindowsBar;
import component.dialog.AddCommentDialog;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import log.GuiLogger;
import theme.GuiStyler;
import theme.Sizes;

/**
 * Shows the comments as well as attachments (which come through as comments as well) for a given Bug number.
 * 
 * @author Tom Hewitt
 */
@SuppressWarnings("unchecked")
public class BugCommentDialog extends GridPane 
{
	private Stage stage = new Stage();
	private VBox vbox = new VBox();
	private VBox attachmentsVbox = new VBox();
	private VBox commentsVbox = new VBox();
	private ScrollPane scrollPane = new ScrollPane();

	private final int WINDOW_WIDTH = 475;
	private final int COMMENT_WIDTH = 418;
	private final int ATTACHMENT_WIDTH = 418;
	
	private JSONObject message;

	public BugCommentDialog(JSONObject message) throws Exception
	{
		this.message = message;
		
		String number = message.get("number").toString();
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setPadding(new Insets(0, 0, 10, 0));
		scrollPane.setStyle("-fx-background-color: white");
		
		StackPane stackpane = new StackPane();
		stackpane.getChildren().add(scrollPane);
		stackpane.setStyle("-fx-background-color: white; -fx-border-color: white; -fx-border-size: 5");

		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, stackpane, "Bug" + number + " Comments"), WINDOW_WIDTH, 900);
		stage.setResizable(false);
		stage.setTitle("Bug" + number + " Comments");
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
		stage.getIcons().add(Icons.createCommentIcon().getImage());
		
		populateAttachments();
		populateComments();
		
		Button addCommentButton = new Button("Add Comment");
		addCommentButton.setOnAction(e -> new AddCommentDialog(number, stage.getX() + WINDOW_WIDTH, stage));
		GuiStyler.stylePrimaryButton(addCommentButton, Sizes.BUTTON_WIDTH_MEDIUM, Sizes.BUTTON_HEIGHT_SMALL);
		
		vbox.getChildren().add(addCommentButton);
		vbox.setStyle("-fx-background-color: white");
		vbox.setAlignment(Pos.CENTER);
		
		scrollPane.setContent(vbox);
	}
	
	private void populateAttachments() throws JsonTransformationException 
	{
		List<BugAttachment> attachments = JacksonAdapter.fromJson(message.get("attachments").toString(), BugAttachment.class);
		
		for (BugAttachment attachment : attachments)
		{			
			Hyperlink link = new Hyperlink();
			link.setText("Attachment " + attachment.getId() + " | " + attachment.getDescription() + " (" + attachment.getFilename() + ")");
			link.setFont(Font.font("System", FontWeight.BOLD, Fonts.FONT_SIZE_NORMAL));
			link.setWrapText(true);
			link.setMinWidth(ATTACHMENT_WIDTH);
			link.setMaxWidth(ATTACHMENT_WIDTH);
			link.setMinHeight(20);
			link.setStyle("-fx-border-color: #99D9EA; -fx-border-width: 1; -fx-background-color: #99D9EA");

			link.setOnAction(e -> 
			{
				try 
				{
					attachment.open();
				} 
				catch (IOException e1) 
				{
					MessageBox.showExceptionDialog(Errors.ATTACHMENT, e1);
				}
			});

			attachmentsVbox.getChildren().add(link);
			attachmentsVbox.setAlignment(Pos.CENTER);
		}

		vbox.getChildren().add(attachmentsVbox);

		attachmentsVbox.setPadding(new Insets(10, 25, 10, 25));
		attachmentsVbox.setSpacing(5);
		attachmentsVbox.setStyle("-fx-background-color: white");
	}

	private void populateComments() throws JsonTransformationException 
	{
		List<BugComment> comments = JacksonAdapter.fromJson(message.get("comments").toString(), BugComment.class);		

		for (BugComment comment : comments)
		{
			comment.setComment(comment.getComment());
			
			GridPane commentSection = new GridPane();

			Label name = createNameLabel(comment.getCommenter());
			name.setFont(Font.font("System", FontWeight.BOLD, Fonts.FONT_SIZE_LARGE));
			name.setAlignment(Pos.CENTER_LEFT);

			Label date = createDateLabel(comment.getTime());
			date.setFont(Font.font("System", FontWeight.NORMAL, Fonts.FONT_SIZE_NORMAL));
			date.setAlignment(Pos.CENTER_LEFT);

			TextArea textArea = new TextArea();
			textArea.setText(comment.getComment());
			textArea.setWrapText(true);
			textArea.setEditable(false);
			textArea.setPrefWidth(COMMENT_WIDTH);
			textArea.setMinHeight(24);
			textArea.setStyle("-fx-control-inner-background: #e0e0e2");
			textArea.setFont(Font.font("System", FontWeight.NORMAL, Fonts.FONT_SIZE_NORMAL));

			HBox nameAndDate = new HBox(name, date);
			nameAndDate.setAlignment(Pos.CENTER);
			nameAndDate.setSpacing(5);

			commentSection.add(nameAndDate, 0, 0);
			commentSection.add(textArea, 0, 1, 2, 1);
			commentSection.setAlignment(Pos.CENTER);

			commentsVbox.getChildren().add(commentSection);

			// Autosize height
			new AnimationTimer()
			{
				@Override
				public void handle(long now)
				{
					Node text = textArea.lookup(".text");
					if (text != null)
					{
						textArea.prefHeightProperty().bind(Bindings.createDoubleBinding(new Callable<Double>()
						{
							@Override
							public Double call() throws Exception
							{
								return text.getBoundsInLocal().getHeight();
							}
						}, text.boundsInLocalProperty()).add(20));
						this.stop();
					}
				}
			}.start();
		}

		commentsVbox.setPadding(new Insets(10, 25, 10, 25));
		commentsVbox.setSpacing(15);
		commentsVbox.setStyle("-fx-background-color: white");
		
		vbox.getChildren().add(commentsVbox);
	}

	private Label createDateLabel(String time)
	{
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		DecimalFormat decimalFormatter = new DecimalFormat("00");
		Calendar calendar = Calendar.getInstance();
		Date date;
		String dateString = "";

		try
		{
			date = dateFormatter.parse(time);
			calendar.setTime(date);
		}
		catch (ParseException e)
		{
			date = null;
			GuiLogger.getInstance().print("Couldn't parse date for string '" + time + "'");
			GuiLogger.getInstance().printStackTrace(e);
		}

		if (date != null)
		{
			dateString += decimalFormatter.format(calendar.get(Calendar.DAY_OF_MONTH)) + " ";
			dateString += DateFormatSymbols.getInstance().getMonths()[calendar.get(Calendar.MONTH)] + " ";
			dateString += decimalFormatter.format(calendar.get(Calendar.HOUR_OF_DAY)) + ":";
			dateString += decimalFormatter.format(calendar.get(Calendar.MINUTE)) + " ";
			dateString += calendar.get(Calendar.YEAR);
		}
		else
			dateString = "Unknown";

		return new Label(dateString);
	}

	private Label createNameLabel(String emailAddress)
	{
		String username = emailAddress.split("@")[0];
		String[] names = username.split("\\.");

		// A name might only have first name, for example "Git-SCM" so only format if the split has 2 parts
		if (names.length > 1)
			username = GuiMethods.createDisplayName(username);

		return new Label(username);
	}
}