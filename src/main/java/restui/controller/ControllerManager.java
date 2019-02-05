package restui.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class ControllerManager {

	public final static String REQUEST_BODY_FXML = "/fxml/requestBody.fxml";
	public final static String MAIN_FXML = "/fxml/restui.fxml";
	public final static String PROJECT_FXML = "/fxml/project.fxml";
	public final static String ENDPOINT_FXML = "/fxml/endpoint.fxml";

	private static FxmlNode mainFxmlNode;
	private static FxmlNode requestBodyFxmlNode;
	private static FxmlNode projectFxmlNode;
	private static FxmlNode endpointFxmlNode;

	public static FxmlNode loadMain() {

		if (mainFxmlNode == null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				Node node = fxmlLoader.load(ControllerManager.class.getResource(MAIN_FXML).openStream());
				MainController mainController = (MainController) fxmlLoader.getController();
				mainFxmlNode = new FxmlNode(node, mainController);
			} catch (IOException e) {
			}
		}
		return mainFxmlNode;
	}

	public static FxmlNode loadProject() {

		if (projectFxmlNode == null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				Node node = fxmlLoader.load(ControllerManager.class.getResource(PROJECT_FXML).openStream());
				ProjectController projectController = (ProjectController) fxmlLoader.getController();
				projectFxmlNode = new FxmlNode(node, projectController);
			} catch (IOException e) {
			}
		}
		return projectFxmlNode;
	}
	
	public static FxmlNode loadEndpoint() {
		
		if (endpointFxmlNode == null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				Node node = fxmlLoader.load(ControllerManager.class.getResource(ENDPOINT_FXML).openStream());
				EndpointController endpointController = (EndpointController) fxmlLoader.getController();
				endpointFxmlNode = new FxmlNode(node, endpointController);
			} catch (IOException e) {
			}
		}
		return endpointFxmlNode;
	}

	public static FxmlNode loadRequestBody() {

		if (requestBodyFxmlNode == null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				Node node = fxmlLoader.load(ControllerManager.class.getResource(REQUEST_BODY_FXML).openStream());
				RequestBodyController requestBodyController = (RequestBodyController) fxmlLoader.getController();
				requestBodyFxmlNode = new FxmlNode(node, requestBodyController);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return requestBodyFxmlNode;
	}

}
