package restui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import restui.conf.App;
import restui.controller.ControllerManager;
import restui.controller.MainController;

public class RestUiApp extends Application {

	@Override
	public void start(final Stage primaryStage) throws Exception {

		MainController mainController = ControllerManager.getMainController();
		primaryStage.setTitle(App.TITLE);
		primaryStage.getIcons().add(new Image(ControllerManager.class.getResource(App.ICON).toString()));
		primaryStage.setScene(new Scene(mainController.getRootNode()));

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

		ControllerManager.getMainController().exit(null);
	}

}
