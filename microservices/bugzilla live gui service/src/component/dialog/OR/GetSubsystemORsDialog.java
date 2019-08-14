package component.dialog.OR;

import bugzilla.common.Errors;
import bugzilla.common.MessageBox;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.message.OR.SubsystemORsRequest;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import common.GuiConstants;
import common.GuiMethods;
import common.RequestType;
import message.GuiMessageSender;
import theme.GuiStyler;
import theme.Sizes;

public class GetSubsystemORsDialog 
{
	private Stage stage;		
	private HBox hbox = new HBox();
	private VBox vbox = new VBox();
	private Button getORsButton;
	
	public GetSubsystemORsDialog(Stage s)
	{	
		this.stage = s;
				
		ComboBox<String> subsystems = new ComboBox<String>();
		subsystems.getItems().addAll("APM", "CM", "CRM", "FM", "SM", "TMS", "INF", "SEGMENT");
		subsystems.getSelectionModel().select(2);
		
		getORsButton = new Button("Get ORs");
		getORsButton.setOnAction(e ->
		{
			GuiConstants.CURRENT_LIST_FILE = null;
			GuiConstants.REQUEST_TYPE = RequestType.SUBSYSTEM;
			
			String subsystem = subsystems.getSelectionModel().getSelectedItem();
			
			try
			{
				SubsystemORsRequest request = new SubsystemORsRequest(subsystem, GuiConstants.USERNAME, GuiConstants.PASSWORD, GuiConstants.APIKEY);
				new GuiMessageSender().sendRequestMessage(request);
			}
			catch (JsonTransformationException | MessageSenderException e1)
			{
				MessageBox.showExceptionDialog(Errors.GENERAL, e1);
			}
			
			GuiMethods.clearTable();
			stage.close();
		});
		
		hbox.getChildren().addAll(subsystems, getORsButton);
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(10);
		
		Label title = new Label("Subsystem ORs");
		
		GuiStyler.styleTitle(title);
		GuiStyler.stylePrimaryButton(getORsButton, Sizes.BUTTON_WIDTH_SMALL, Sizes.BUTTON_HEIGHT_SMALL);
		GuiStyler.styleComboBox(subsystems);
		
		vbox.getChildren().addAll(title, hbox);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
	}
	
	public VBox getVbox()
	{
		return vbox;
	}
	
	public Button getButton()
	{
		return getORsButton;
	}
}
