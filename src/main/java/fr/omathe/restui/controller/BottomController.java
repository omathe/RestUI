package fr.omathe.restui.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ResourceBundle;

import fr.omathe.restui.model.Version;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BottomController implements Initializable {

	@FXML
	private Label file;
	@FXML
	private Label baseURL;
	@FXML
	private Label notification;
	@FXML
	private Label version;
	@FXML
	private Label time;
	@FXML
	private Label memory;

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		// version
		Version.load();
		version.setText(Version.getName() + " " + Version.getDate(ZoneId.systemDefault().getId()));

		// base URL
		baseURL.setTooltip(new Tooltip());
		baseURL.tooltipProperty().get().textProperty().bind(MainController.baseUrlProperty.get().urlProperty());
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
			memory.setStyle("-fx-background-color: lightgray ;");
			if (mem > 100) {
				memory.setStyle("-fx-background-color: orange ;");
			}
		}));
		timelineMemory.setCycleCount(Animation.INDEFINITE);
		timelineMemory.play();
	}

	public void setFileName(final String name) {
		file.setText(name);
	}

	public void setNotification(final String message, final Color color) {

		notification.setText(message);
		notification.setTextFill(color);
	}

}
