package restui.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RestUiApp extends Application {

	@Override
	public void start(final Stage primaryStage) throws Exception {

		final BorderPane root = FXMLLoader.load(getClass().getResource("/restui.fxml"));

		primaryStage.setTitle("RestUI");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public static void main(final String[] args) {
		launch(args);
	}

}
