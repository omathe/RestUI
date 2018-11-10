package restui.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class ControllerManager {

	public final static String REQUEST_BODY_FXML = "/fxml/requestBody.fxml";

	private static FxmlNode requestBodyFxmlNode;

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
