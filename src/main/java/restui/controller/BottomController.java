package restui.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import restui.model.AppVersion;
import restui.model.AppVersion.DateVersion;

public class BottomController implements Initializable {

	@FXML
	private Label file;
	@FXML
	private Label baseURL;
	@FXML
	public Label notification;
	@FXML
	private Label version;
	@FXML
	private Label time;
	@FXML
	private Label memory;

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		// version
		DateVersion dateVersion = AppVersion.getDateVersion();
		version.setText(dateVersion.version + " " + AppVersion.date(ZoneId.systemDefault().getId(), dateVersion.date));

		// base URL
		baseURL.setTooltip(new Tooltip(MainController.baseUrlProperty.get().urlProperty().get()));
		baseURL.textProperty().bind(MainController.baseUrlProperty.get().nameProperty());

		// time
		final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), event -> {
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
			time.setText(simpleDateFormat.format(Instant.now().toEpochMilli()));
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

		// memory usage
		final Timeline timelineMemory = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
			final Double mem = (double) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1_000_000);
			memory.setText(mem.toString() + " Mo");
		}));
		timelineMemory.setCycleCount(Animation.INDEFINITE);
		timelineMemory.play();
	}
	
	public void setFileName(final String name) {
		file.setText(name);
	}

}
