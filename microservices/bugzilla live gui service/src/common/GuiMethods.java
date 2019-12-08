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
import bugzilla.common.bug.Bug;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.message.bug.BugsRequest;
import bugzilla.message.bug.UserBugsRequest;
import bugzilla.utilities.JacksonAdapter;
import component.InformationPane;
import component.BugComparator;
import component.BugTable;
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
	public static void requestBugRefresh()
	{						
		switch(GuiConstants.REQUEST_TYPE)
		{
			case CURRENT_USER:
				requestRefreshOfCurrentUserBugs();
				break;
				
			case LIST:
				requestRefreshOfBugsInList();
				break;

			case USER:
				requestRefreshOfBugsInTable();
				break;

			default:
				MessageBox.showDialog(Errors.GENERAL + "\nIncorrect REQUEST_TYPE: " + GuiConstants.REQUEST_TYPE);
				break;
		}
	}
	
	/**
	 * Requests bugs for the user currently logged in.
	 */
	public static void requestRefreshOfCurrentUserBugs()
	{
		GuiConstants.REQUEST_TYPE = RequestType.CURRENT_USER;
		GuiConstants.CURRENT_LIST_FILE = null;
		
		try
		{
			UserBugsRequest request = new UserBugsRequest(GuiConstants.USERNAME, GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
			new GuiMessageSender().sendRequestMessage(request);
		}
		catch (JsonTransformationException | MessageSenderException e)
		{
			MessageBox.showExceptionDialog(Errors.GENERAL, e);
		}
	}	
	

	/**
	 * Sends a request to the Bug Details Service to refresh the Bugs contained in the active list. Call this method when adding or removing an Bug from a list, or when switching lists.
	 */
	public static void requestRefreshOfBugsInList()
	{
		try
		{
			// Sleep first to allow previous file processing to complete
			Thread.sleep(500);

			// Get the current bug numbers in the file
			String content = new String(Files.readAllBytes(Paths.get(GuiConstants.CURRENT_LIST_FILE.getAbsolutePath())));
			List<String> numbers = new ArrayList<String>(Arrays.asList(content.split(",")));
			
			GuiConstants.REQUEST_TYPE = RequestType.LIST;

			if (!numbers.isEmpty())
			{
				BugsRequest request = new BugsRequest(numbers, GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
				new GuiMessageSender().sendRequestMessage(request);
			}
		}
		catch (Exception e)
		{
			MessageBox.showExceptionDialog(Errors.REFRESH, e);
		}
	}
	

	/**
	 * Sends a request to the Bug Service to refresh the <b><i>current</i></b> set of Bugs in the table.<p>
	 * NB: Since filtering introduced, now we must use the prefiltered Bugs, otherwise the refresh request will just send a list of the Bugs
	 * that have been filtered. 
	 */
	public static void requestRefreshOfBugsInTable()
	{	
		try
		{
			List<String> numbers = new ArrayList<String>();
			List<Bug> bugs = JacksonAdapter.fromJson(GuiConstants.PREFILTERED_BUG_DATA, Bug.class);
			
			for (Bug bug : bugs)
				numbers.add(bug.getNumber());

			if (!numbers.isEmpty())
			{
				BugsRequest request = new BugsRequest(numbers, GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
				new GuiMessageSender().sendRequestMessage(request);
			}
		}
		catch (JsonTransformationException | MessageSenderException e)
		{
			MessageBox.showExceptionDialog(Errors.REFRESH, e);
		}
	}
	

	/**
	 * Called when an bug response message is received. Decodes the JSON returned, creates a list of bug objects and inserts it into the table.
	 */
	public static void updateBugsInTable(JSONObject jsonObject)
	{		
		try
		{
			ObservableList<Bug> bugs = FXCollections.observableArrayList(JacksonAdapter.fromJson(jsonObject.get("Bugs").toString(), Bug.class));
			FXCollections.reverse(bugs);
			
			GuiConstants.PREFILTERED_BUG_DATA = JacksonAdapter.toJson(bugs);
			BugTable.getInstance().getTableView().getItems().clear();
			BugTable.getInstance().getTableView().setItems(bugs);
			BugTable.getInstance().getTableView().refresh();
		}
		catch (JsonTransformationException e1)
		{
			MessageBox.showExceptionDialog(Errors.GENERAL, e1);
		}
	}
	
	/**
	 * Sorts the bug table based on up to two parameters.
	 */
	public static void sortBugs(boolean descending, String... sort)
	{
		BugComparator comparator = new BugComparator(sort);
		ObservableList<Bug> listOfBugs = BugTable.getInstance().getTableView().getItems();
		listOfBugs.sort(comparator);

		if (descending)
			Collections.reverse(listOfBugs);
	}
	
	/**
	 * Clears the bug table and inserts a 'Please Wait' placeholder.
	 */
	public static void clearTable()
	{
		Label l = new Label("Please wait...");
		l.setFont(Font.font(Fonts.FONT_SIZE_SUPER));
		BugTable.getInstance().getTableView().getItems().clear();
		BugTable.getInstance().getTableView().setPlaceholder(l);
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