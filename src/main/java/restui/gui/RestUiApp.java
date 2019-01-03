package restui.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import restui.controller.ControllerManager;
import restui.controller.MainController;

public class RestUiApp extends Application {

	private MainController controller;

	@Override
	public void start(final Stage primaryStage) throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader();
		final BorderPane root = fxmlLoader.load(ControllerManager.class.getResource("/fxml/restui.fxml").openStream());
		controller = (MainController) fxmlLoader.getController();

		primaryStage.setTitle("RestUI");
//		primaryStage.getIcons().add(new Image("/style/applicationIcon.png")); // FIXME ne fonctionne plus avec les modules
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

		controller.exit(null);
	}

}
