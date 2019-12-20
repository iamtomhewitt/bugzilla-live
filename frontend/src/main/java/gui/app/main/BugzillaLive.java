package gui.app.main;

import gui.app.component.InformationPane;
import gui.app.component.NavBar;
import gui.app.common.GuiConstants;
import gui.app.common.GuiMethods;
import gui.app.component.BugTable;
import gui.app.component.Toolbar;
import gui.app.log.GuiLogger;
import gui.app.theme.Fonts;
import gui.app.theme.Icons;
import common.exception.Errors;
import common.exception.JsonTransformationException;
import common.message.MessageBox;
import common.utility.JacksonAdapter;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

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
		GuiLogger.getInstance().print("Starting Bugzilla Live...");
		
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
		primaryStage.setTitle("Bugzilla LIVE");
		primaryStage.setMaximized(true);

		Label welcomeLabel = new Label("Welcome to Bugzilla LIVE.\nTo get started, select or create a list using the File, Lists menu.\nUse the Help menu if you get stuck.");
		welcomeLabel.setTextAlignment(TextAlignment.CENTER);
		welcomeLabel.setFont(Font.font(Fonts.FONT_SIZE_SUPER));
		table.getTableView().setPlaceholder(welcomeLabel);

		try
		{
			GuiConstants.PREFILTERED_BUG_DATA = JacksonAdapter.toJson(table.getTableView().getItems());
		}
		catch (JsonTransformationException e1)
		{
			MessageBox.showExceptionDialog(Errors.JACKSON_TO, e1);
		}
		
		primaryStage.show();
		

		Thread refreshThread = new Thread(() ->
		{
			try
			{
				while (true)
				{
					// If for some reason the refresh time didn't get retrieved, just set to a normal value
					if (GuiConstants.REFRESH_TIME == 0)
					{
						GuiConstants.REFRESH_TIME = 60;
					}
					
					Thread.sleep(GuiConstants.REFRESH_TIME * 1000);
					
					if (!GuiConstants.PAUSED) 
					{
						GuiMethods.requestBugRefresh();
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
