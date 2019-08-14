package component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import bugzilla.common.Fonts;
import theme.Colours;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import common.GuiConstants;

public class InformationPane
{
	private VBox pane = new VBox();
	private VBox refreshedVbox = new VBox();

	private List<Label> subsystemLabels 	= new ArrayList<Label>();
	private List<Label> statusLabels 		= new ArrayList<Label>();
	private List<Label> severityLabels 		= new ArrayList<Label>();
	private List<Label> headingLabels 		= new ArrayList<Label>();
	private List<Label> subheadingLabels 	= new ArrayList<Label>();
		
	private Label title;
	private Label refreshed;
	private Label refreshedTitle;

	private static InformationPane instance;

	public InformationPane()
	{
		instance = this;

		title = createLabel("ORs", Fonts.FONT, FontWeight.EXTRA_BOLD, Fonts.FONT_SIZE_SUPER, Color.WHITE);
		title.setWrapText(true);

		Label subsystem = createHeadingLabel("Subsystem");

		Label apmORs 		= createSubHeadingLabel("APM", Colours.toHex(Color.TOMATO), subsystemLabels);
		Label cmORs 		= createSubHeadingLabel("CM", Colours.toHex(Color.YELLOW), subsystemLabels);
		Label crmORs 		= createSubHeadingLabel("CRM", Colours.toHex(Color.YELLOWGREEN), subsystemLabels);
		Label fmORs 		= createSubHeadingLabel("FM", Colours.toHex(Color.LIGHTBLUE), subsystemLabels);
		Label tmsORs 		= createSubHeadingLabel("TMS", Colours.toHex(Color.BLUE), subsystemLabels);
		Label smORs 		= createSubHeadingLabel("SM", Colours.toHex(Color.PURPLE), subsystemLabels);
		Label infORs 		= createSubHeadingLabel("INF", Colours.toHex(Color.ORANGE), subsystemLabels);
		Label segmentORs 	= createSubHeadingLabel("SEGMENT", Colours.toHex(Color.ALICEBLUE), subsystemLabels);

		Label severity 		= createHeadingLabel("\nSeverity");
		Label critical 		= createSubHeadingLabel("Critical", Colours.CRITICAL, severityLabels);
		Label high 			= createSubHeadingLabel("High", Colours.HIGH, severityLabels);
		Label medium 		= createSubHeadingLabel("Medium", Colours.MEDIUM, severityLabels);
		Label low 			= createSubHeadingLabel("Low", Colours.LOW, severityLabels);
		Label unknown 		= createSubHeadingLabel("Unknown", Colours.UNKNOWN, severityLabels);

		Label status 		= createHeadingLabel("\nStatus");
		Label investigation = createSubHeadingLabel("Investigation", Colours.BLANK, statusLabels);
		Label diagnosed 	= createSubHeadingLabel("Diagnosed", Colours.BLANK, statusLabels);
		Label addressed		= createSubHeadingLabel("Addressed", Colours.ADDRESSED, statusLabels);
		Label coded 		= createSubHeadingLabel("Coded", Colours.CODED, statusLabels);
		Label built 		= createSubHeadingLabel("Built", Colours.BUILT, statusLabels);
		Label released 		= createSubHeadingLabel("Released", Colours.RELEASED, statusLabels);
		Label fixed			= createSubHeadingLabel("Fixed", Colours.FIXED, statusLabels);
		Label noFault		= createSubHeadingLabel("No Fault", Colours.NOFAULT, statusLabels);
		Label closed 		= createSubHeadingLabel("Closed", Colours.CLOSED, statusLabels);

		refreshed = createLabel("Never", Fonts.FONT, FontWeight.BOLD, Fonts.FONT_SIZE_LARGE, Color.WHITE);
		refreshedTitle = createHeadingLabel("Last Updated");
		refreshedVbox.getChildren().addAll(refreshedTitle, refreshed);

		subheadingLabels.add(refreshed);

		pane.getChildren().addAll(title, refreshedVbox, subsystem, apmORs, cmORs, crmORs, fmORs, smORs, tmsORs, infORs, segmentORs, severity, critical, high, medium, low, unknown, status, investigation, diagnosed, addressed, coded, built, released, fixed, noFault, closed);
		pane.setSpacing(12);
		pane.setPadding(new Insets(20, 15, 15, 15));
		pane.setMinWidth(250);
		pane.setMaxWidth(250);
		pane.setStyle("-fx-background-color: " + Colours.INFO_PANE_BACKGROUND);
	}

	public void updateTexts()
	{
		// .runLater() or will throw an exception 'Not on JavaFX thread'
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				String filename = GuiConstants.CURRENT_LIST_FILE == null ? "" : GuiConstants.CURRENT_LIST_FILE.getName().split("\\.")[0] + " | ";

				title.setText(filename + ORTable.getInstance().getTableView().getItems().size() + " ORs");

				refreshed.setText(Calendar.getInstance().getTime().toString());

				for (Label l : subsystemLabels)
				{
					String subsystem = l.getText().split(":")[0];
					l.setText(subsystem + ": " + ORCounter.countSubsystemORs(subsystem));
				}

				for (Label l : severityLabels)
				{
					String severity = l.getText().split(":")[0];
					l.setText(severity + ": " + ORCounter.countSeverityORs(severity) + " (Active: " + ORCounter.countActiveSeverityORs(severity) + ")");
				}

				for (Label l : statusLabels)
				{
					String status = l.getText().split(":")[0];
					l.setText(status + ": " + ORCounter.countStatusORs(status));
				}
			}
		});
	}

	private Label createSubHeadingLabel(String name, String iconColour, List<Label> list)
	{
		Label l = createLabel(name, Fonts.FONT, FontWeight.BOLD, Fonts.FONT_SIZE_LARGE, Color.web(Colours.INFO_PANE_SUBHEADING));
		list.add(l);
		subheadingLabels.add(l);

		Rectangle r = new Rectangle();
		r.setWidth(10);
		r.setHeight(10);
		r.setFill(Color.web(iconColour));
		
		l.setGraphic(r);
				
		return l;
	}

	private Label createHeadingLabel(String name)
	{
		Label l = createLabel(name, Fonts.FONT, FontWeight.BOLD, Fonts.FONT_SIZE_SUPER, Color.web(Colours.INFO_PANE_HEADING));
		headingLabels.add(l);
		return l;
	}

	private Label createLabel(String name, String font, FontWeight weight, int size, Color colour)
	{
		Label l = new Label(name);
		l.setFont(Font.font(font, weight, size));
		l.setTextFill(colour);
		return l;
	}

	public VBox getPane()
	{
		return pane;
	}

	public void updateBackgroundColour()
	{
		pane.setStyle("-fx-background-color: " + Colours.INFO_PANE_BACKGROUND);
	}

	public void updateHeadingColour()
	{
		Color colour = Color.web(Colours.INFO_PANE_HEADING);

		for (Label l : headingLabels)
			l.setTextFill(colour);
	}

	public void updateSubheadingColour()
	{
		Color colour = Color.web(Colours.INFO_PANE_SUBHEADING);

		for (Label l : subheadingLabels)
			l.setTextFill(colour);
	}
	
	public Label getTitle()
	{
		return title;
	}

	public static InformationPane getInstance()
	{
		return instance;
	}
}
