package fr.omathe.restui.controller;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ResourceBundle;

import fr.omathe.restui.service.tools.DateFormater;
import fr.omathe.restui.service.tools.StackTraceHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class LogsController implements Initializable {
	
	private enum Level {
		INFO, DEBUG, ERROR;
	}

	@FXML
	private VBox rootNode;

	@FXML
	private TextFlow logsFlow;
	
	@FXML
	private TextArea logsArea;

	public LogsController() {
		super();
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		logsArea.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; ");
	}

	public VBox getRootNode() {
		return rootNode;
	}

	public void logInfo(final String text) {

		logsArea.appendText(buildLog(text, Level.INFO));
	}
	
	public void logDebug(final String text) {
		
		logsArea.appendText(buildLog(text, Level.DEBUG));
	}
	
	public void logError(final Throwable throwable) {
		
		logsArea.appendText(buildLog(StackTraceHelper.toString(throwable), Level.ERROR));
	}
	
	public void logError(final String text) {
		
		logsArea.appendText(buildLog(text, Level.ERROR));
	}
	
	private String buildLog(final String text, final Level level) {
		
		return DateFormater.iso(Instant.now().toEpochMilli(), ZoneId.systemDefault().getId()) + " [" + level.name() + "] " + text + "\n";

	}
}
