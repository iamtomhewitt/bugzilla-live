package gui.app.component.dialog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import gui.app.common.GuiConstants;
import gui.app.component.WindowsBar;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import common.Errors;

import common.Fonts;
import common.MessageBox;
import common.utilities.Icons;

public class AboutDialog
{
	private Stage stage = new Stage();
	private VBox vbox = new VBox();

	public AboutDialog()
	{
		try
		{
			// TODO fetch this from Github releases page?
			
			ScrollPane scroll = new ScrollPane();

			String content = new String(Files.readAllBytes(Paths.get("release notes.json")));
			content = content.replaceAll("\\*", "\n\n");

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(content);
			JSONArray builds = (JSONArray) jsonObject.get("builds");

			for (int i = 0; i < builds.size(); i++)
			{
				JSONObject build = (JSONObject) builds.get(i);

				String version = build.get("version").toString();
				String date = build.get("date").toString();
				String title = build.get("title").toString();
				String description = build.get("description").toString();

				BorderPane border = new BorderPane();

				Label versionLabel = new Label(version);
				versionLabel.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, Fonts.FONT_SIZE_NORMAL));
				
				Label dateLabel = new Label(date);
				dateLabel.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, Fonts.FONT_SIZE_NORMAL));
				
				Label titleLabel = new Label(title);
				titleLabel.setWrapText(true);
				titleLabel.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, Fonts.FONT_SIZE_LARGE));
				
				Label descriptionLabel = new Label(description);
				descriptionLabel.setWrapText(true);
				descriptionLabel.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, Fonts.FONT_SIZE_NORMAL));
				autosize(descriptionLabel);

				Pane spacer = new Pane();
				HBox.setHgrow(spacer, Priority.ALWAYS);
				HBox buildData = new HBox(versionLabel, spacer, dateLabel);
				buildData.setMinHeight(20);
				buildData.setAlignment(Pos.CENTER);

				VBox buildInfo = new VBox(titleLabel, descriptionLabel);
				buildInfo.setPadding(new Insets(10));
				buildInfo.setSpacing(5);
				
				border.setTop(buildData);
				border.setCenter(buildInfo);
				border.getCenter().setStyle("-fx-background-color: white");
				border.getTop().setStyle("-fx-background-color: #f4f5f7");
				border.setStyle("-fx-border-color: #f4f5f7; -fx-border-width: 2;");
				
				vbox.getChildren().add(border);
			}

			vbox.setSpacing(25);
			vbox.setAlignment(Pos.CENTER);
			vbox.setStyle("-fx-background-color: #f4f5f7");
			vbox.setPadding(new Insets(10));
			
			scroll.setContent(vbox);
			scroll.setFitToWidth(true);
			scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
			scroll.setStyle("-fx-background-color: white");

			Scene scene = new Scene(WindowsBar.createWindowsBar(stage, scroll, "About | Version: " + GuiConstants.VERSION), 300, 500);
			stage.getIcons().add(Icons.createAboutIcon().getImage());
			stage.setTitle("About");
			stage.setScene(scene);
			stage.show();
			stage.centerOnScreen();
		} 
		catch (IOException | ParseException e)
		{
			MessageBox.showExceptionDialog(Errors.GENERAL, e);
		}
	}
	
	private void autosize(Label l)
	{
		// Autosize height
		new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				Node text = l.lookup(".text");
				if (text != null)
				{
					l.prefHeightProperty().bind(Bindings.createDoubleBinding(new Callable<Double>()
					{
						@Override
						public Double call() throws Exception
						{
							return text.getBoundsInLocal().getHeight();
						}
					}, text.boundsInLocalProperty()).add(20));
					this.stop();
				}
			}
		}.start();
	}
}
