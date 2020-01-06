package fr.omathe.restui.conf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface App {

	// application
	String TITLE = "RestUI";
	String HOME = ".restui";
	String APLICATION_FILE = getApplicationHome() + "/" + "application.xml";

	// FXML
	String FXML_LOCATION = "/fxml";
	String MAIN_FXML = FXML_LOCATION + "/restui.fxml";
	String PROJECT_FXML = FXML_LOCATION + "/project.fxml";
	String ENDPOINT_FXML = FXML_LOCATION + "/endpoint.fxml";
	String REQUEST_BODY_FXML = FXML_LOCATION + "/requestBody.fxml";
	String TEST_FXML = FXML_LOCATION + "/test.fxml";

	// style
	String STYLE_LOCATION = "/style";
	String APPLICATION_ICON = STYLE_LOCATION + "/applicationIcon.png";
//	String STYLE_DEFAULT = "DEFAULT";
//	String STYLE_DARK = "DARK";
	String DEFAULT_STYLE_URI = "file:///" + getApplicationHome() + STYLE_LOCATION + "/default/stylesheet.css";
	String DARK_STYLE_URI = "file:///" + getApplicationHome() + STYLE_LOCATION + "/dark/stylesheet.css";

	static String getApplicationHome() {

		final String userHome = System.getProperty("user.home").replace("\\", "/");
		return userHome + "/" + App.HOME;
	}

	static String getStyleUri(final String style) {
		String styleDir = style == null || style.isEmpty() ? "/default" : "/" + style;
		return "file:///" + getApplicationHome() + STYLE_LOCATION + styleDir + "/stylesheet.css";
	}
	
	static List<String> getStyles() {
		List<String> styles = new ArrayList<>();
		
		File styleDirectory = new File(getApplicationHome() + STYLE_LOCATION);
		if (styleDirectory.exists()) {
			File[] files = styleDirectory.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					styles.add(file.getName());
				}
			}
		}
		return styles;
	}
	
	/*static String getPrefix() {

		String prefix = "";
		final String os = System.getProperty("os.name").toLowerCase();

		if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0) {
			// unix / linux os
			prefix = ".";
		}
		return prefix;
	}*/
}
