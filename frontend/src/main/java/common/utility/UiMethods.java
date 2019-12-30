package common.utility;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import common.bug.Bug;
import common.error.Errors;
import common.error.JsonTransformationException;
import common.error.RequestException;
import common.message.ApiRequestor;
import common.message.ApiRequestor.ApiRequestType;
import common.message.Endpoints;
import common.message.MessageBox;
import common.message.RequestType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import ui.component.BugComparator;
import ui.component.BugTable;
import ui.component.InformationPane;
import ui.theme.Colours;
import ui.theme.Fonts;
import ui.theme.RowColours;

/**
 * A collection of static methods to be called from anywhere in the application.
 *
 * @author Tom Hewitt
 * @since 2.0.0
 */
@SuppressWarnings("unchecked")
public class UiMethods
{	
	public static String createApplicationTitle(String email)
	{
		return "Bugzilla LIVE | " + createDisplayName(email);
	}
	
	/**
	 * Determines which type of refresh request is needed, and then makes the request.
	 */
	public static void requestBugRefresh() throws RequestException, JsonTransformationException
	{						
		switch(UiConstants.REQUEST_TYPE)
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
				MessageBox.showDialog(Errors.GENERAL + "\nIncorrect REQUEST_TYPE: " + UiConstants.REQUEST_TYPE);
				break;
		}
	}
	
	/**
	 * Requests bugs for the user currently logged in.
	 */
	public static void requestRefreshOfCurrentUserBugs() throws RequestException, JsonTransformationException
	{
		UiConstants.REQUEST_TYPE = RequestType.CURRENT_USER;
		UiConstants.CURRENT_LIST = null;
		
		JSONObject response = ApiRequestor.request(ApiRequestType.GET, Endpoints.BUGS_EMAIL(UiConstants.USER_EMAIL));
		
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
	public static void requestRefreshOfBugsInList() throws RequestException, JsonTransformationException
	{
		// Get the current bug numbers in the file
		JSONObject response = ApiRequestor.request(ApiRequestType.GET, Endpoints.LIST_CONTENTS(UiConstants.CURRENT_LIST.split("\\.")[0]));
		String content = response.getString("contents");

		UiConstants.REQUEST_TYPE = RequestType.LIST;

		if (MessageBox.showErrorIfResponseNot200(response))
		{
			return;
		}

		if (!content.isEmpty())
		{
			response = ApiRequestor.request(ApiRequestType.GET, Endpoints.BUGS_NUMBERS(content));
			updateBugsInTable(response);
		}
	}

	/**
	 * Sends a request to the Bug Service to refresh the <b><i>current</i></b> set of Bugs in the table.<p>
	 * NB: Since filtering introduced, now we must use the prefiltered Bugs, otherwise the refresh request will just send a list of the Bugs
	 * that have been filtered. 
	 */
	public static void requestRefreshOfBugsInTable() throws RequestException, JsonTransformationException
	{
		List<Bug> bugs = JacksonAdapter.fromJson(UiConstants.PREFILTERED_BUG_DATA, Bug.class);
		String numbers = bugs.stream().map(bug -> bug.getId()).collect(Collectors.joining(","));

		if (!numbers.isEmpty())
		{
			JSONObject response = ApiRequestor.request(ApiRequestType.GET, Endpoints.BUGS_NUMBERS(numbers));

			if (MessageBox.showErrorIfResponseNot200(response))
			{
				return;
			}

			updateBugsInTable(response);
		}
	}

	/**
	 * Called when an bug response message is received. Decodes the JSON returned, creates a list of bug objects and inserts it into the table.
	 */
	public static void updateBugsInTable(JSONObject response) throws JsonTransformationException
	{
		ObservableList<Bug> bugs = FXCollections.observableArrayList(JacksonAdapter.fromJson(response.get("bugs").toString(), Bug.class));
		FXCollections.reverse(bugs);

		UiConstants.PREFILTERED_BUG_DATA = JacksonAdapter.toJson(bugs);
		BugTable.getInstance().getTableView().getItems().clear();
		BugTable.getInstance().getTableView().setItems(bugs);
		BugTable.getInstance().getTableView().refresh();
		InformationPane.getInstance().updateTexts();
	}

	/**
	 * Sorts the bug table based on up to two parameters.
	 */
	public static List<Bug> sortBugs(List<Bug> bugs, boolean descending, String... sort)
	{
		BugComparator comparator = new BugComparator(sort);
		bugs.sort(comparator);

		if (descending)
		{
			Collections.reverse(bugs);
		}
		
		return bugs;
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
	 * Creates a display name from the current email. <p>
	 * E.g. 'Someone@example.com' would return 'Someone'
	 */
	public static String createDisplayName(String email)
	{
		return email.split("@")[0]; 
	}
	
	/**
	 * Helper method for calling all the methods that update colours.
	 */
	public static void updateColours()
	{
		InformationPane.getInstance().updateBackgroundColour();
		InformationPane.getInstance().updateHeadingColour();
		InformationPane.getInstance().updateSubheadingColour();
		InformationPane.getInstance().getTitle().setTextFill(Color.web(Colours.getWindowText()));

		RowColours.updateColours();		
	}
}