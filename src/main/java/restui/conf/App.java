package restui.conf;

public interface App {

	String TITLE = "RestUI";
	String MAIN_FXML = "/fxml/restui.fxml";
	String PROJECT_FXML = "/fxml/project.fxml";
	String ENDPOINT_FXML = "/fxml/endpoint.fxml";
	String REQUEST_BODY_FXML = "/fxml/requestBody.fxml";
	String TEST_FXML = "/fxml/test.fxml";
	String ICON = "/style/applicationIcon.png";
	String HOME = "restui";
	String FILE = getApplicationHome() + "/" + "application.xml";
	String DEFAULT_STYLE_URI = "file:/" + getApplicationHome() + "/style/default/stylesheet.css";
	String STYLE_LOCATION = "/style";

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
