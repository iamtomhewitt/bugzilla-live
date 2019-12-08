package theme;

import common.common.Fonts;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

public class GuiStyler
{	
	public static void stylePrimaryButton(Button button, int width, int height)
	{
		String normalStyle = "-fx-background-color: " + Colours.toHex(Color.web(Colours.WINDOW)) + "; -fx-background-radius: 0;";
		String hoverStyle = "-fx-background-color: " + Colours.toHex(Color.web(Colours.WINDOW).brighter()) + "; -fx-background-radius: 0;";
		String clickedStyle = "-fx-background-color: " + Colours.toHex(Color.web(Colours.WINDOW).darker()) + "; -fx-background-radius: 0;";
		
		button.setMinWidth(width);
		button.setMinHeight(height);
		button.setFont(Font.font(Fonts.FONT, FontWeight.BOLD, Fonts.FONT_SIZE_NORMAL));
		button.setStyle(normalStyle);
		button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
		button.setOnMouseExited(e -> button.setStyle(normalStyle));
		button.setOnMousePressed(e -> button.setStyle(clickedStyle));
		button.setTextFill(Color.web(Colours.WINDOW_TEXT));
	}
	
	public static void styleSecondaryButton(Button button, int width, int height, FontWeight font)
	{
		String normalStyle = "-fx-background-color: #e5e5e5; -fx-text-fill: #848383; -fx-background-radius: 0;";
		String hoverStyle = "-fx-background-color: #adabab; -fx-text-fill: white; -fx-background-radius: 0;";
		
		button.setMinWidth(width);
		button.setMinHeight(height);
		button.setFont(Font.font(Fonts.FONT, font, Fonts.FONT_SIZE_NORMAL));
		button.setStyle(normalStyle);
		button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
		button.setOnMouseExited(e -> button.setStyle(normalStyle));
	}
	
	public static void styleGraphicButton(Button button, int width)
	{
		String normalStyle = "-fx-background-color: #e5e5e5;";
		String hoverStyle = "-fx-background-color: #d1d1d1;";
		
		button.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, Fonts.FONT_SIZE_NORMAL));
		button.setStyle(normalStyle);
		button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
		button.setOnMouseExited(e -> button.setStyle(normalStyle));
		button.setMinHeight(35);
		button.setPrefWidth(width);
	}
	
	public static void styleTextField(TextField textField, int width, int height)
	{
		textField.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, Fonts.FONT_SIZE_NORMAL));
		textField.setStyle("-fx-text-fill: black");
		textField.setAlignment(Pos.CENTER);
		textField.setMaxWidth(width);
		textField.setMinHeight(height);
	}
	
	public static void styleTitle(Label label)
	{
		label.setFont(Font.font(Fonts.FONT, FontWeight.BOLD, Fonts.FONT_SIZE_LARGE));
	}
	
	public static void styleComboBox(ComboBox<String> combo)
	{
		String normalStyle = "-fx-background-color: #e5e5e5; -fx-text-fill: #848383; -fx-background-radius: 0;";
		String hoverStyle = "-fx-background-color: #adabab; -fx-text-fill: white; -fx-background-radius: 0;";
		
		combo.setMinHeight(30);
		combo.setMinWidth(Sizes.BUTTON_WIDTH_SMALL);
		combo.buttonCellProperty().bind(Bindings.createObjectBinding(() ->
		{
			return new ListCell<String>()
			{
				@Override
				protected void updateItem(String item, boolean empty)
				{
					super.updateItem(item, empty);
					setText(item);
					setTextFill(Color.web("#848383"));
					setFont(Font.font(Fonts.FONT, FontWeight.BOLD, Fonts.FONT_SIZE_NORMAL));
				}
			};
		}, combo.valueProperty()));
		combo.setCellFactory(new Callback<ListView<String>, ListCell<String>>()
		{
			@Override
			public ListCell<String> call(ListView<String> p)
			{
				return new ListCell<String>()
				{
					@Override
					protected void updateItem(String item, boolean empty)
					{
						super.updateItem(item, empty);
						if (item != null)
						{
							setText(item);
							getStyleClass().add("my-list-cell");
							setTextFill(Color.web("#848383"));
							setStyle(normalStyle);
							setFont(Font.font(Fonts.FONT, FontWeight.BOLD, Fonts.FONT_SIZE_NORMAL));
							setOnMouseEntered(e->setStyle(hoverStyle));
							setOnMouseExited(e->setStyle(normalStyle));
						}
					}
				};
			}
		});

		combo.setStyle(normalStyle);
		combo.setOnMouseEntered(e -> combo.setStyle(hoverStyle));
		combo.setOnMouseExited(e -> combo.setStyle(normalStyle));
	}
}
