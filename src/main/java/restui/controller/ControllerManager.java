package restui.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import restui.conf.App;

public class ControllerManager {

	private static ProjectController projectController;
	private static EndpointController endpointController;
	private static MainController mainController;
	private static RequestBodyController requestBodyController;
	private static TestController testController;
	private static BottomController bottomController;

	public static MainController getMainController() {

		if (mainController == null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.load(ControllerManager.class.getResource(App.MAIN_FXML).openStream());
				mainController = (MainController) fxmlLoader.getController();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mainController;
	}

	public static ProjectController getProjectController() {

		if (projectController == null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.load(ControllerManager.class.getResource(App.PROJECT_FXML).openStream());
				projectController = (ProjectController) fxmlLoader.getController();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return projectController;
	}

	public static EndpointController getEndpointController() {

		if (endpointController == null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.load(ControllerManager.class.getResource(App.ENDPOINT_FXML).openStream());
				endpointController = (EndpointController) fxmlLoader.getController();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return endpointController;
	}

	public static RequestBodyController getRequestBodyController() {

		if (requestBodyController == null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.load(ControllerManager.class.getResource(App.REQUEST_BODY_FXML).openStream());
				requestBodyController = (RequestBodyController) fxmlLoader.getController();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return requestBodyController;
	}

	public static TestController getTestController() {

		if (testController == null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.load(ControllerManager.class.getResource(App.TEST_FXML).openStream());
				testController = (TestController) fxmlLoader.getController();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return testController;
	}
	
	public static BottomController getBottomController() {
		
		if (bottomController == null) {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.load(ControllerManager.class.getResource(App.BOTTOM_FXML).openStream());
				bottomController = (BottomController) fxmlLoader.getController();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bottomController;
	}

}
