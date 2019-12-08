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
import bugzilla.message.document.ReleaseNoteRequest;
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
import message.GuiMessageSender;
import theme.GuiStyler;
import theme.Sizes;

public class ReleaseNoteDialog extends VBox 
{
	private List<TextField> fields = new ArrayList<TextField>();
	
	public ReleaseNoteDialog(ObservableList<Bug> bugs)
	{
		Stage stage = new Stage();		
		
		TextField filename 			= createTextField("file name", "Document file name");		
		TextField releaseNumber 	= createTextField("release number", "Number of this release");
		TextField documentReference = createTextField("document reference", "The reference of the document (generated in the Access Database)");
		TextField issue 			= createTextField("issue", "E.g. A, B, 1.0, 1.1");
		TextField issueStatus 		= createTextField("issue status", "E.g. Draft, Definitive");		
		TextField saveLocation 		= createTextField("save location", "Where to save the file");
		
		Button directoryButton 	= new Button("...");
		Button generateButton 	= new Button("Generate");
				
		HBox save = new HBox(saveLocation, directoryButton);
		save.setAlignment(Pos.CENTER);
		save.setSpacing(20);
		
		ComboBox<String> classificationCombo 	= createComboBox(Arrays.asList("SECRET", "RESTRICTED", "UNCLASSIFIED"), 1);

		saveLocation.setMaxWidth(150);
		saveLocation.setEditable(false);

		directoryButton.setOnAction(e ->
		{
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Choose Save Location");
			chooser.setInitialDirectory(new File(Folders.DOCUMENTS_FOLDER));
			
			File selectedDir = chooser.showDialog(stage);
			saveLocation.setText(selectedDir.getAbsolutePath());
			saveLocation.setAlignment(Pos.CENTER_LEFT);
		});
		
		generateButton.setOnAction(e ->
		{
			for (TextField f : fields)
			{
				if (f.getText().isEmpty())
				{
					MessageBox.showDialog(Errors.MISSING_FIELD);
					return;
				}
			}
			
			String classification = classificationCombo.getSelectionModel().getSelectedItem();
			
			try
			{
				ReleaseNoteRequest request = new ReleaseNoteRequest.Builder().withClassification(classification)
																			.withDocumentNumber(documentReference.getText())
																			.withDocumentTitle(releaseNumber.getText() + " Release Note")
																			.withFilename(filename.getText())
																			.withIssue(issue.getText())
																			.withIssueStatus(issueStatus.getText())
																			.withBugs(bugs)
																			.build();
				new GuiMessageSender().sendRequestMessage(request);
			}
			catch(JsonTransformationException | MessageSenderException e1)
			{
				MessageBox.showExceptionDialog(Errors.GENERAL, e1);
			}
			
			stage.close();
		});		
		
		GuiStyler.stylePrimaryButton(directoryButton, Sizes.INPUT_HEIGHT_SMALL, Sizes.INPUT_HEIGHT_SMALL);
		GuiStyler.stylePrimaryButton(generateButton, Sizes.BUTTON_WIDTH_MEDIUM, Sizes.BUTTON_HEIGHT_SMALL);
		
		this.setSpacing(15);
		this.setPadding(new Insets(20));
		this.setAlignment(Pos.CENTER);
		this.setStyle("-fx-background-color: white");
		this.getChildren().addAll(filename, save, releaseNumber, documentReference, issue, issueStatus, classificationCombo, generateButton);
		
		stage.setScene(new Scene(WindowsBar.createWindowsBar(stage, this, "Create Release Note"), 300, 475));
		stage.show();
		stage.getIcons().add(Icons.createBugzillaIcon().getImage());
		stage.centerOnScreen();
	}
	
	private TextField createTextField(String placeholder, String tooltip)
	{
		TextField field = new TextField();
		field.setPromptText(placeholder);
		field.setTooltip(new Tooltip(tooltip));
		GuiStyler.styleTextField(field, Sizes.INPUT_WIDTH_LARGE, Sizes.INPUT_HEIGHT_SMALL);
		fields.add(field);
		return field;
	}
	
	private ComboBox<String> createComboBox(List<String> values, int selected)
	{
		ComboBox<String> combo = new ComboBox<String>();
		combo.getItems().addAll(values);
		combo.getSelectionModel().select(selected);
		GuiStyler.styleComboBox(combo);
		combo.setMinWidth(200);
		combo.setMaxWidth(200);
		return combo;
	}
}
