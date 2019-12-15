package gui.app.common;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import common.Errors;
import common.Fonts;
import common.bug.Bug;
import common.exception.JsonTransformationException;
import common.message.ApiRequestor;
import common.message.MessageBox;
import common.utilities.JacksonAdapter;
import gui.app.component.InformationPane;
import gui.app.component.BugComparator;
import gui.app.component.BugTable;
import gui.app.component.WindowsBar;
import gui.app.main.BugzillaLive;

import gui.app.theme.Colours;
import gui.app.theme.RowColours;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
		
		// TODO get users email
		String email = "leif@ogre.com";
		String url = String.format("/bugs/email?email=%s", email);
		String response = ApiRequestor.request(url);
		
		if(MessageBox.showErrorIfResponseNot200(response))
		{
			return;
		}
		
		clearTable();
		updateBugsInTable(response);
	}	
	
	/**
	 * Sends a request to the backend to refresh the bugs contained in the active list. Call this method when adding or removing a bug from a list, or when switching lists.
	 */
	public static void requestRefreshOfBugsInList()
	{
		// Get the current bug numbers in the file
		String url = String.format("/list/%s/contents", GuiConstants.CURRENT_LIST_FILE.split("\\.")[0]);
		String response = ApiRequestor.request(url);
		String content = new JSONObject(response).getString("contents");
		
		GuiConstants.REQUEST_TYPE = RequestType.LIST;
		
		if(MessageBox.showErrorIfResponseNot200(response))
		{
			return;
		}

		if (!content.isEmpty())
		{
			url = String.format("/bugs/numbers?numbers=%s", content);
			response = ApiRequestor.request(url);
			updateBugsInTable(response);
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
			String numbers = "";
			List<Bug> bugs = JacksonAdapter.fromJson(GuiConstants.PREFILTERED_BUG_DATA, Bug.class);
			
			for (Bug bug : bugs)
			{
				numbers += bug.getId() + ",";
			}

			if (!numbers.isEmpty())
			{
				String url = String.format("/bugs/numbers?numbers=%s", numbers);
				String response = ApiRequestor.request(url);
				
				if(MessageBox.showErrorIfResponseNot200(response))
				{
					return;
				}
				
				updateBugsInTable(response);
			}
		}
		catch (JsonTransformationException e)
		{
			MessageBox.showExceptionDialog(Errors.REFRESH, e);
		}
	}

	/**
	 * Called when an bug response message is received. Decodes the JSON returned, creates a list of bug objects and inserts it into the table.
	 */
	public static void updateBugsInTable(String response)
	{		
		try
		{
			JSONObject jsonObject = new JSONObject(response);
			ObservableList<Bug> bugs = FXCollections.observableArrayList(JacksonAdapter.fromJson(jsonObject.get("bugs").toString(), Bug.class));
			FXCollections.reverse(bugs);
			
			GuiConstants.PREFILTERED_BUG_DATA = JacksonAdapter.toJson(bugs);
			BugTable.getInstance().getTableView().getItems().clear();
			BugTable.getInstance().getTableView().setItems(bugs);
			BugTable.getInstance().getTableView().refresh();
			InformationPane.getInstance().updateTexts();
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