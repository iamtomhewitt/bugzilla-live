package component.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import bugzilla.common.Errors;
import bugzilla.common.Folders;
import bugzilla.common.MessageBox;
import bugzilla.common.bug.Bug;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.message.document.SubsystemTestRequest;
import bugzilla.utilities.Icons;
import component.WindowsBar;
import javafx.collections.ObservableList;
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

public class SubsystemTestDialog extends VBox
{
	private List<TextField> textFields = new ArrayList<TextField>();
	
	public SubsystemTestDialog(ObservableList<Bug> bugs)
	{
		TextField releaseNumberField 	= createTextField("release number", "E.g. 8.20.0.0", 450);
		TextField fileLocationField		= createTextField("\tfile location", "Location of saved file", 450);		
		TextField documentNumberField 	= createTextField("document reference", "Create a reference in the Skynet Access tool", 450);
		TextField issueField 			= createTextField("issue", "E.g. A, B, 1.0, 1.1", 450);
		TextField issueStatusField 		= createTextField("issue status", "E.g. Draft, Definitive", 450);
		TextField testEnvironmentField 	= createTextField("test environment", "E.g. Development, RSIF", 450);

		Button browseButton = new Button("...");
		Button generateButton = new Button("Generate");

		ComboBox<String> subsystem = createComboBox(Arrays.asList("CRM"), 0);
		ComboBox<String> classification = createComboBox(Arrays.asList("SECRET", "RESTRICTED", "UNCLASSIFIED"), 1);

		HBox fileLocationDetails = new HBox(fileLocationField, browseButton);
		fileLocationDetails.setSpacing(5);
		fileLocationDetails.setAlignment(Pos.CENTER);
		fileLocationDetails.setMinWidth(400);
		
		HBox comboBoxes = new HBox(subsystem, classification);
		comboBoxes.setSpacing(15);
		comboBoxes.setAlignment(Pos.CENTER);
		
		HBox buttons = new HBox(generateButton);
		buttons.setSpacing(10);
		buttons.setAlignment(Pos.CENTER);

		GuiStyler.stylePrimaryButton(generateButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiStyler.stylePrimaryButton(browseButton, Sizes.BUTTON_HEIGHT_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		
		fileLocationField.setMinWidth((450) - browseButton.getMinHeight() - fileLocationDetails.getSpacing());
		fileLocationField.setEditable(false);
		
		this.getChildren().addAll(releaseNumberField, fileLocationDetails, documentNumberField, issueField, issueStatusField, testEnvironmentField, comboBoxes, buttons);
		this.setSpacing(15);
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(20));
		this.setStyle("-fx-background-color: white");
		
		Stage stage = new Stage();
		stage.setScene(new Scene(WindowsBar.createWindowsBar(stage, this, "Subsystem Test"), 500, 425));
		stage.show();
		stage.setTitle("Subsystem Test");
		stage.getIcons().add(Icons.createBugzillaIcon().getImage());
		stage.centerOnScreen();
		
		browseButton.setOnAction(e ->
		{
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Choose Save Location");
			chooser.setInitialDirectory(new File(Folders.DOCUMENTS_FOLDER));
			
			File selectedDir = chooser.showDialog(stage);
			fileLocationField.setText(selectedDir.getAbsolutePath());
			fileLocationField.setAlignment(Pos.CENTER_LEFT);
		});
		
		generateButton.setOnAction(e ->
		{
			for (TextField f : textFields)
			{
				if (f.getText().isEmpty())
				{
					MessageBox.showDialog(Errors.MISSING_FIELD);
					return;
				}
			}
			
			String filename = documentNumberField.getText() + "A " + subsystem.getSelectionModel().getSelectedItem() + " " + releaseNumberField.getText() + " Subsystem Testing";
			
			try
			{
				SubsystemTestRequest request = new SubsystemTestRequest.Builder().withClassification(classification.getSelectionModel().getSelectedItem())
																		.withDeveloperUsername(GuiMethods.createDisplayName(GuiConstants.USERNAME))
																		.withDocumentNumber(documentNumberField.getText())
																		.withFileLocation(fileLocationField.getText())
																		.withFilename(filename)
																		.withIssue(issueField.getText())
																		.withIssueStatus(issueStatusField.getText())
																		.withBugs(bugs)
																		.withReleaseNumber(releaseNumberField.getText())
																		.withSubsystem(subsystem.getSelectionModel().getSelectedItem())
																		.withTestEnvironment(testEnvironmentField.getText())
																		.build();
				new GuiMessageSender().sendRequestMessage(request);
			}
			catch (JsonTransformationException | MessageSenderException e1)
			{
				MessageBox.showExceptionDialog(Errors.GENERAL, e1);
			}
			
			stage.close();
		});
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
