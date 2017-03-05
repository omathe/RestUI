package restui.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RestUiApp extends Application {

	@Override
	public void start(final Stage primaryStage) throws Exception {
		
		// start
//		final URL url = getClass().getClassLoader().getResource("style/default/stylesheet.css");
//		System.err.println("---------------------------------------- " + url);
		
		/*final File file = new File(url.getFile());
		System.err.println("file exists ? : " + file.exists());
		
		final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("stylesheet.css");
		System.err.println("inputStream : " + inputStream);
		
		final InputStream inputStream2 = getClass().getClassLoader().getResourceAsStream("images");
		System.err.println("inputStream2 : " + inputStream2);
		final URL url2 = getClass().getResource("/images");
		// jar:file:/media/DATA/dev/workspace/RestUI/build/libs/RestUI-1.0.jar!/images
		System.err.println("url2 : " + url2);
		

		final URL url3 = getClass().getResource("/fxml/restui.fxml");
		final File file3 = new File(url3.getFile());
		System.err.println("file3 exists ? : " + file3.exists());*/
		
		//final String path = "images/folder";
//		final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
//		System.err.println("jarFile : " + jarFile.getAbsolutePath());
//
//		ResourceHelper.copyResource("style/default/stylesheet.css", "/home/olivier/tmp/style.css");
		
		
		// end
		
		final BorderPane root = FXMLLoader.load(getClass().getResource("/fxml/restui.fxml"));

		primaryStage.setTitle("RestUI");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public static void main(final String[] args) {
		launch(args);
	}

}
