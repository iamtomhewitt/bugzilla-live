package gui.login.service;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import common.exception.Errors;
import common.message.ApiRequestor;
import common.message.Endpoints;
import common.message.MessageBox;
import common.utilities.Encryptor;
import gui.app.common.GuiConstants;
import gui.app.main.BugzillaLive;
import gui.app.theme.Sizes.Size;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The main entry point for the application.
 * 
 * @author Tom Hewitt
 * @since 2.0.0
 */
public class LoginService extends Application
{
	private Stage stage;

	private TextField emailInput;
	private TextField apiKeyInput;
	private PasswordField passwordInput;
	
	private LoginUiBuilder uiBuilder = new LoginUiBuilder();
				
	@Override
	public void start(Stage primaryStage)
	{	
		String response;
		try 
		{
			response = ApiRequestor.request(Endpoints.CONFIG_GET);
		} 
		catch (Exception e) 
		{
			showCannotConnect(e);
			return;
		}
		
		JSONObject json = new JSONObject(response);
		JSONObject config = new JSONObject(json.getString("config"));
		
		GuiConstants.BUGZILLA_URL = config.getString("bugzillaUrl");
				
		Label title = uiBuilder.createTitle("Bugzilla LIVE");
		ImageView logo = uiBuilder.createLogo();
		
		TextField emailInput = uiBuilder.createTextField("email address", Size.LARGE);
		TextField apiKeyInput = uiBuilder.createTextField("api key", Size.LARGE);
		PasswordField passwordInput = uiBuilder.createPasswordField("password", Size.LARGE);
		
		Button loginButton = uiBuilder.createLoginButton();
		loginButton.setOnAction(e -> execute());
			
		Button apiKeyButton = uiBuilder.createApiKeyButton();
		apiKeyButton.setOnAction(e ->
		{
			try
			{				
				Desktop.getDesktop().browse(new URI(GuiConstants.BUGZILLA_URL+"/userprefs.cgi?tab=apikey"));
			} 
			catch (IOException | URISyntaxException ex)
			{
				MessageBox.showExceptionDialog(Errors.BROWSER, ex);
			}
		});

		emailInput.setOnKeyPressed(e -> 
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				execute();
			}
		});
		
		passwordInput.setOnKeyPressed(e -> 
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				execute();
			}
		});
	
		apiKeyInput.setOnKeyPressed(e -> 
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				execute();
			}
		});
		
		emailInput.setTooltip(new Tooltip("Your email address for Bugzilla"));
		apiKeyInput.setTooltip(new Tooltip("Click the blue button if you do not have an API key"));	
		
		VBox titleVbox = createVBox(logo, title);
		VBox fieldsVbox = createVBox(emailInput, passwordInput, apiKeyInput);
		VBox buttonsVbox = createVBox(loginButton, apiKeyButton);
		
		VBox vbox = new VBox();		
		vbox.getChildren().addAll(titleVbox, fieldsVbox, buttonsVbox);
		vbox.setAlignment(Pos.CENTER);
		
		Platform.runLater(() -> loginButton.requestFocus());
		
		Scene scene = new Scene(vbox, 325, 550);
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

	/**
	 * @return <code>true</code> if all the login fields are filled in.
	 */
	private boolean canLogin()
	{
		return !(emailInput.getText().equals("") || passwordInput.getText().equals("") || apiKeyInput.getText().contentEquals(""));
	}
	
	/*
	 * Executes the login procedure.
	 */
	private void execute()
	{
		if (canLogin())
		{
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("username", emailInput.getText());
			properties.put("password", Encryptor.encrypt(passwordInput.getText()));
			properties.put("api_key", apiKeyInput.getText());

			for (Map.Entry<String, String> entry : properties.entrySet())
			{
				String response;
				
				try
				{
					response = ApiRequestor.request(Endpoints.CONFIG_SAVE(entry.getKey(), entry.getValue()));
				} 
				catch (Exception e)
				{
					MessageBox.showExceptionDialog(Errors.REQUEST, e);
					return;
				}					
				
				MessageBox.showErrorIfResponseNot200(response);
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