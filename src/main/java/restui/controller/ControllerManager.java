package restui.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class ControllerManager {

	//private static FXMLLoader fxmlLoader = new FXMLLoader();
	//public static Map<String, Node> map = new HashMap<String, Node>();
	public final static String REQUEST_BODY_FXML = "/fxml/requestBody.fxml";


//	public static AnchorPane loadRequestBody() {
//
//		return (AnchorPane) loadNode(REQUEST_BODY_FXML);
//	}

	public static FxmlNode loadRequestBody() {

		FxmlNode fxmlNode = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			Node node = fxmlLoader.load(ControllerManager.class.getResource(REQUEST_BODY_FXML).openStream());
			RequestBodyController requestBodyController = (RequestBodyController) fxmlLoader.getController();
			fxmlNode = new FxmlNode(node, requestBodyController);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fxmlNode;
	}

	private static Node loadNode(String fxmlFile) {

		Node node = null;
		FXMLLoader fxmlLoader = new FXMLLoader();

		try {
			node = fxmlLoader.load(ControllerManager.class.getResource(fxmlFile).openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return node;
	}

	private static Object loadControler(String fxmlFile) {
		return fxmlFile;


	}


//	final FXMLLoader fxmlLoader = new FXMLLoader();
//	try {
//		final AnchorPane anchorPane = fxmlLoader.load(MainController.class.getResource("/fxml/requestBody.fxml").openStream());
//		final RequestBodyController requestBodyController = (RequestBodyController) fxmlLoader.getController();
//		requestBodyController.display(this, BodyType.RAW);
//
//
//		final Optional<Exchange> exchange = getSelectedExchange();
//		if (exchange.isPresent()) {
//			exchange.get().getRequest().setBodyType(Request.BodyType.RAW);
//			requestBodyController.display(this, BodyType.RAW);
//			if (!bodyVBox.getChildren().contains(anchorPane)) {
//				bodyVBox.getChildren().add(anchorPane);
//			}
//		}
//	} catch (final IOException e) {
//		e.printStackTrace();
//	}

}
