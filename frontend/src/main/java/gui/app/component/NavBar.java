package gui.app.component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import common.bug.Bug;
import common.exception.Errors;
import common.exception.JsonTransformationException;
import common.message.MessageBox;
import common.utilities.JacksonAdapter;
import common.utilities.Utilities;
import gui.app.common.GuiConstants;
import gui.app.common.GuiMethods;
import gui.app.component.dialog.bug.AddBugDialog;
import gui.app.component.dialog.bug.GetBugsDialog;
import gui.app.theme.Fonts;
import gui.app.theme.UiBuilder;
import gui.app.theme.Icons;
import gui.app.theme.Sizes;

/**
 * The bar above the table showing buttons to navigate around the application.
 * 
 * @author Tom Hewitt
 * @since 1.0.0
 */	
public class NavBar
{
	private HBox navBar = new HBox();
	private FilteredList<Bug> filteredData = null;
	private int fieldHeight = 35;
	
	@SuppressWarnings("unchecked")
	public NavBar()
	{
		CheckBox pause = new CheckBox("Pause Refresh");
		pause.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, Fonts.FONT_SIZE_LARGE));
		pause.setOnAction(e ->  GuiConstants.PAUSED = pause.isSelected());
		
		TextField filterField = new TextField();
		filterField.setMinHeight(fieldHeight);
		filterField.setMinWidth(Sizes.INPUT_WIDTH_LARGE + 100);
		filterField.setAlignment(Pos.CENTER);
		filterField.setPromptText("filter");
		filterField.setTooltip(new Tooltip("Enter text to filter the table below. E.g., use 'severity:low' to get 'Low' bugs."));	
		filterField.setOnKeyReleased(f ->
		{
			filterField.textProperty().addListener((obsValue, oldValue, newValue) ->
			{
				try
				{
					// Get the bugs from the prefiltered data, i.e the master set of bugs
					ObservableList<Bug> data = FXCollections.observableArrayList(JacksonAdapter.fromJson(GuiConstants.PREFILTERED_BUG_DATA, Bug.class));

					filteredData = new FilteredList<>(data, e -> true);
					filteredData.setPredicate((Predicate<? super Bug>) bug ->
					{
						// See if any of the bugs contain the typed filter
						String filterText = newValue.toLowerCase();

						if (newValue == null || newValue.isEmpty())
						{
							return true;
						}
						
						if (bug.contains(filterText))
						{
							return true;
						}
						
						if (filterText.contains("severity:".toLowerCase()))
						{
							String[] s = filterText.split(":");
							
							if (s.length > 1)
							{
								String severity = s[1];
								
								if (severity == null)
								{
									return false;
								}
								
								if (bug.getSeverity().toLowerCase().contains(severity))
								{
									return true;
								}
							}
						}

						return false;
					});
				}
				catch (JsonTransformationException e)
				{
					MessageBox.showExceptionDialog(Errors.JACKSON_FROM, e);
				}
			});			

			if (filteredData == null || filteredData.isEmpty())
			{
				BugTable.getInstance().getTableView().getItems().clear();
				BugTable.getInstance().getTableView().refresh();
			
				Label l = new Label("Filter matches no bugs.");
				l.setFont(Font.font(Fonts.FONT_SIZE_SUPER));
				BugTable.getInstance().getTableView().setPlaceholder(l);
			}
			else
			{
				// Filtered data found, set table items
				SortedList<Bug> sorted = new SortedList<>(filteredData);
				sorted.comparatorProperty().bind(BugTable.getInstance().getTableView().comparatorProperty());
				BugTable.getInstance().getTableView().getItems().clear();
				BugTable.getInstance().getTableView().refresh();
				BugTable.getInstance().getTableView().setItems(FXCollections.observableArrayList(sorted));
			}
		});

		TextField browserField = new TextField();
		browserField.setMinHeight(fieldHeight);
		browserField.setAlignment(Pos.CENTER);
		browserField.setPromptText("bugzilla");
		browserField.setTooltip(new Tooltip("Enter a bug to open in Bugzilla"));		

		Button firefoxButton = new Button("", new Icons().createBrowserIcon());
		firefoxButton.setTooltip(new Tooltip("Open the bug in Bugzilla"));
		firefoxButton.setOnAction(e ->
		{
			try 
			{
				Utilities.openBugInBrowser(browserField.getText());
			} 
			catch (IOException | URISyntaxException e1) 
			{
				MessageBox.showExceptionDialog(Errors.BROWSER, e1);
			}
			browserField.setText("");
		});

		Button addButton = new Button("", new Icons().createAddIcon());
		addButton.setTooltip(new Tooltip("Add a new bug to this list"));
		addButton.setOnAction(e -> new AddBugDialog());

		Button refreshButton = new Button("", new Icons().createRefreshIcon());
		refreshButton.setTooltip(new Tooltip("Refresh the table"));
		refreshButton.setOnAction(e -> 
		{
			try
			{
				GuiMethods.requestRefreshOfBugsInTable();
			} 
			catch (Exception ex)
			{
				MessageBox.showExceptionDialog(Errors.REQUEST, ex);
				return;
			}
		});

		Button bugsButton = new Button("Get bugs", new Icons().createListIcon());
		bugsButton.setOnAction(e -> new GetBugsDialog());		
		
		UiBuilder.styleGraphicButton(addButton, Sizes.BUTTON_WIDTH_SMALL);
		UiBuilder.styleGraphicButton(firefoxButton, Sizes.BUTTON_WIDTH_SMALL);
		UiBuilder.styleGraphicButton(refreshButton, Sizes.BUTTON_WIDTH_SMALL);
		UiBuilder.styleGraphicButton(bugsButton, Sizes.BUTTON_WIDTH_MEDIUM);
		UiBuilder.styleTextField(filterField, Sizes.INPUT_WIDTH_X_LARGE, fieldHeight);
		UiBuilder.styleTextField(browserField, Sizes.INPUT_WIDTH_LARGE, fieldHeight);
		
		browserField.setOnKeyPressed(e ->
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				try 
				{
					Utilities.openBugInBrowser(browserField.getText());
				} 
				catch (IOException | URISyntaxException e1) 
				{
					MessageBox.showExceptionDialog(Errors.BROWSER, e1);
				}
				browserField.setText("");
			}
		});
		
		navBar.getChildren().addAll(filterField, createRegion(50), browserField, firefoxButton, addButton, refreshButton, bugsButton, createRegion(50), pause);
		navBar.setAlignment(Pos.CENTER);
		navBar.setSpacing(10);
		navBar.setPadding(new Insets(10, 5, 5, 5));
		navBar.setStyle("-fx-background-color: white");
	}

	public HBox getNavBar()
	{
		return navBar;
	}
	
	private Region createRegion(int minWidth)
	{
		Region r = new Region();
		r.setMinWidth(minWidth);
		return r;
	}
}