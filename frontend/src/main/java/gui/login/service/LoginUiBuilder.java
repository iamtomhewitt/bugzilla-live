package gui.login.service;

import gui.app.theme.Fonts;
import gui.app.theme.Icons;
import gui.app.theme.Sizes;
import gui.app.theme.UiBuilder;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import gui.app.theme.Sizes.Size;

/**
 * A class for styling the login service. We can't use multiple inheritance so make it a class variable that can be used in the LoginService.
 */
public class LoginUiBuilder extends UiBuilder 
{
	public Button createLoginButton()
	{		
		String normalStyle 	= "-fx-background-color: #f5683d; -fx-text-fill: white";
		String hoverStyle 	= "-fx-background-color: #ed6338; -fx-text-fill: white";
		
		Button button = this.createButton("LOGIN", Size.MEDIUM, ButtonType.PRIMARY);
		button.setStyle(normalStyle);
		button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
		button.setOnMouseExited(e -> button.setStyle(normalStyle));		
		return button;
	}

	public Button createApiKeyButton()
	{
		String normalStyle 	= "-fx-background-color: #4690fb; -fx-text-fill: white";
		String hoverStyle 	= "-fx-background-color: #3c82e8; -fx-text-fill: white";
		
		Button button = this.createButton("CREATE API KEY", Size.MEDIUM, ButtonType.PRIMARY);
		button.setStyle(normalStyle);
		button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
		button.setOnMouseExited(e -> button.setStyle(normalStyle));		
		return button;
	}

	public ImageView createLogo()
	{		
		DropShadow shadow = new DropShadow();
		shadow.setOffsetX(5);
		shadow.setOffsetY(5);
		shadow.setColor(Color.GREY);
		
		ImageView logo = new Icons().createBugzillaIcon();
		logo.setFitHeight(125);
		logo.setFitWidth(125);		
		logo.setEffect(shadow);
		return logo;
	}
	
	public PasswordField createPasswordField(String prompt, Size size)
	{
		int width;
		int height = Sizes.INPUT_HEIGHT_SMALL;
		
		switch (size)
		{
			case SMALL:
					width = Sizes.INPUT_WIDTH_SMALL;
				break;

			case MEDIUM:
					width = Sizes.INPUT_WIDTH_MEDIUM;
				break;

			case LARGE:
					width = Sizes.INPUT_WIDTH_LARGE;
				break;

			case X_LARGE:
					width = Sizes.INPUT_WIDTH_X_LARGE;
				break;

			default:
					width = Sizes.INPUT_WIDTH_MEDIUM;
				break;
		}
		
		PasswordField passwordField = new PasswordField();
		passwordField.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, Fonts.FONT_SIZE_NORMAL));
		passwordField.setStyle("-fx-text-fill: black");
		passwordField.setAlignment(Pos.CENTER);
		passwordField.setMaxWidth(width);
		passwordField.setMinHeight(height);
		passwordField.setPromptText(prompt);
		return passwordField;
	}
}