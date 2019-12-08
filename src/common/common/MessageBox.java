package common.common;

import java.io.PrintWriter;
import java.io.StringWriter;

import common.utilities.Icons;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Shows a message dialog in the GUI. <p>
 * Useful for showing exception messages to the user, or other alerts.
 *
 * @author Tom Hewitt
 * @since 1.0.0
 */
public class MessageBox
{
	public static void showExceptionDialog(String message, Exception e)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Exception");
				alert.setHeaderText("An Exception has occured.");
				alert.setContentText(message + "\n\n" + e.getMessage());

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				String exceptionText = sw.toString();

				Label label = new Label("Stacktrace: ");

				TextArea textArea = new TextArea(exceptionText);
				textArea.setEditable(false);
				textArea.setWrapText(true);
				textArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				GridPane.setVgrow(textArea, Priority.ALWAYS);
				GridPane.setHgrow(textArea, Priority.ALWAYS);

				GridPane expContent = new GridPane();
				expContent.setMaxWidth(Double.MAX_VALUE);
				expContent.add(label, 0, 0);
				expContent.add(textArea, 0, 1);
				
				Stage s = (Stage) alert.getDialogPane().getScene().getWindow();
				s.getIcons().add(Icons.createBugzillaIcon().getImage());
				
				alert.getDialogPane().setExpandableContent(expContent);
				alert.showAndWait();
			}
		});
	}

	public static void showDialog(String message)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText("Information");
		alert.setContentText(message);
		Stage s = (Stage) alert.getDialogPane().getScene().getWindow();
		s.getIcons().add(Icons.createBugzillaIcon().getImage());
		alert.showAndWait();
	}

	public static boolean showConfirmDialog(String message)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("");
		alert.setContentText(message);
		Stage s = (Stage) alert.getDialogPane().getScene().getWindow();
		s.getIcons().add(Icons.createBugzillaIcon().getImage());
		alert.showAndWait();
				
		return alert.getResult() == ButtonType.OK;
	}
}
