package ui.component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import common.utility.UiConstants;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ui.theme.Colours;
import ui.theme.Fonts;

public class InformationPane
{
	private VBox pane = new VBox();
	private VBox refreshedVbox = new VBox();

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

		title = createLabel("Bugs", Fonts.FONT, FontWeight.EXTRA_BOLD, Fonts.FONT_SIZE_SUPER, Color.WHITE);
		title.setWrapText(true);

		Label severity 		= createHeadingLabel("\nSeverity");
		Label blocker 		= createSubHeadingLabel("Blocker", Colours.CRITICAL, severityLabels);
		Label critical 		= createSubHeadingLabel("Critical", Colours.CRITICAL, severityLabels);
		Label major 		= createSubHeadingLabel("Major", Colours.MAJOR, severityLabels);
		Label minor 		= createSubHeadingLabel("Minor", Colours.MINOR, severityLabels);
		Label normal		= createSubHeadingLabel("Normal", Colours.NORMAL, severityLabels);

		Label status 		= createHeadingLabel("\nStatus");
		Label worksForMe	= createSubHeadingLabel("Works For Me", Colours.WORKS_FOR_ME, statusLabels);
		Label fixed			= createSubHeadingLabel("Fixed", Colours.FIXED, statusLabels);
		Label wontFix		= createSubHeadingLabel("Won't Fix", Colours.NOFAULT, statusLabels);
		Label closed 		= createSubHeadingLabel("Closed", Colours.RESOLVED, statusLabels);

		refreshed = createLabel("Never", Fonts.FONT, FontWeight.BOLD, Fonts.FONT_SIZE_LARGE, Color.WHITE);
		refreshedTitle = createHeadingLabel("Last Updated");
		refreshedVbox.getChildren().addAll(refreshedTitle, refreshed);

		subheadingLabels.add(refreshed);

		pane.getChildren().addAll(title, refreshedVbox, severity, blocker, critical, major, minor, normal, status, worksForMe, fixed, wontFix, closed);
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
				String filename = UiConstants.CURRENT_LIST == null ? "" : UiConstants.CURRENT_LIST.split("\\.")[0] + " | ";

				title.setText(filename + BugTable.getInstance().getTableView().getItems().size() + " Bugs");

				refreshed.setText(Calendar.getInstance().getTime().toString());

				for (Label l : severityLabels)
				{
					String severity = l.getText().split(":")[0];
					l.setText(severity + ": " + BugCounter.countSeverityBugs(BugTable.getInstance().getTableView().getItems(), severity) + " (Active: " + BugCounter.countActiveSeverityBugs(BugTable.getInstance().getTableView().getItems(), severity) + ")");
				}

				for (Label l : statusLabels)
				{
					String status = l.getText().split(":")[0];
					l.setText(status + ": " + BugCounter.countStatusBugs(BugTable.getInstance().getTableView().getItems(), status));
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
		{
			l.setTextFill(colour);
		}
	}

	public void updateSubheadingColour()
	{
		Color colour = Color.web(Colours.INFO_PANE_SUBHEADING);

		for (Label l : subheadingLabels)
		{
			l.setTextFill(colour);
		}
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