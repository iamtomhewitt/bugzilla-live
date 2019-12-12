package common.utilities;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import common.bug.Bug;
import gui.app.common.GuiConstants;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * A class of useful methods that cannot be incorporated into a micro service.
 * 
 * @author Tom Hewitt
 * @since 2.2.1
 */
public class Utilities
{
	/**
	 * Opens a specific url in Firefox.
	 * @throws URISyntaxException 
	 */
	public static void openUrlInBrowser(String url) throws IOException, URISyntaxException
	{
        Desktop.getDesktop().browse(new URI(url));
	}

	/**
	 * Opens a bug in Firefox.
	 * @throws URISyntaxException 
	 */
	public static void openBugInBrowser( String number) throws IOException, URISyntaxException
	{
		Desktop.getDesktop().browse(new URI(GuiConstants.BUGZILLA_URL + "/show_bug.cgi?id=" + number));
	}

	/**
	 * Copies the bug table to the clipboard.
	 */
	public static void copy(TableView<Bug> table)
	{
		ObservableList<Integer> rows = table.getSelectionModel().getSelectedIndices();
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		String finalString = "";
		for (int row : rows)
		{
			Bug bug = (Bug) table.getItems().get(row);
			if (table.getSelectionModel().isSelected(row))
			{
				finalString += bug.toString() + "\n\n";
			}
		}
		content.putString(finalString);
		clipboard.setContent(content);
	}
	
	/**
	 * Copies a string to the clipboard.
	 */
	public static void copy(String stringToCopy)
	{
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(stringToCopy);
		clipboard.setContent(content);
	}
}
