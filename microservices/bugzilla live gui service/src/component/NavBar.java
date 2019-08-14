package component;

import java.io.IOException;
import java.util.function.Predicate;

import bugzilla.common.Errors;
import bugzilla.common.Fonts;
import bugzilla.common.MessageBox;
import bugzilla.common.OR.OR;
import bugzilla.exception.JsonTransformationException;
import bugzilla.utilities.Icons;
import bugzilla.utilities.JacksonAdapter;
import bugzilla.utilities.Utilities;
import component.dialog.OR.AddORDialog;
import component.dialog.OR.GetORsDialog;
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
import theme.GuiStyler;
import theme.Sizes;
import common.GuiConstants;
import common.GuiMethods;

/**
 * The bar above the table showing buttons to navigate around the application.
 * 
 * @author Tom Hewitt
 * @since 1.0.0
 */	
public class NavBar
{
	private HBox navBar = new HBox();
	private FilteredList<OR> filteredData = null;
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
		filterField.setTooltip(new Tooltip("Enter text to filter the table below. E.g., use 'severity:low' to get 'Low' ORs."));	
		filterField.setOnKeyReleased(f ->
		{
			filterField.textProperty().addListener((obsValue, oldValue, newValue) ->
			{
				try
				{
					// Get the ORs from the prefiltered data, i.e the master set of ORs
					ObservableList<OR> data = FXCollections.observableArrayList(JacksonAdapter.fromJson(GuiConstants.PREFILTERED_OR_DATA, OR.class));

					filteredData = new FilteredList<>(data, e -> true);
					filteredData.setPredicate((Predicate<? super OR>) or ->
					{
						// See if any of the ORs contain the typed filter
						String filterText = newValue.toLowerCase();

						if (newValue == null || newValue.isEmpty())
							return true;
						
						if (or.contains(filterText))
							return true;
						
						if (filterText.contains("severity:".toLowerCase()))
						{
							String[] s = filterText.split(":");
							
							if (s.length > 1)
							{
								String severity = s[1];
								
								if (severity == null)
									return false;
								
								if (or.getSeverity().toLowerCase().contains(severity))
									return true;
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
				ORTable.getInstance().getTableView().getItems().clear();
				ORTable.getInstance().getTableView().refresh();
			
				Label l = new Label("Filter matches no ORs.");
				l.setFont(Font.font(Fonts.FONT_SIZE_SUPER));
				ORTable.getInstance().getTableView().setPlaceholder(l);
			}
			else
			{
				// Filtered data found, set table items
				SortedList<OR> sorted = new SortedList<>(filteredData);
				sorted.comparatorProperty().bind(ORTable.getInstance().getTableView().comparatorProperty());
				ORTable.getInstance().getTableView().getItems().clear();
				ORTable.getInstance().getTableView().refresh();
				ORTable.getInstance().getTableView().setItems(FXCollections.observableArrayList(sorted));
			}
		});

		TextField firefoxField = new TextField();
		firefoxField.setMinHeight(fieldHeight);
		firefoxField.setAlignment(Pos.CENTER);
		firefoxField.setPromptText("bugzilla");
		firefoxField.setTooltip(new Tooltip("Enter an OR to open in Bugzilla"));		

		Button firefoxButton = new Button("", Icons.createFirefoxIcon());
		firefoxButton.setTooltip(new Tooltip("Open the OR in Bugzilla"));
		firefoxButton.setOnAction(e ->
		{
			try 
			{
				Utilities.openORInFirefox(GuiConstants.BUGZILLA_URL, firefoxField.getText());
			} 
			catch (IOException e1) 
			{
				MessageBox.showExceptionDialog(Errors.FIREFOX, e1);
			}
			firefoxField.setText("");
		});

		Button addButton = new Button("", Icons.createAddIcon());
		addButton.setTooltip(new Tooltip("Add a new OR to this list"));
		addButton.setOnAction(e -> new AddORDialog());

		Button refreshButton = new Button("", Icons.createRefreshIcon());
		refreshButton.setTooltip(new Tooltip("Refresh the table"));
		refreshButton.setOnAction(e -> GuiMethods.requestRefreshOfORsInTable());

		Button ORsButton = new Button("Get ORs", Icons.createListIcon());
		ORsButton.setOnAction(e -> new GetORsDialog());		
		
		GuiStyler.styleGraphicButton(addButton, Sizes.BUTTON_WIDTH_SMALL);
		GuiStyler.styleGraphicButton(firefoxButton, Sizes.BUTTON_WIDTH_SMALL);
		GuiStyler.styleGraphicButton(refreshButton, Sizes.BUTTON_WIDTH_SMALL);
		GuiStyler.styleGraphicButton(ORsButton, Sizes.BUTTON_WIDTH_MEDIUM);
		GuiStyler.styleTextField(filterField, Sizes.INPUT_WIDTH_X_LARGE, fieldHeight);
		GuiStyler.styleTextField(firefoxField, Sizes.INPUT_WIDTH_LARGE, fieldHeight);
		
		firefoxField.setOnKeyPressed(e ->
		{
			if (e.getCode() == KeyCode.ENTER)
			{
				try 
				{
					Utilities.openORInFirefox(GuiConstants.BUGZILLA_URL, firefoxField.getText());
				} 
				catch (IOException e1) 
				{
					MessageBox.showExceptionDialog(Errors.FIREFOX, e1);
				}
				firefoxField.setText("");
			}
		});
		
		navBar.getChildren().addAll(filterField, createRegion(50), firefoxField, firefoxButton, addButton, refreshButton, ORsButton, createRegion(50), pause);
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