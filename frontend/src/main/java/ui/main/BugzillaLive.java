package ui.main;

import org.json.JSONObject;

import common.error.Errors;
import common.error.JsonTransformationException;
import common.error.RequestException;
import common.message.ApiRequestor;
import common.message.ApiRequestor.ApiRequestType;
import common.message.Endpoints;
import common.message.MessageBox;
import common.utility.JacksonAdapter;
import common.utility.UiConstants;
import common.utility.UiMethods;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import ui.component.BugTable;
import ui.component.InformationPane;
import ui.component.NavBar;
import ui.component.Toolbar;
import ui.log.UiLogger;
import ui.theme.Fonts;
import ui.theme.Icons;
import ui.theme.Themes;

public class BugzillaLive
{
	private BugTable table = new BugTable();
	private BorderPane border = new BorderPane();
	private Toolbar toolbar = new Toolbar();
	private InformationPane infoPane = new InformationPane();
	private VBox top = new VBox();
	private VBox center = new VBox();
	private NavBar navBar = new NavBar();

	private static Stage mainStage;
	
	public BugzillaLive()
	{
		UiLogger.getInstance().print("Starting Bugzilla Live...");
		
		Stage primaryStage = new Stage();
		BugzillaLive.mainStage = primaryStage;
		
		top.getChildren().add(toolbar.getMenuBar());

		center.getChildren().addAll(navBar.getNavBar(), table.getTableView());
		VBox.setVgrow(table.getTableView(), Priority.ALWAYS);

		border.setTop(top);
		border.setCenter(center);
		border.setLeft(infoPane.getPane());

		BorderPane.setAlignment(top, Pos.CENTER);
		BorderPane.setAlignment(center, Pos.CENTER);

		Scene scene = new Scene(border);
		primaryStage.getIcons().add(new Icons().createBugzillaIcon().getImage());
		primaryStage.setScene(scene);
		primaryStage.setTitle(UiMethods.createApplicationTitle(UiConstants.USER_EMAIL));
		primaryStage.setMaximized(true);

		Label welcomeLabel = new Label("Welcome to Bugzilla LIVE.\nTo get started, select or create a list using the File, Lists menu.\nUse the Help menu if you get stuck.");
		welcomeLabel.setTextAlignment(TextAlignment.CENTER);
		welcomeLabel.setFont(Font.font(Fonts.FONT_SIZE_SUPER));
		table.getTableView().setPlaceholder(welcomeLabel);

		try
		{
			UiConstants.PREFILTERED_BUG_DATA = JacksonAdapter.toJson(table.getTableView().getItems());
		}
		catch (JsonTransformationException e1)
		{
			MessageBox.showExceptionDialog(Errors.JACKSON_TO, e1);
		}
		
		try 
		{
			JSONObject response = ApiRequestor.request(ApiRequestType.GET, Endpoints.CONFIG_GET);
			JSONObject colours = new JSONObject(response.getString("config")).getJSONObject("colours");
			Themes.updateColours(colours);
		} 
		catch (RequestException e1) 
		{
			MessageBox.showExceptionDialog(Errors.REQUEST, e1);
		}
		
		primaryStage.show();		

		Thread refreshThread = new Thread(() ->
		{
			try
			{
				while (true)
				{
					// If for some reason the refresh time didn't get retrieved, just set to a normal value
					if (UiConstants.REFRESH_TIME == 0)
					{
						UiConstants.REFRESH_TIME = 60;
					}
					
					Thread.sleep(UiConstants.REFRESH_TIME * 1000);
					
					if (!UiConstants.PAUSED) 
					{
						UiMethods.requestBugRefresh();
					}
				}
			}
			catch (Exception e)
			{
				MessageBox.showExceptionDialog(Errors.REFRESH, e);
			}
		});
		refreshThread.setDaemon(true);
		refreshThread.start();
	}
	
	public static Stage getMainStage()
	{
		return mainStage;
	}
}
