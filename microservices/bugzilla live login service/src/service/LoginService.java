package service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bugzilla.common.Errors;
import bugzilla.common.Folders;
import bugzilla.common.MessageBox;
import bugzilla.exception.MessageReceiverException;
import bugzilla.exception.MessageSenderException;
import bugzilla.message.config.ApplicationGetRequest;
import bugzilla.message.config.UserGetRequest;
import bugzilla.message.config.UserSaveRequest;
import bugzilla.utilities.Encryptor;
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
import message.LoginReceiver;
import message.LoginSender;

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

	private TextField usernameInput;
	private PasswordField passwordInput;
	private TextField apiKeyInput;
	
	private VBox vbox 			= new VBox();
	private VBox fieldsVbox		= new VBox();
	private VBox titleVbox		= new VBox();
	private VBox buttonsVbox 	= new VBox();
	
	private Label title 		= new Label("Bugzilla LIVE");
	
	private String icon = "file:" + Folders.ICONS_FOLDER + "Icon.png";
		
	private LoginReceiver 	messageReceiver = new LoginReceiver();	
	private LoginSender		messageSender 	= new LoginSender();
	private LoginStyler 	styler 			= new LoginStyler();

	@Override
	public void start(Stage primaryStage) throws Exception
	{		
		// Start running the message receiver
		Thread receiverThread = new Thread(() ->
		{
			try 
			{
				messageReceiver.start();
			} 
			catch (MessageReceiverException e) 
			{
				MessageBox.showExceptionDialog(Errors.GENERAL, e);
			}
		});
		receiverThread.setDaemon(true);
		receiverThread.start();
		
		// Request already stored login properties
		messageSender.sendRequestMessage(new UserGetRequest());		
		messageSender.sendRequestMessage(new ApplicationGetRequest());

		// Delay so properties are retrieved before the GUI starts
		Thread.sleep(1000);

		loginButton.setOnAction(e -> execute());
		
		apiKeyButton.setOnAction(e ->
		{
			try
			{
				if (messageReceiver.getRetrievedBugzillaUrl() == null || messageReceiver.getRetrievedBugzillaUrl().isEmpty())
				{
					MessageBox.showDialog("The Bugzilla URL was not retrieved from the config service properly. Either re-launch Bugzilla Live or open the API keys page manually.");
					return;
				}
				
				String cmd = "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe " + messageReceiver.getRetrievedBugzillaUrl() + "/userprefs.cgi?tab=apikey";
				Runtime.getRuntime().exec(cmd);
			} 
			catch (IOException ex)
			{
				MessageBox.showExceptionDialog(Errors.FIREFOX, ex);
			}
		});
		
		ImageView logo = new ImageView(new Image(icon));
		
		usernameInput = new TextField();
		usernameInput.setPromptText("username");
		usernameInput.setAlignment(Pos.CENTER);
		usernameInput.setOnKeyPressed(e -> 
		{
			if (e.getCode() == KeyCode.ENTER)
				execute();
		});

		passwordInput = new PasswordField();
		passwordInput.setPromptText("password");
		passwordInput.setAlignment(Pos.CENTER);
		passwordInput.setOnKeyPressed(e -> 
		{
			if (e.getCode() == KeyCode.ENTER)
				execute();
		});

		apiKeyInput = new TextField();
		apiKeyInput.setPromptText("api key");		
		apiKeyInput.setAlignment(Pos.CENTER);	
		apiKeyInput.setOnKeyPressed(e -> 
		{
			if (e.getCode() == KeyCode.ENTER)
				execute();
		});
		
		Tooltip usernameTooltip = new Tooltip();
		usernameTooltip.setText("(e.g. thomas.hewitt)");
		usernameInput.setTooltip(usernameTooltip);
		
		Tooltip apiKeyTooltip = new Tooltip();
		apiKeyTooltip.setText("Click the blue button if you do not have an API key");
		apiKeyInput.setTooltip(apiKeyTooltip);
		
		styler.styleInputField(usernameInput);
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
		
		fieldsVbox.getChildren().addAll(usernameInput, passwordInput, apiKeyInput);
		fieldsVbox.setAlignment(Pos.CENTER);
		fieldsVbox.setSpacing(10);
		fieldsVbox.setPadding(new Insets(15));
		
		buttonsVbox.getChildren().addAll(loginButton, apiKeyButton);
		buttonsVbox.setAlignment(Pos.CENTER);
		buttonsVbox.setSpacing(10);
		buttonsVbox.setPadding(new Insets(15));
		
		vbox.getChildren().addAll(titleVbox, fieldsVbox, buttonsVbox);
		vbox.setAlignment(Pos.CENTER);
		
		usernameInput.setText(messageReceiver.getRetrivedUsername());
		passwordInput.setText(messageReceiver.getRetrievedPassword());
		apiKeyInput.setText(messageReceiver.getRetrievedApiKey());
		
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
		return !(usernameInput.getText().equals("") || passwordInput.getText().equals("") || apiKeyInput.getText().contentEquals(""));
	}
	
	/*
	 * Executes the login procedure.
	 */
	private void execute()
	{
		if (canLogin())
		{
			// Stop processing as we do not want to process the messages we are about to make
			messageReceiver.stop();

			Map<String, String> userPropertiesToSave = new HashMap<String, String>();
			userPropertiesToSave.put("username", usernameInput.getText());
			userPropertiesToSave.put("password", Encryptor.encrypt(passwordInput.getText()));
			userPropertiesToSave.put("api_key", apiKeyInput.getText());

			try
			{
				messageSender.sendRequestMessage(new UserSaveRequest(userPropertiesToSave));			
				messageSender.sendRequestMessage(new ApplicationGetRequest());
				messageSender.sendRequestMessage(new UserGetRequest());
			
				Runtime.getRuntime().exec("java -jar \"" + Folders.GUI_SERVICE_FOLDER + "guiservice.jar\"");
				stage.close();
				Platform.exit();
				System.exit(0);
			} 
			catch (MessageSenderException | IOException ex)
			{
				MessageBox.showExceptionDialog(Errors.GENERAL, ex);
			}
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