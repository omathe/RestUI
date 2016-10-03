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
	
	//private Project project;
	
	public TextField getBaseUrl() {
		return baseUrl;
	}
	
	public void setProject(Project project) {
		//this.project = project;
		baseUrl.textProperty().bindBidirectional(project.baseUrlProperty());
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
//		System.out.println("initialize");
//		System.out.println("baseUrl = " + baseUrl);
		
	}

}
