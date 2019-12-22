package restui.conf;

public interface App {

	String TITLE = "RestUI";

	// FXML
	String MAIN_FXML = "/fxml/restui.fxml";
	String PROJECT_FXML = "/fxml/project.fxml";
	String ENDPOINT_FXML = "/fxml/endpoint.fxml";
	String REQUEST_BODY_FXML = "/fxml/requestBody.fxml";
	String TEST_FXML = "/fxml/test.fxml";

	String HOME = "restui";
	String APLICATION_FILE = getApplicationHome() + "/" + "application.xml";
	String STYLE_LOCATION = "/style";
	String ICON = STYLE_LOCATION + "/applicationIcon.png";
	String DEFAULT_STYLE_URI = "file:///" + getApplicationHome() + STYLE_LOCATION + "/default/stylesheet.css";
	String DARK_STYLE_URI = "file://" + getApplicationHome() + STYLE_LOCATION + "/dark/stylesheet.css";

	static String getApplicationHome() {

		final String userHome = System.getProperty("user.home").replace("\\", "/");
		return userHome + "/" + getPrefix() + App.HOME;
	}

	static String getPrefix() {

		String prefix = "";
		final String os = System.getProperty("os.name").toLowerCase();

		if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0) {
			// unix / linux os
			prefix = ".";
		}
		return prefix;
	}
}
