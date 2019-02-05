package restui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import restui.controller.ControllerManager;
import restui.controller.FxmlNode;
import restui.controller.MainController;

public class RestUiApp extends Application {

	private FxmlNode fxmlNode;

	@Override
	public void start(final Stage primaryStage) throws Exception {

		fxmlNode = ControllerManager.loadMain();
		BorderPane root = (BorderPane) fxmlNode.getNode();

		primaryStage.setTitle("RestUI");
		primaryStage.getIcons().add(new Image(ControllerManager.class.getResource("/style/applicationIcon.png").toString()));
		primaryStage.setScene(new Scene(root));

		primaryStage.setOnCloseRequest(e -> {
			e.consume();
			closeApplication();
		});

		primaryStage.show();
	}

	public static void main(final String[] args) {
		launch(args);
	}

	private void closeApplication() {

		MainController mainController = (MainController) fxmlNode.getController();
		mainController.exit(null);
	}

}
