package ui.component.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONObject;

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
import ui.component.WindowsBar;
import ui.theme.Fonts;
import ui.theme.Icons;
import common.error.Errors;
import common.error.RequestException;
import common.message.ApiRequestor;
import common.message.Endpoints;
import common.message.MessageBox;

public class AboutDialog
{
	public AboutDialog()
	{
		Stage stage = new Stage();
		ScrollPane scroll = new ScrollPane();
		VBox vbox = new VBox();
		
		String response;
		try
		{
			response = ApiRequestor.request(Endpoints.GITHUB_RELEASES);
		} 
		catch (RequestException e)
		{
			MessageBox.showExceptionDialog(Errors.REQUEST, e);
			return;
		}

		SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

		JSONArray json = new JSONArray(response);
		for (int i = 0; i < json.length(); i++)
		{
			JSONObject release = json.getJSONObject(i);

			String version = release.getString("tag_name");
			String publishedDate = release.getString("published_at");
			String title = release.getString("name");
			String description = release.getString("body");

			BorderPane border = new BorderPane();

			Label versionLabel = new Label(version);
			versionLabel.setFont(Font.font(Fonts.FONT, FontWeight.NORMAL, Fonts.FONT_SIZE_NORMAL));

			Date date = null;
			String formattedDate = "";

			try
			{
				date = input.parse(publishedDate);
				formattedDate = output.format(date);
			} 
			catch (ParseException e)
			{
				formattedDate = "Unknown";
			}

			Label dateLabel = new Label(formattedDate);
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

		Scene scene = new Scene(WindowsBar.createWindowsBar(stage, scroll, "About"), 300, 500);
		stage.getIcons().add(new Icons().createAboutIcon().getImage());
		stage.setTitle("About");
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
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
