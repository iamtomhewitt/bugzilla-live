package component.dialog.bug;

import common.utilities.Icons;
import component.WindowsBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GetBugsDialog extends VBox 
{
    public GetBugsDialog() 
    {
        Stage stage = new Stage();

        this.getChildren().add(new GetUserBugsDialog(stage).getVbox());
        this.getChildren().add(new Separator());

        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: white");

        stage.setScene(new Scene(WindowsBar.createWindowsBar(stage, this, "Get Bugs"), 300, 325));
        stage.show();
        stage.getIcons().add(Icons.createBugzillaIcon().getImage());
        stage.centerOnScreen();
    }
}