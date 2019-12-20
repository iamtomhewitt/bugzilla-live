package ui.app.component;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.app.theme.Colours;
import ui.app.theme.Fonts;

public class WindowsBar
{		
	static double xOffset = 0;
	static double yOffset = 0;
	
	private static ToolBar mainToolBar;
	private static Label mainToolBarTitle;
		
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
	
	public static BorderPane createApplicationBar(Stage stage, Node center, String title)
	{
		maximize(stage);
		
		Color currentColour = Color.web(Colours.WINDOW);		
		
		String NORMAL 				= "-fx-background-color: " + Colours.toHex(currentColour);
		String CLOSE 				= "-fx-background-color: " + "#FF605C";
		String CLOSE_HIGHLIGHT		= "-fx-background-color: " + "#ff6e6b";
		String CLOSE_PRESSED 		= "-fx-background-color: " + "#e84540";
		String MAXIMISE 			= "-fx-background-color: " + "#FFBD44";
		String MAXIMISE_HIGHLIGHT	= "-fx-background-color: " + "#ffc254";
		String MAXIMISE_PRESSED 	= "-fx-background-color: " + "#f2a924";
		String MINIMISE 			= "-fx-background-color: " + "#00CA4E";
		String MINIMISE_HIGHLIGHT	= "-fx-background-color: " + "#00f45e";
		String MINIMISE_PRESSED 	= "-fx-background-color: " + "#01a340";

		mainToolBar = new ToolBar();
		BorderPane border = new BorderPane();
		
		Font font = Font.font(Fonts.FONT, FontWeight.BOLD, Fonts.FONT_SIZE_LARGE);
		
		stage.initStyle(StageStyle.UNDECORATED);

		mainToolBarTitle = new Label(title);
		mainToolBarTitle.setTextFill(Color.WHITE);
		mainToolBarTitle.setFont(font);

		Button maximise = createButton("", font, 10);
		applyMouseColourProperties(maximise, MAXIMISE, MAXIMISE_HIGHLIGHT, MAXIMISE_PRESSED);
		maximise.setOnAction(e -> maximize(stage));
		
		Button minimise = createButton("", font, 10);
		applyMouseColourProperties(minimise, MINIMISE, MINIMISE_HIGHLIGHT, MINIMISE_PRESSED);
		minimise.setOnAction(e -> ((Stage)((Button)e.getSource()).getScene().getWindow()).setIconified(true));
		
		Button close 	= createButton("", font, 10);
		applyMouseColourProperties(close, CLOSE, CLOSE_HIGHLIGHT, CLOSE_PRESSED);
		close.setOnAction(e -> stage.close());
		
		Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		mainToolBar.getItems().addAll(mainToolBarTitle, spacer, minimise, maximise, close);
		mainToolBar.setPrefHeight(30);
		mainToolBar.setMinHeight(30);
		mainToolBar.setMaxHeight(30);
		mainToolBar.setStyle(NORMAL);
		mainToolBar.setOnMousePressed(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) 
			{
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		mainToolBar.setOnMouseDragged(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) 
			{
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});
		
		border.setTop(mainToolBar);
		border.setCenter(center);

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

	private static void maximize(Stage s)
	{
		// Set up the screen bounds to not cover the taskbar
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		s.setX(primaryScreenBounds.getMinX());
		s.setY(primaryScreenBounds.getMinY());
		s.setMaxWidth(primaryScreenBounds.getWidth());
		s.setMinWidth(primaryScreenBounds.getWidth());
		s.setMaxHeight(primaryScreenBounds.getHeight());
		s.setMinHeight(primaryScreenBounds.getHeight());
	}

	public static ToolBar getMainToolBar()
	{
		return mainToolBar;
	}
	
	public static void updateMainToolBarLabel(String text)
	{
		mainToolBarTitle.setText(text);
	}
	
	public static void updateMainToolBarLabelColour()
	{
		mainToolBarTitle.setTextFill(Color.web(Colours.WINDOW_TEXT));
	}
	
	public static void updateMainToolBarColour()
	{
		getMainToolBar().setStyle("-fx-background-color: " + Colours.toHex(Color.web(Colours.WINDOW)));
	}
}
