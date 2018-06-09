package restui.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import restui.commons.ResourceHelper;
import restui.model.Application;

public class ApplicationService {

	private static final String APPLICATION_HOME = "restui";
	private static final String APPLICATION_XML = "application.xml";
	private static final String APPLICATION_DEFAULT_STYLE_DIRECTORY = "style/default";
	private static final String APPLICATION_DEFAULT_STYLE_SHEET = "stylesheet.css";

	public static String getApplicationHome() {

		final String userHome = System.getProperty("user.home");
		return userHome + File.separator + getPrefix() + APPLICATION_HOME;
	}

	public static Application openApplication() {

		createApplicationDefaultSettings();

		final Application application = new Application();

		final SAXBuilder sxb = new SAXBuilder();

		try {
			final Document document = sxb.build(ApplicationService.getApplicationHome() + File.separator + APPLICATION_XML);
			// application
			final Element applicationElement = document.getRootElement();
			final Element currentProjectElement = applicationElement.getChild("lastProjectUri");
			application.setLastProjectUri(currentProjectElement.getValue());

			final Element styleFileElement = applicationElement.getChild("styleFile");
			if (styleFileElement.getValue().isEmpty()) {
				application.setStyleFile("file:" + getApplicationHome() + File.separator + APPLICATION_DEFAULT_STYLE_DIRECTORY + File.separator + APPLICATION_DEFAULT_STYLE_SHEET);
			} else {
				application.setStyleFile(styleFileElement.getValue());
			}

		} catch (final FileNotFoundException e) {
			saveApplication(null);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return application;
	}

	public static void saveApplication(final Application application) {

		Application app = application;
		if (application == null) {
			app = new Application();
			app.setStyleFile(getApplicationHome() + "/style/default/stylesheet.css");
		}

		final Element rootElement = new Element("application");

		final Element currentProjectElement = new Element("lastProjectUri");
		currentProjectElement.addContent(app.getLastProjectUri());
		rootElement.addContent(currentProjectElement);

		final Element styleFileElement = new Element("styleFile");
		styleFileElement.addContent(app.getStyleFile());
		rootElement.addContent(styleFileElement);

		final XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		final Document document = new Document(rootElement);
		final File projectFile = new File(ApplicationService.getApplicationHome() + File.separator + APPLICATION_XML);
		try {
			xmlOutputter.output(document, new FileOutputStream(projectFile));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private static void createApplicationDefaultSettings() {

		// create application home directory
		final File applicationDirectory = new File(getApplicationHome());
		if (!applicationDirectory.exists()) {
			applicationDirectory.mkdir();
		}

		// copy stylesheet.css if not exists
		try {
			ResourceHelper.copyResource("/style", getApplicationHome());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static String getPrefix() {

		String prefix = "";
		final String os = System.getProperty("os.name").toLowerCase();

		if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0) {
			// unix / linux os
			prefix = ".";
		}
		return prefix;
	}

}
