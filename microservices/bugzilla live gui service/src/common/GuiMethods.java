package common;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.json.simple.JSONObject;
import bugzilla.common.Errors;
import bugzilla.common.Fonts;
import bugzilla.common.MessageBox;
import bugzilla.common.OR.OR;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.message.OR.ORsRequest;
import bugzilla.message.OR.UserORsRequest;
import bugzilla.utilities.JacksonAdapter;
import component.InformationPane;
import component.ORComparator;
import component.ORTable;
import component.WindowsBar;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.BugzillaLive;
import message.GuiMessageSender;
import theme.Colours;
import theme.RowColours;

/**
 * A collection of static methods to be called from anywhere in the application.
 *
 * @author Tom Hewitt
 * @since 2.0.0
 */
@SuppressWarnings("unchecked")
public class GuiMethods
{	
	public static void updateApplicationTitle(String username)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				String title = "Bugzilla LIVE | " + createDisplayName(username);
				BugzillaLive.getMainStage().setTitle(title);
				WindowsBar.updateMainToolBarLabel(title);
			}
		});
	}
	
	/**
	 * Determines which type of refresh request is needed, and then makes the request.
	 */
	public static void requestORRefresh()
	{						
		switch(GuiConstants.REQUEST_TYPE)
		{
			case CURRENT_USER:
				requestRefreshOfCurrentUserORs();
				break;
				
			case LIST:
				requestRefreshOfORsInList();
				break;

			case USER:
				requestRefreshOfORsInTable();
				break;

			case SUBSYSTEM:
				requestRefreshOfORsInTable();
				break;

			default:
				MessageBox.showDialog(Errors.GENERAL + "\nIncorrect REQUEST_TYPE: " + GuiConstants.REQUEST_TYPE);
				break;
		}
	}
	
	/**
	 * Requests ORs for the user currently logged in.
	 */
	public static void requestRefreshOfCurrentUserORs()
	{
		GuiConstants.REQUEST_TYPE = RequestType.CURRENT_USER;
		GuiConstants.CURRENT_LIST_FILE = null;
		
		try
		{
			UserORsRequest request = new UserORsRequest(GuiConstants.USERNAME, GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
			new GuiMessageSender().sendRequestMessage(request);
		}
		catch (JsonTransformationException | MessageSenderException e)
		{
			MessageBox.showExceptionDialog(Errors.GENERAL, e);
		}
	}	
	

	/**
	 * Sends a request to the OR Details Service to refresh the ORs contained in the active list. Call this method when adding or removing an OR from a list, or when switching lists.
	 */
	public static void requestRefreshOfORsInList()
	{
		try
		{
			// Sleep first to allow previous file processing to complete
			Thread.sleep(500);

			// Get the current OR numbers in the file
			String content = new String(Files.readAllBytes(Paths.get(GuiConstants.CURRENT_LIST_FILE.getAbsolutePath())));
			List<String> numbers = new ArrayList<String>(Arrays.asList(content.split(",")));
			
			GuiConstants.REQUEST_TYPE = RequestType.LIST;

			if (!numbers.isEmpty())
			{
				ORsRequest request = new ORsRequest(numbers, GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
				new GuiMessageSender().sendRequestMessage(request);
			}
		}
		catch (Exception e)
		{
			MessageBox.showExceptionDialog(Errors.REFRESH, e);
		}
	}
	

	/**
	 * Sends a request to the OR Details Service to refresh the <b><i>current</i></b> set of ORs in the table.<p>
	 * NB: Since filtering introduced, now we must use the prefiltered ORs, otherwise the refresh request will just send a list of the ORs
	 * that have been filtered. 
	 */
	public static void requestRefreshOfORsInTable()
	{	
		try
		{
			List<String> numbers = new ArrayList<String>();
			List<OR> ors = JacksonAdapter.fromJson(GuiConstants.PREFILTERED_OR_DATA, OR.class);
			
			for (OR o : ors)
				numbers.add(o.getNumber());

			if (!numbers.isEmpty())
			{
				ORsRequest request = new ORsRequest(numbers, GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
				new GuiMessageSender().sendRequestMessage(request);
			}
		}
		catch (JsonTransformationException | MessageSenderException e)
		{
			MessageBox.showExceptionDialog(Errors.REFRESH, e);
		}
	}
	

	/**
	 * Called when an OR response message is received. Decodes the JSON returned, creates a list of OR objects and inserts it into the table.
	 */
	public static void updateORsInTable(JSONObject jsonObject)
	{		
		try
		{
			ObservableList<OR> ors = FXCollections.observableArrayList(JacksonAdapter.fromJson(jsonObject.get("ORs").toString(), OR.class));
			FXCollections.reverse(ors);
			
			GuiConstants.PREFILTERED_OR_DATA = JacksonAdapter.toJson(ors);
			ORTable.getInstance().getTableView().getItems().clear();
			ORTable.getInstance().getTableView().setItems(ors);
			ORTable.getInstance().getTableView().refresh();
		}
		catch (JsonTransformationException e1)
		{
			MessageBox.showExceptionDialog(Errors.GENERAL, e1);
		}
	}
	
	/**
	 * Sorts the OR table based on up to two parameters.
	 */
	public static void sortORs(boolean descending, String... sort)
	{
		ORComparator comparator = new ORComparator(sort);
		ObservableList<OR> listOfORs = ORTable.getInstance().getTableView().getItems();
		listOfORs.sort(comparator);

		if (descending)
			Collections.reverse(listOfORs);
	}
	
	/**
	 * Clears the OR table and inserts a 'Please Wait' placeholder.
	 */
	public static void clearTable()
	{
		Label l = new Label("Please wait...");
		l.setFont(Font.font(Fonts.FONT_SIZE_SUPER));
		ORTable.getInstance().getTableView().getItems().clear();
		ORTable.getInstance().getTableView().setPlaceholder(l);
	}
	
	/**
	 * Creates a display name from the current username. <p>
	 * E.g. 'thomas.hewitt' would return 'Thomas Hewitt'
	 */
	public static String createDisplayName(String username)
	{
		char firstNameFirstLetter = Character.toUpperCase(username.charAt(0));
		char lastNameFirstLetter  = Character.toUpperCase(username.split("\\.")[1].charAt(0));
		
		String firstName = firstNameFirstLetter + username.split("\\.")[0].substring(1);
		String lastName = lastNameFirstLetter + username.split("\\.")[1].substring(1);
		
		String name = firstName + " " + lastName;
		
		if (name.equals("Thomas Hewitt"))
			name = "Tom Hewitt";
		
		return name; 
	}
	
	/**
	 * Helper method for calling all the methods that update colours.
	 */
	public static void updateColours()
	{
		InformationPane.getInstance().updateBackgroundColour();
		InformationPane.getInstance().updateHeadingColour();
		InformationPane.getInstance().updateSubheadingColour();
		InformationPane.getInstance().getTitle().setTextFill(Color.web(Colours.WINDOW_TEXT));

		RowColours.updateColours();
		
		WindowsBar.updateMainToolBarColour();
		WindowsBar.updateMainToolBarLabelColour();		
	}
}