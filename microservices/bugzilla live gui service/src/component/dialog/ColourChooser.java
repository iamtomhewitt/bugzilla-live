package component.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.GuiMethods;
import common.common.Errors;
import common.common.Fonts;
import common.common.MessageBox;
import common.exception.JsonTransformationException;
import common.exception.MessageSenderException;
import common.message.config.ApplicationSaveRequest;
import common.utilities.Icons;
import component.BugTable;
import component.WindowsBar;
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
import message.GuiMessageSender;
import theme.Colours;
import theme.GuiStyler;
import theme.Sizes;

public class ColourChooser extends VBox
{		
	private List<ColourEntry> colourEntries = new ArrayList<ColourEntry>();

	public ColourChooser() 
	{
		Stage stage = new Stage();		
		
		ColourEntry windowBar 	= createColourEntry("Window", Colours.WINDOW);
		ColourEntry critical 	= createColourEntry("Critical", Colours.CRITICAL);
		ColourEntry high 		= createColourEntry("High", Colours.HIGH);
		ColourEntry medium 		= createColourEntry("Medium", Colours.MEDIUM);
		ColourEntry low 		= createColourEntry("Low", Colours.LOW);
		ColourEntry unknown 	= createColourEntry("Unknown", Colours.UNKNOWN);
		ColourEntry coded 		= createColourEntry("Coded", Colours.CODED);
		ColourEntry built 		= createColourEntry("Built", Colours.BUILT);
		ColourEntry released 	= createColourEntry("Released", Colours.RELEASED);
		ColourEntry addressed 	= createColourEntry("Addressed", Colours.ADDRESSED);
		ColourEntry fixed 		= createColourEntry("Fixed", Colours.FIXED);
		ColourEntry closed 		= createColourEntry("Closed", Colours.CLOSED);
		ColourEntry noFault 	= createColourEntry("No Fault", Colours.NOFAULT);
		ColourEntry infoPaneBackground 	= createColourEntry("Information Pane Background", Colours.INFO_PANE_BACKGROUND);
		ColourEntry infoPaneHeading		= createColourEntry("Information Pane Heading", Colours.INFO_PANE_HEADING);
		ColourEntry infoPaneSubheading	= createColourEntry("Information Pane Subheading", Colours.INFO_PANE_SUBHEADING);
		
		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e -> stage.close());
		
		Button applyButton = new Button("Apply");
		applyButton.setOnAction(e -> 
		{
			Colours.WINDOW 		= Colours.toHex(windowBar.getColourPicker().getValue());
			Colours.CRITICAL 	= Colours.toHex(critical.getColourPicker().getValue());
			Colours.HIGH 		= Colours.toHex(high.getColourPicker().getValue());
			Colours.MEDIUM 		= Colours.toHex(medium.getColourPicker().getValue());
			Colours.LOW 		= Colours.toHex(low.getColourPicker().getValue());
			Colours.UNKNOWN 	= Colours.toHex(unknown.getColourPicker().getValue());
			Colours.CODED 		= Colours.toHex(coded.getColourPicker().getValue());
			Colours.BUILT 		= Colours.toHex(built.getColourPicker().getValue());
			Colours.RELEASED 	= Colours.toHex(released.getColourPicker().getValue());
			Colours.ADDRESSED 	= Colours.toHex(addressed.getColourPicker().getValue());
			Colours.FIXED 		= Colours.toHex(fixed.getColourPicker().getValue());
			Colours.CLOSED 		= Colours.toHex(closed.getColourPicker().getValue());
			Colours.NOFAULT 	= Colours.toHex(noFault.getColourPicker().getValue());
			Colours.INFO_PANE_BACKGROUND	= Colours.toHex(infoPaneBackground.getColourPicker().getValue());
			Colours.INFO_PANE_HEADING 	= Colours.toHex(infoPaneHeading.getColourPicker().getValue());
			Colours.INFO_PANE_SUBHEADING= Colours.toHex(infoPaneSubheading.getColourPicker().getValue());

			BugTable.getInstance().getTableView().refresh();
			
			GuiMethods.updateColours();
			
			// Now send a config request to save the colours
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("windowcolour", Colours.WINDOW);		
			properties.put("windowtextcolour", Colours.WINDOW_TEXT);	
			properties.put("criticalcolour", Colours.CRITICAL);
			properties.put("highcolour", Colours.HIGH);
			properties.put("mediumcolour", Colours.MEDIUM);
			properties.put("lowcolour", Colours.LOW);
			properties.put("unknowncolour", Colours.UNKNOWN);
			properties.put("codedcolour", Colours.CODED);
			properties.put("builtcolour", Colours.BUILT);
			properties.put("releasedcolour", Colours.RELEASED);
			properties.put("addressedcolour", Colours.ADDRESSED);
			properties.put("fixedcolour", Colours.FIXED);
			properties.put("closedcolour", Colours.CLOSED);
			properties.put("nofaultcolour", Colours.NOFAULT);			
			properties.put("infopanebackgroundcolour", Colours.INFO_PANE_BACKGROUND);
			properties.put("infopaneheadingcolour", Colours.INFO_PANE_HEADING);
			properties.put("infopanesubheadingcolour", Colours.INFO_PANE_SUBHEADING);

			try
			{
				ApplicationSaveRequest request = new ApplicationSaveRequest(properties);
				new GuiMessageSender().sendRequestMessage(request);
			}
			catch (JsonTransformationException | MessageSenderException e1)
			{
				MessageBox.showExceptionDialog(Errors.GENERAL, e1);
			}
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
		
		this.getChildren().addAll(g, buttons);
		this.setAlignment(Pos.CENTER);
		this.setSpacing(15);
		this.setStyle("-fx-background-color: white");
		
		GuiStyler.stylePrimaryButton(applyButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiStyler.styleSecondaryButton(cancelButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL, FontWeight.BOLD);

		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, this, "Change Colours"), 300, 600);
		stage.setScene(scene);
		stage.setTitle("Change Colours");
		stage.getIcons().add(Icons.createThemeIcon().getImage());
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