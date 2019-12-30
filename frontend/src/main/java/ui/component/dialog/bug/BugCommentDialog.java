package ui.component.dialog.bug;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.json.JSONObject;

import common.bug.BugAttachment;
import common.bug.BugComment;
import common.error.Errors;
import common.error.JsonTransformationException;
import common.error.RequestException;
import common.message.ApiRequestor;
import common.message.ApiRequestor.ApiRequestType;
import common.message.Endpoints;
import common.message.MessageBox;
import common.utility.JacksonAdapter;
import common.utility.UiMethods;
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
import ui.component.WindowsBar;
import ui.component.dialog.AddCommentDialog;
import ui.log.UiLogger;
import ui.theme.Fonts;
import ui.theme.Icons;
import ui.theme.UiBuilder;
import ui.theme.Sizes.Size;

/**
 * Shows the comments as well as attachments (which come through as comments as well) for a given Bug number.
 * 
 * @author Tom Hewitt
 */
@SuppressWarnings("unchecked")
public class BugCommentDialog extends UiBuilder
{
	private Stage stage = new Stage();
	private VBox vbox = new VBox();
	private ScrollPane scrollPane = new ScrollPane();

	private final int WINDOW_WIDTH = 475;
	private final int COMMENT_WIDTH = 418;
	private final int ATTACHMENT_WIDTH = 418;

	public BugCommentDialog(String number) throws Exception
	{
		StackPane stackpane = new StackPane();
		stackpane.getChildren().add(scrollPane);
		stackpane.setStyle("-fx-background-color: white; -fx-border-color: white; -fx-border-size: 5");

		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, stackpane, "Bug " + number + " Comments"), WINDOW_WIDTH, 900);
		stage.setResizable(false);
		stage.setTitle("Bug " + number + " Comments");
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
		stage.getIcons().add(new Icons().createCommentIcon().getImage());

		Button addCommentButton = createButton("Add Comment", Size.MEDIUM, ButtonType.PRIMARY);
		addCommentButton.setOnAction(e -> new AddCommentDialog(number, stage.getX() + WINDOW_WIDTH, stage));

		VBox attachments = populateAttachments(number);
		VBox comments = populateComments(number);
		
		vbox.getChildren().addAll(attachments, comments, addCommentButton);
		vbox.setStyle("-fx-background-color: white");
		vbox.setAlignment(Pos.CENTER);
		
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setPadding(new Insets(0, 0, 10, 0));
		scrollPane.setStyle("-fx-background-color: white");
		scrollPane.setContent(vbox);
	}

	private VBox populateAttachments(String number) throws JsonTransformationException, RequestException
	{
		VBox attachmentsVbox = new VBox();

		JSONObject response = ApiRequestor.request(ApiRequestType.GET, Endpoints.BUGS_ATTACHMENTS(number));
		
		if (MessageBox.showErrorIfResponseNot200(response))
		{
			return attachmentsVbox;
		}

		String bug = response.getJSONObject("bugs").getJSONArray(number).toString();
		List<BugAttachment> attachments = JacksonAdapter.fromJson(bug, BugAttachment.class);

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

		attachmentsVbox.setPadding(new Insets(10, 25, 10, 25));
		attachmentsVbox.setSpacing(5);
		attachmentsVbox.setStyle("-fx-background-color: white");
		return attachmentsVbox;
	}

	private VBox populateComments(String number) throws JsonTransformationException, RequestException
	{
		VBox commentsVbox = new VBox();
		JSONObject response = ApiRequestor.request(ApiRequestType.GET, Endpoints.BUGS_COMMENTS(number));

		if (MessageBox.showErrorIfResponseNot200(response))
		{
			return commentsVbox;
		}

		String bug = response.getJSONObject("bugs").getJSONObject(number).getJSONArray("comments").toString();
		List<BugComment> comments = JacksonAdapter.fromJson(bug, BugComment.class);

		for (BugComment comment : comments)
		{
			comment.setText(comment.getText());

			GridPane commentSection = new GridPane();

			Label name = createNameLabel(comment.getCreator());
			name.setFont(Font.font("System", FontWeight.BOLD, Fonts.FONT_SIZE_LARGE));
			name.setAlignment(Pos.CENTER_LEFT);

			Label date = createDateLabel(comment.getTime());
			date.setFont(Font.font("System", FontWeight.NORMAL, Fonts.FONT_SIZE_NORMAL));
			date.setAlignment(Pos.CENTER_LEFT);

			TextArea textArea = new TextArea();
			textArea.setText(comment.getText());
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
		return commentsVbox;
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
			UiLogger.getInstance().print("Couldn't parse date for string '" + time + "'");
			UiLogger.getInstance().printStackTrace(e);
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
		{
			dateString = "Unknown";
		}

		return new Label(dateString);
	}

	private Label createNameLabel(String emailAddress)
	{
		String username = UiMethods.createDisplayName(emailAddress);
		return new Label(username);
	}
}