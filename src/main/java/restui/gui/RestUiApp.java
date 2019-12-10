package restui.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import restui.conf.App;
import restui.controller.ControllerManager;
import restui.controller.MainController;

public class RestUiApp extends Application {

	private MainController controller;

	@Override
	public void start(final Stage primaryStage) throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader();
		final BorderPane root = fxmlLoader.load(ControllerManager.class.getResource(App.MAIN_FXML).openStream());
		controller = (MainController) fxmlLoader.getController();

		primaryStage.setTitle(App.TITLE);
		primaryStage.getIcons().add(new Image(ControllerManager.class.getResource(App.ICON).toString()));
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
