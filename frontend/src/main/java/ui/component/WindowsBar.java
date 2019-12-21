package ui.component;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.theme.Colours;
import ui.theme.Fonts;

public class WindowsBar
{		
	static double xOffset = 0;
	static double yOffset = 0;
			
	public static BorderPane createWindowsBar(Stage stage, Node center, String title)
	{
		Color currentColour = Color.web(Colours.WINDOW);
		
		String NORMAL = "-fx-background-color: " + Colours.toHex(currentColour);
		String HIGHLIGHT = "-fx-background-color: " + Colours.toHex(currentColour.brighter());
		String PRESSED = "-fx-background-color: " + Colours.toHex(currentColour.darker());

		ToolBar toolbar = new ToolBar();
		BorderPane border = new BorderPane();
		
		Font font = Font.font(Fonts.FONT, FontWeight.BOLD, Fonts.FONT_SIZE_LARGE);
		
		stage.initStyle(StageStyle.UNDECORATED);

		Label label = new Label(title);
		label.setTextFill(Color.web(Colours.WINDOW_TEXT));
		label.setFont(font);
		
		Button close = createButton("X", font, 30);
		applyMouseColourProperties(close, NORMAL, HIGHLIGHT, PRESSED );
		close.setOnAction(e -> stage.close());
		
		Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		toolbar.getItems().addAll(label, spacer, close);
		toolbar.setPrefHeight(30);
		toolbar.setMinHeight(30);
		toolbar.setMaxHeight(30);
		toolbar.setStyle(NORMAL);
		toolbar.setOnMousePressed(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) 
			{
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		toolbar.setOnMouseDragged(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) 
			{
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});
		
		border.setTop(toolbar);
		border.setCenter(center);
		border.setStyle("-fx-border-color: lightgrey; -fx-border-size: 5");	

		return border;
	}
	
	private static Button createButton(String text, Font font, int size)
	{
		Button b = new Button(text);
		b.setFont(font);
		b.setMinWidth(size);
		b.setMinHeight(size);
		b.setShape(new Circle(2));
		return b;
	}
	
	private static void applyMouseColourProperties(Button b, String normalColour, String highlightedColour, String pressedColour)
	{
		b.setStyle(normalColour);
		b.setTextFill(Color.WHITE);
		b.setOnMouseEntered(e -> b.setStyle(highlightedColour));
		b.setOnMouseExited(e -> b.setStyle(normalColour));
		b.setOnMousePressed(e -> b.setStyle(pressedColour));
	}
}