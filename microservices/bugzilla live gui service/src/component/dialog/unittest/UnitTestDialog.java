package component.dialog.unittest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import bugzilla.common.Errors;
import bugzilla.common.Folders;
import bugzilla.common.MessageBox;
import bugzilla.common.OR.OR;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.common.UnitTestStep;
import bugzilla.message.document.UnitTestRequest;
import bugzilla.utilities.Icons;
import bugzilla.utilities.Utilities;
import component.WindowsBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import common.GuiConstants;
import common.GuiMethods;
import message.GuiMessageSender;
import theme.GuiStyler;
import theme.Sizes;

public class UnitTestDialog extends VBox
{
	private Stage stage = new Stage();
	private UnitTestTable unitTable = new UnitTestTable();
	
	private List<TextField> textFields = new ArrayList<TextField>();

	public UnitTestDialog(OR or)
	{
		// Create file details
		TextField fileReferenceField = createTextField("file reference (e.g. 21-27-1350)", "Reference created in Skynet Access Database", 450);
		TextField fileLocationField = createTextField("\tfile location", "Location of saved file", 450);		
		TextField aim = createTextField("test aim", "Aim of the test", 450);
		TextField testEnvironment = createTextField("test environment", "E.g. Development, RSIF", 450);
		
		Button browseButton = new Button("...");
		Button addButton = new Button("Add Step");
		Button createButton = new Button("Create");

		fileLocationField.setEditable(false);
		fileLocationField.setText("\\\\skynet-fs\\Filing\\Segment\\Filing System\\21-Testing\\27 - Unit Testing");
		
		HBox fileLocationDetails = new HBox(fileLocationField, browseButton);
		fileLocationDetails.setSpacing(5);
		fileLocationDetails.setAlignment(Pos.CENTER);
		fileLocationDetails.setMinWidth(400);

		VBox fileDetails = new VBox(fileReferenceField, fileLocationDetails, aim, testEnvironment);
		fileDetails.setAlignment(Pos.CENTER);
		fileDetails.setSpacing(10);

		// Other details
		ComboBox<String> subsystem = createComboBox(Arrays.asList("APM", "CM", "CRM", "FM", "SM", "TMS", "INF"), 2);
		ComboBox<String> classification = createComboBox(Arrays.asList("SECRET", "RESTRICTED", "UNCLASSIFIED"), 1);

		HBox comboDetails = new HBox(subsystem, classification);
		comboDetails.setSpacing(5);
		comboDetails.setAlignment(Pos.CENTER);

		addButton.setOnAction(e -> unitTable.getData().add(new UnitTestStep(unitTable.getData().size() + 1 + ".", "Action", "Expected Result")));
		
		browseButton.setOnAction(e ->
		{
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Choose Save Location");
			chooser.setInitialDirectory(new File(Folders.DOCUMENTS_FOLDER));
			
			File selectedDir = chooser.showDialog(stage);
			fileLocationField.setText(selectedDir.getAbsolutePath());
			fileLocationField.setAlignment(Pos.CENTER_LEFT);
		});
		
		createButton.setOnAction(e ->
		{
			for (TextField f : textFields)
			{
				if (f.getText().isEmpty())
				{
					MessageBox.showDialog(Errors.MISSING_FIELD);
					return;
				}
			}
			
			String fileLocation = fileLocationField.getText();
			String filename = fileReferenceField.getText() + "A - Unit Test " + subsystem.getSelectionModel().getSelectedItem() + " - OR" + or.getNumber() + " - " + or.getSummary();
						
			try
			{
				UnitTestRequest request = new UnitTestRequest.Builder().withAim(aim.getText())
																		.withClassification(classification.getSelectionModel().getSelectedItem())
																		.withDeveloperUsername(GuiMethods.createDisplayName(GuiConstants.USERNAME))
																		.withDocumentTitle("OR" + or.getNumber() + " - " + or.getSummary())
																		.withFileLocation(fileLocation)
																		.withFilename(filename)
																		.withTestEnvironment(testEnvironment.getText())
																		.withTestSteps(unitTable.getData().stream().collect(Collectors.toList()))
																		.build();
				new GuiMessageSender().sendRequestMessage(request);
			}
			catch (JsonTransformationException | MessageSenderException e1)
			{
				MessageBox.showExceptionDialog(Errors.GENERAL, e1);
			}
			
			Utilities.copy(filename);
			stage.close();
		});

		HBox buttons = new HBox(addButton, createButton);
		buttons.setSpacing(10);
		buttons.setAlignment(Pos.CENTER);

		GuiStyler.stylePrimaryButton(addButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiStyler.stylePrimaryButton(createButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiStyler.stylePrimaryButton(browseButton, Sizes.BUTTON_HEIGHT_SMALL, Sizes.BUTTON_HEIGHT_SMALL);

		double x = (450) - browseButton.getMinHeight() - fileLocationDetails.getSpacing();
		fileLocationField.setMinWidth(x);

		this.getChildren().addAll(fileDetails, comboDetails, unitTable.getTableView(), buttons);
		this.setSpacing(15);
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(20));
		this.setStyle("-fx-background-color: white");
		
		String title = "Unit Test | OR" + or.getNumber() + " - ";
		title += (or.getSummary().length() > 30) ? or.getSummary().substring(0, 30) + "..." : or.getSummary();

		stage.setScene(new Scene(WindowsBar.createWindowsBar(stage, this, title), 500, 800));
		stage.show();
		stage.setTitle(title);
		stage.getIcons().add(Icons.createBugzillaIcon().getImage());
		stage.centerOnScreen();
	}

	private TextField createTextField(String placeholder, String tooltip, int width)
	{
		TextField field = new TextField();
		field.setPromptText(placeholder);
		field.setTooltip(new Tooltip(tooltip));
		textFields.add(field);
		
		GuiStyler.styleTextField(field, width, Sizes.INPUT_HEIGHT_SMALL);

		return field;
	}

	private ComboBox<String> createComboBox(List<String> values, int selected)
	{
		ComboBox<String> combo = new ComboBox<String>();
		combo.getItems().addAll(values);
		combo.getSelectionModel().select(selected);
		combo.setMinWidth(200);
		combo.setMaxWidth(200);
		
		GuiStyler.styleComboBox(combo);
		
		return combo;
	}
}
