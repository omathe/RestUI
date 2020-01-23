package fr.omathe.restui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class LogsController implements Initializable {

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

		//logsArea.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; ");
	}

	public VBox getRootNode() {
		return rootNode;
	}

	public TextArea getLogsArea() {
		return logsArea;
	}

	@FXML
	protected void clear(final ActionEvent event) {
		logsArea.clear();
	}

}
