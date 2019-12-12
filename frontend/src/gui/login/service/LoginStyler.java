package gui.login.service;

import common.Fonts;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * Styles aspects of the login window such as buttons and labels.
 * 
 * @author Tom Hewitt
 * @since 2.2.0
 */
public class LoginStyler
{
	public void styleButton(Button button)
	{
		button.setMinWidth(250);
		button.setMinHeight(40);
		button.setFont(Font.font(Fonts.FONT, FontWeight.BOLD, Fonts.FONT_SIZE_X_LARGE));
		
		DropShadow d = new DropShadow();
		d.setOffsetX(5);
		d.setOffsetY(5);
		d.setColor(Color.GREY);
		
		button.setEffect(d);
	}

	public void styleInputField(TextField field)
	{
		field.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, Fonts.FONT_SIZE_X_LARGE));
		field.setMaxWidth(250);
		field.setMinHeight(40);
		
		DropShadow d = new DropShadow();
		d.setOffsetX(5);
		d.setOffsetY(5);
		d.setColor(Color.GREY);
		
		field.setEffect(d);
	}
	
	public void styleTitle(Label title)
	{
		title.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, FontPosture.REGULAR, 30));
	}
	
	public void styleLoginButton(Button button)
	{
		styleButton(button);
		
		String normalStyle = "-fx-background-color: #f5683d; -fx-text-fill: white";
		String hoverStyle = "-fx-background-color: #ed6338; -fx-text-fill: white";
		
		button.setStyle(normalStyle);
		button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
		button.setOnMouseExited(e -> button.setStyle(normalStyle));
	}
	
	public void styleAPIButton(Button button)
	{
		styleButton(button);
		
		String normalStyle = "-fx-background-color: #4690fb; -fx-text-fill: white";
		String hoverStyle = "-fx-background-color: #3c82e8; -fx-text-fill: white";
		
		button.setStyle(normalStyle);
		button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
		button.setOnMouseExited(e -> button.setStyle(normalStyle));
	}
	
	public void styleLogo(ImageView logo)
	{
		logo.setFitHeight(100);
		logo.setFitWidth(100);
		DropShadow shadow = new DropShadow();
		shadow.setOffsetX(5);
		shadow.setOffsetY(5);
		shadow.setColor(Color.GREY);
		logo.setEffect(shadow);
	}
}
