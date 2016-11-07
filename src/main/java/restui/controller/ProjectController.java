package restui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import restui.model.Project;

public class ProjectController implements Initializable {

	@FXML
	private TextField baseUrl;
	
	public TextField getBaseUrl() {
		return baseUrl;
	}
	
	public void setProject(final Project project) {
		baseUrl.textProperty().bindBidirectional(project.baseUrlProperty());
		baseUrl.prefColumnCountProperty().bind(baseUrl.textProperty().length());
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		
	}

}
