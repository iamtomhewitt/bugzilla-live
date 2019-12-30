package ui.login;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;

import common.error.Errors;
import common.message.ApiRequestor;
import common.message.ApiRequestor.ApiRequestType;
import common.message.Endpoints;
import common.message.MessageBox;
import common.utility.UiConstants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.main.BugzillaLive;
import ui.theme.Fonts;
import ui.theme.Sizes.Size;

/**
 * The main entry point for the application.
 * 
 * @author Tom Hewitt
 * @since 2.0.0
 */
public class Login extends Application
{
	private Stage stage;

	private TextField apiKeyInput;
	
	private LoginUiBuilder uiBuilder = new LoginUiBuilder();
				
	@Override
	public void start(Stage primaryStage)
	{	
		JSONObject response;
		try 
		{
			response = ApiRequestor.request(ApiRequestType.GET, Endpoints.CONFIG_GET);
		} 
		catch (Exception e) 
		{
			showCannotConnect(e);
			return;
		}
		
		JSONObject config = new JSONObject(response.getString("config"));
		
		UiConstants.BUGZILLA_URL = config.getString("bugzillaUrl");
		UiConstants.APIKEY = config.getString("apiKey");
				
		Label title = uiBuilder.createTitle("Bugzilla LIVE", Fonts.FONT_SIZE_X_SUPER);
		ImageView logo = uiBuilder.createLogo();
				
		Button loginButton = uiBuilder.createLoginButton();
		loginButton.setOnAction(e -> execute());
			
		Button apiKeyButton = uiBuilder.createApiKeyButton();
		apiKeyButton.setOnAction(e ->
		{
			try
			{				
				Desktop.getDesktop().browse(new URI(UiConstants.BUGZILLA_URL + "/userprefs.cgi?tab=apikey"));
			} 
			catch (IOException | URISyntaxException ex)
			{
				MessageBox.showExceptionDialog(Errors.BROWSER, ex);
			}
		});
		
		apiKeyInput = uiBuilder.createTextField("api key", Size.LARGE);
		apiKeyInput.setText(UiConstants.APIKEY);
		apiKeyInput.setTooltip(new Tooltip("Click the blue button if you do not have an API key"));	
		apiKeyInput.setOnKeyPressed(e -> 
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				execute();
			}
		});
		
		VBox titleVbox = createVBox(logo, title);
		VBox fieldsVbox = createVBox(apiKeyInput);
		VBox buttonsVbox = createVBox(loginButton, apiKeyButton);
		
		VBox vbox = new VBox();		
		vbox.getChildren().addAll(titleVbox, fieldsVbox, buttonsVbox);
		vbox.setAlignment(Pos.CENTER);
		
		Platform.runLater(() -> loginButton.requestFocus());
		
		Scene scene = new Scene(vbox, 275, 425);
		stage = new Stage();
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setTitle("Login");
		stage.getIcons().add(logo.getImage());
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
	}
	
	private VBox createVBox(Node... children)
	{
		VBox vbox = new VBox();
		vbox.getChildren().addAll(children);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		vbox.setPadding(new Insets(15));
		return vbox;
	}
	
	private boolean canLogin()
	{
		return !apiKeyInput.getText().contentEquals("");
	}
	
	/*
	 * Executes the login procedure.
	 */
	private void execute()
	{
		if (canLogin())
		{
			try
			{
				JSONObject response = ApiRequestor.request(ApiRequestType.GET, Endpoints.CONFIG_SAVE("apiKey", apiKeyInput.getText()));
				MessageBox.showErrorIfResponseNot200(response);
			} 
			catch (Exception e)
			{
				MessageBox.showExceptionDialog(Errors.REQUEST, e);
				return;
			}

			new BugzillaLive();
			stage.close();
		}
		else
		{
			MessageBox.showDialog(Errors.MISSING_FIELD);
		}
	}
	
	private void showCannotConnect(Exception e)
	{
		Scene scene = new Scene(new VBox(), 250, 20);
		stage = new Stage();
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
		stage.setTitle("Please Close This Window");
		MessageBox.showExceptionDialog(Errors.CANNOT_CONNECT, e);
	}
	
	public static void main(String[] args)
	{		
		launch(args);
	}
}