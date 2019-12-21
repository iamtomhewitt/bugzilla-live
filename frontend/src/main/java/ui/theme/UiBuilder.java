package ui.theme;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import ui.theme.Sizes.Size;

/**
 * An abstract class for creating base UI components such as buttons and textfields.
 * The returned object can then be manipulated to fit as you need.
 *
 */
public abstract class UiBuilder
{	
	public enum ButtonType { PRIMARY, SECONDARY, NONE }
	
	public Button createButton(String name, Size size, ButtonType type)
	{		
		String normalStyle 	= type == ButtonType.PRIMARY ? "-fx-background-color: " + Colours.toHex(Color.web(Colours.WINDOW)) 				+ "; -fx-background-radius: 0;" : "-fx-background-color: #e5e5e5; -fx-text-fill: #848383; -fx-background-radius: 0;";
		String hoverStyle 	= type == ButtonType.PRIMARY ? "-fx-background-color: " + Colours.toHex(Color.web(Colours.WINDOW).brighter()) 	+ "; -fx-background-radius: 0;" : "-fx-background-color: #adabab; -fx-text-fill: white; -fx-background-radius: 0;";
		String clickedStyle = type == ButtonType.PRIMARY ? "-fx-background-color: " + Colours.toHex(Color.web(Colours.WINDOW).darker()) 	+ "; -fx-background-radius: 0;" : "";
		
		FontWeight weight = type == ButtonType.PRIMARY ? FontWeight.BOLD : FontWeight.NORMAL;
		
		int width = Sizes.calculateButtonWidth(size);
		int height = Sizes.calculateButtonHeight(size);

		Button button = new Button(name);
		button.setMinWidth(width);
		button.setMinHeight(height);
		button.setFont(Font.font(Fonts.FONT, weight, Fonts.FONT_SIZE_NORMAL));
		button.setStyle(normalStyle);
		button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
		button.setOnMouseExited(e -> button.setStyle(normalStyle));
		button.setOnMousePressed(e -> button.setStyle(clickedStyle));
		button.setTextFill(Color.web(Colours.WINDOW_TEXT));
		
		return button;
	}
	
	public TextField createTextField(String prompt, Size size)
	{
		int width = Sizes.calculateTextFieldWidth(size);
		int height = Sizes.calculateTextFieldHeight(size);
		
		TextField textField = new TextField();
		textField.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, Fonts.FONT_SIZE_NORMAL));
		textField.setStyle("-fx-text-fill: black");
		textField.setAlignment(Pos.CENTER);
		textField.setMaxWidth(width);
		textField.setMinHeight(height);
		textField.setPromptText(prompt);
		return textField;
	}
	
	public Label createTitle(String name, int fontSize)
	{
		Label label = new Label(name);
		label.setFont(Font.font(Fonts.FONT, FontWeight.BOLD, fontSize));
		return label;
	}
	
	public ComboBox<String> createComboBox(String... items)
	{
		String normalStyle = "-fx-background-color: #e5e5e5; -fx-text-fill: #848383; -fx-background-radius: 0;";
		String hoverStyle = "-fx-background-color: #adabab; -fx-text-fill: white; -fx-background-radius: 0;";
		
		ComboBox<String> combo = new ComboBox<String>();
		combo.setMinHeight(30);
		combo.setMinWidth(100);
		combo.setStyle(normalStyle);
		combo.setOnMouseEntered(e -> combo.setStyle(hoverStyle));
		combo.setOnMouseExited(e -> combo.setStyle(normalStyle));
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

		return combo;
	}

	public Button createButtonWithGraphic(String name, Size size, ImageView icon)
	{
		Button button = createButton(name, size, ButtonType.NONE);
		button.setGraphic(icon);
		return button;
	}
}
