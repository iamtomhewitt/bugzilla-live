package gui.app.component.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gui.app.common.GuiMethods;
import gui.app.component.BugTable;
import gui.app.component.WindowsBar;

import gui.app.theme.Colours;
import gui.app.theme.Fonts;
import gui.app.theme.UiBuilder;
import gui.app.theme.Sizes.Size;
import gui.app.theme.Icons;
import gui.app.theme.Sizes;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ColourChooser extends UiBuilder
{		
	private List<ColourEntry> colourEntries = new ArrayList<ColourEntry>();

	public ColourChooser() 
	{
		Stage stage = new Stage();		
		
		ColourEntry windowBar 			= createColourEntry("Window", Colours.WINDOW);
		ColourEntry critical 			= createColourEntry("Critical", Colours.CRITICAL);
		ColourEntry high 				= createColourEntry("Major", Colours.MAJOR);
		ColourEntry medium 				= createColourEntry("Medium", Colours.MINOR);
		ColourEntry low 				= createColourEntry("Normal", Colours.NORMAL);
		ColourEntry addressed 			= createColourEntry("Works For Me", Colours.WORKS_FOR_ME);
		ColourEntry fixed 				= createColourEntry("Fixed", Colours.FIXED);
		ColourEntry closed 				= createColourEntry("Resolved", Colours.RESOLVED);
		ColourEntry noFault 			= createColourEntry("WONTFIX / DUPLICATE", Colours.NOFAULT);
		ColourEntry infoPaneBackground 	= createColourEntry("Information Pane Background", Colours.INFO_PANE_BACKGROUND);
		ColourEntry infoPaneHeading		= createColourEntry("Information Pane Heading", Colours.INFO_PANE_HEADING);
		ColourEntry infoPaneSubheading	= createColourEntry("Information Pane Subheading", Colours.INFO_PANE_SUBHEADING);
		
		Button cancelButton = createButton("Cancel", Size.SMALL, ButtonType.SECONDARY);
		cancelButton.setOnAction(e -> stage.close());
		
		Button applyButton = createButton("Apply", Size.SMALL, ButtonType.PRIMARY);
		applyButton.setOnAction(e -> 
		{
			Colours.WINDOW 					= Colours.toHex(windowBar.getColourPicker().getValue());
			Colours.CRITICAL 				= Colours.toHex(critical.getColourPicker().getValue());
			Colours.MAJOR 					= Colours.toHex(high.getColourPicker().getValue());
			Colours.MINOR 					= Colours.toHex(medium.getColourPicker().getValue());
			Colours.NORMAL 					= Colours.toHex(low.getColourPicker().getValue());
			Colours.WORKS_FOR_ME 			= Colours.toHex(addressed.getColourPicker().getValue());
			Colours.FIXED 					= Colours.toHex(fixed.getColourPicker().getValue());
			Colours.RESOLVED 				= Colours.toHex(closed.getColourPicker().getValue());
			Colours.NOFAULT 				= Colours.toHex(noFault.getColourPicker().getValue());
			Colours.INFO_PANE_BACKGROUND	= Colours.toHex(infoPaneBackground.getColourPicker().getValue());
			Colours.INFO_PANE_HEADING 		= Colours.toHex(infoPaneHeading.getColourPicker().getValue());
			Colours.INFO_PANE_SUBHEADING	= Colours.toHex(infoPaneSubheading.getColourPicker().getValue());

			BugTable.getInstance().getTableView().refresh();
			
			GuiMethods.updateColours();
			
			// Now send a config request to save the colours
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("windowcolour", Colours.WINDOW);		
			properties.put("windowtextcolour", Colours.WINDOW_TEXT);	
			properties.put("criticalcolour", Colours.CRITICAL);
			properties.put("highcolour", Colours.MAJOR);
			properties.put("mediumcolour", Colours.MINOR);
			properties.put("lowcolour", Colours.NORMAL);
			properties.put("addressedcolour", Colours.WORKS_FOR_ME);
			properties.put("fixedcolour", Colours.FIXED);
			properties.put("closedcolour", Colours.RESOLVED);
			properties.put("nofaultcolour", Colours.NOFAULT);			
			properties.put("infopanebackgroundcolour", Colours.INFO_PANE_BACKGROUND);
			properties.put("infopaneheadingcolour", Colours.INFO_PANE_HEADING);
			properties.put("infopanesubheadingcolour", Colours.INFO_PANE_SUBHEADING);

			// TODO use ApiRequestor
			stage.close();
		});
		
		HBox buttons = new HBox(applyButton, cancelButton);
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);
		
		GridPane g = new GridPane();
		
		int i = 0;
		for (ColourEntry c : colourEntries)
		{
			g.add(c.getLabel(), 0, i);
			g.add(c.getColourPicker(), 1, i);
			i++;
		}
		
		g.setHgap(10);
		g.setVgap(5);
		g.setAlignment(Pos.CENTER);
		
		VBox vbox = new VBox(g, buttons);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(15);
		vbox.setStyle("-fx-background-color: white");

		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, vbox, "Change Colours"), 300, 600);
		stage.setScene(scene);
		stage.setTitle("Change Colours");
		stage.getIcons().add(new Icons().createThemeIcon().getImage());
		stage.show();
		stage.centerOnScreen();
	}
	
	private Label createLabel(String name)
	{
		Label l = new Label(name);
		l.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, Fonts.FONT_SIZE_NORMAL));
		return l;
	}
	
	private ColourEntry createColourEntry(String name, String defaultColour)
	{
		ColorPicker picker = new ColorPicker();
		picker.setValue(Color.valueOf(defaultColour));
		picker.setMaxWidth(Sizes.BUTTON_WIDTH_SMALL);
		picker.getStyleClass().add("split-button");
		
		Label label = createLabel(name);
		
		ColourEntry entry = new ColourEntry(label, picker);
		colourEntries.add(entry);

		return entry;
	}
}

class ColourEntry
{
	private Label label;
	private ColorPicker colourPicker;

	public ColourEntry(Label l, ColorPicker c)
	{
		label = l;
		colourPicker = c;
	}

	public Label getLabel()
	{
		return label;
	}

	public ColorPicker getColourPicker()
	{
		return colourPicker;
	}
}