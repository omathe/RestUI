package restui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class RightController implements Initializable {

	@FXML
	private Label javaFxId;

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		javaFxId.setText("Hello RestUI !");

	}

	public String getText() {
		return javaFxId.getText();
	}

	public void setText(final String text) {
		javaFxId.setText(text);
	}

}
