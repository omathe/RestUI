package restui.conf;

public interface App {

	String TITLE = "RestUI";
	String MAIN_FXML = "/fxml/restui.fxml";
	String ICON = "/style/applicationIcon.png";
	String HOME = "restui";
	String FILE = getApplicationHome() + "/" + "application.xml";
	String DEFAULT_STYLE_URI = "file:/" + getApplicationHome() + "/style/default/stylesheet.css";

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
