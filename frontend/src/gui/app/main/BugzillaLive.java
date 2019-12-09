package gui.app.main;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import gui.app.component.InformationPane;
import gui.app.component.NavBar;
import gui.app.common.GuiConstants;
import gui.app.common.GuiMethods;
import gui.app.component.BugTable;
import gui.app.component.Toolbar;
import gui.app.component.WindowsBar;
import gui.app.message.GuiMessageReceiver;
import common.Errors;
import common.Folders;
import common.Fonts;
import common.MessageBox;
import common.exception.JsonTransformationException;
import common.exception.MessageReceiverException;
import common.utilities.Icons;
import common.utilities.JacksonAdapter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class BugzillaLive extends Application
{
	private BugTable table = new BugTable();
	private BorderPane border = new BorderPane();
	private Toolbar toolbar = new Toolbar();
	private InformationPane infoPane = new InformationPane();
	private VBox top = new VBox();
	private VBox center = new VBox();
	private NavBar navBar = new NavBar();
	private GuiMessageReceiver messageReceiver = new GuiMessageReceiver();

	private static Stage mainStage;

	public static void main(String[] args) throws IOException, ParseException
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		BugzillaLive.mainStage = primaryStage;
		
		top.getChildren().add(toolbar.getMenuBar());

		center.getChildren().addAll(navBar.getNavBar(), table.getTableView());
		VBox.setVgrow(table.getTableView(), Priority.ALWAYS);

		border.setTop(top);
		border.setCenter(center);
		border.setLeft(infoPane.getPane());

		BorderPane.setAlignment(top, Pos.CENTER);
		BorderPane.setAlignment(center, Pos.CENTER);

		Scene scene = new Scene(WindowsBar.createApplicationBar(primaryStage, border, "Bugzilla Live"), 1920, 1050);
		primaryStage.getIcons().add(Icons.createBugzillaIcon().getImage());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Bugzilla LIVE");
		primaryStage.setOnCloseRequest(e ->
		{
			// Clear out any remaining messages
			File dir = new File(Folders.MESSAGE_FOLDER);
			for (File f : dir.listFiles())
				f.delete();

			Platform.exit();
			System.exit(0);
		});

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
		
		Thread t = new Thread(() ->
		{
			try 
			{
				messageReceiver.start();
			} 
			catch (MessageReceiverException e1) 
			{
				MessageBox.showExceptionDialog(Errors.GENERAL, e1);
			}
		});
		t.setDaemon(true);
		t.start();

		Thread refreshThread = new Thread(() ->
		{
			try
			{
				while (true)
				{
					// If for some reason the refresh time didn't get retrieved, just set to a normal value
					if (GuiConstants.REFRESH_TIME == 0)
						GuiConstants.REFRESH_TIME = 60;

					Thread.sleep(GuiConstants.REFRESH_TIME * 1000);
					
					if (!GuiConstants.PAUSED) GuiMethods.requestBugRefresh();
				}
			}
			catch (InterruptedException e)
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
