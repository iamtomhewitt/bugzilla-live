package gui.login.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import common.Errors;

import common.MessageBox;
import common.message.ApiRequestor;
import common.utilities.Encryptor;
import gui.app.main.BugzillaLive;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The main entry point for the login service.
 * 
 * @author Tom Hewitt
 * @since 2.0.0
 */
public class LoginService extends Application
{
	private Button loginButton 	= new Button("LOGIN");
	private Button apiKeyButton	= new Button("CREATE API KEY");

	private Stage stage;

	private TextField emailInput;
	private TextField apiKeyInput;
	private PasswordField passwordInput;
	
	private VBox vbox 			= new VBox();
	private VBox fieldsVbox		= new VBox();
	private VBox titleVbox		= new VBox();
	private VBox buttonsVbox 	= new VBox();
	
	private Label title = new Label("Bugzilla LIVE");
	
	private String icon = "file:" + "Icon.png";
		
	private LoginStyler styler 	= new LoginStyler();

	@Override
	public void start(Stage primaryStage) throws Exception
	{	
		loginButton.setOnAction(e -> execute());
		
		apiKeyButton.setOnAction(e ->
		{
			try
			{				
				String cmd = "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe /userprefs.cgi?tab=apikey";
				Runtime.getRuntime().exec(cmd);
			} 
			catch (IOException ex)
			{
				MessageBox.showExceptionDialog(Errors.FIREFOX, ex);
			}
		});
		
		ImageView logo = new ImageView(new Image(icon));
		
		emailInput = new TextField();
		emailInput.setPromptText("email address");
		emailInput.setAlignment(Pos.CENTER);
		emailInput.setOnKeyPressed(e -> 
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				execute();
			}
		});

		passwordInput = new PasswordField();
		passwordInput.setPromptText("password");
		passwordInput.setAlignment(Pos.CENTER);
		passwordInput.setOnKeyPressed(e -> 
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				execute();
			}
		});

		apiKeyInput = new TextField();
		apiKeyInput.setPromptText("api key");		
		apiKeyInput.setAlignment(Pos.CENTER);	
		apiKeyInput.setOnKeyPressed(e -> 
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				execute();
			}
		});
		
		Tooltip usernameTooltip = new Tooltip();
		usernameTooltip.setText("(e.g. thomas.hewitt)");
		emailInput.setTooltip(usernameTooltip);
		
		Tooltip apiKeyTooltip = new Tooltip();
		apiKeyTooltip.setText("Click the blue button if you do not have an API key");
		apiKeyInput.setTooltip(apiKeyTooltip);
		
		styler.styleInputField(emailInput);
		styler.styleInputField(passwordInput);
		styler.styleInputField(apiKeyInput);
		styler.styleLoginButton(loginButton);
		styler.styleAPIButton(apiKeyButton);
		styler.styleTitle(title);
		styler.styleLogo(logo);		
		
		titleVbox.getChildren().addAll(logo, title);
		titleVbox.setAlignment(Pos.CENTER);
		titleVbox.setSpacing(10);
		titleVbox.setPadding(new Insets(15));
		
		fieldsVbox.getChildren().addAll(emailInput, passwordInput, apiKeyInput);
		fieldsVbox.setAlignment(Pos.CENTER);
		fieldsVbox.setSpacing(10);
		fieldsVbox.setPadding(new Insets(15));
		
		buttonsVbox.getChildren().addAll(loginButton, apiKeyButton);
		buttonsVbox.setAlignment(Pos.CENTER);
		buttonsVbox.setSpacing(10);
		buttonsVbox.setPadding(new Insets(15));
		
		vbox.getChildren().addAll(titleVbox, fieldsVbox, buttonsVbox);
		vbox.setAlignment(Pos.CENTER);
		
		Platform.runLater(() -> loginButton.requestFocus());
		
		Scene scene = new Scene(vbox, 325, 550);
		stage = new Stage();
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setTitle("Login");
		stage.getIcons().add(new Image(icon));
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
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
				String url = String.format("/config/save?key=%s&value=%s", entry.getKey(), entry.getValue());
				String response = ApiRequestor.request(url);					
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
	
	public static void main(String[] args)
	{		
		launch(args);
	}
}