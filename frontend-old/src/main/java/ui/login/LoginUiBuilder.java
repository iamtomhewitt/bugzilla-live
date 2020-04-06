package ui.login;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import ui.theme.Icons;
import ui.theme.UiBuilder;
import ui.theme.Sizes.Size;

/**
 * A class for styling the login service. We can't use multiple inheritance so make it a class variable that can be used in the LoginService.
 */
public class LoginUiBuilder extends UiBuilder 
{
	public Button createLoginButton()
	{		
		String normalStyle 	= "-fx-background-color: #f5683d; -fx-text-fill: white";
		String hoverStyle 	= "-fx-background-color: #ed6338; -fx-text-fill: white";
		String clickedStyle	= "-fx-background-color: #b84825; -fx-text-fill: white";

		Button button = this.createButton("LOGIN", Size.MEDIUM, ButtonType.PRIMARY);
		button.setStyle(normalStyle);
		button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
		button.setOnMouseExited(e -> button.setStyle(normalStyle));		
		button.setOnMousePressed(e -> button.setStyle(clickedStyle));
		return button;
	}

	public Button createApiKeyButton()
	{
		String normalStyle 	= "-fx-background-color: #4690fb; -fx-text-fill: white";
		String hoverStyle 	= "-fx-background-color: #3c82e8; -fx-text-fill: white";
		String clickedStyle	= "-fx-background-color: #2262bf; -fx-text-fill: white";
		
		Button button = this.createButton("CREATE API KEY", Size.MEDIUM, ButtonType.PRIMARY);
		button.setStyle(normalStyle);
		button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
		button.setOnMouseExited(e -> button.setStyle(normalStyle));	
		button.setOnMousePressed(e -> button.setStyle(clickedStyle));
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
}