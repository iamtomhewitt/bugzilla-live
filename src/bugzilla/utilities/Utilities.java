package bugzilla.utilities;

import java.io.IOException;

import bugzilla.common.OR.OR;

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
	 */
	public static void openUrlInFirefox(String url) throws IOException
	{
		Runtime.getRuntime().exec("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe " + url);
	}

	/**
	 * Opens an OR in Firefox.
	 */
	public static void openORInFirefox(String bugzillaUrl, String number) throws IOException
	{
		Runtime.getRuntime().exec("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe " + bugzillaUrl + "show_bug.cgi?id=" + number);
	}

	/**
	 * Copies the OR table to the clipboard.
	 */
	public static void copy(TableView<OR> table)
	{
		ObservableList<Integer> rows = table.getSelectionModel().getSelectedIndices();
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		String finalString = "";
		for (int row : rows)
		{
			OR or = (OR) table.getItems().get(row);
			if (table.getSelectionModel().isSelected(row))
			{
				finalString += or.toString() + "\n\n";
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
