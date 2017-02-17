package restui.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import restui.model.Application;

public class ApplicationService {

	private static final String APPLICATION_HOME = "restui";
	private static final String APPLICATION_DEFAULT_STYLE_DIRECTORY = "style/default";
	private static final String APPLICATION_DEFAULT_STYLE_IMAGES_DIRECTORY = "style/default/images";
	private static final String APPLICATION_DEFAULT_STYLE_SHEET = "stylesheet.css";
	private static final String APPLICATION_SETTINGS = "application.xml";

	public static Application openApplication() {

		createApplicationDefaultSettings();

		final Application application = new Application();

		final SAXBuilder sxb = new SAXBuilder();

		try {
			final Document document = sxb.build(ApplicationService.getApplicationHome() + File.separator + APPLICATION_SETTINGS);
			// application
			final Element applicationElement = document.getRootElement();
			final Element currentProjectElement = applicationElement.getChild("currentProject");
			application.setCurrentProject(currentProjectElement.getValue());

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

		final Element currentProjectElement = new Element("currentProject");
		currentProjectElement.setText(app.getCurrentProject());
		rootElement.addContent(currentProjectElement);

		final Element styleFileElement = new Element("styleFile");
		styleFileElement.setText(app.getStyleFile());
		rootElement.addContent(styleFileElement);

		final XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		final Document document = new Document(rootElement);
		final File projectFile = new File(ApplicationService.getApplicationHome() + File.separator + APPLICATION_SETTINGS);
		try {
			xmlOutputter.output(document, new FileOutputStream(projectFile));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static String getApplicationHome() {

		final String userHome = System.getProperty("user.home");
		return userHome + File.separator + getPrefix() + APPLICATION_HOME;
	}

	private static void createApplicationDefaultSettings() {

		// create application home directory
		final File applicationDirectory = new File(getApplicationHome());
		if (!applicationDirectory.exists()) {
			applicationDirectory.mkdir();
		}

		// create default style
		final File applicationDefaultStyle = new File(getApplicationHome() + File.separator + APPLICATION_DEFAULT_STYLE_DIRECTORY);
		if (!applicationDefaultStyle.exists()) {
			applicationDefaultStyle.mkdirs();
		}

		// copy stylesheet.css if not exists
		final URL url = ApplicationService.class.getResource("/" + APPLICATION_DEFAULT_STYLE_SHEET);
		final File stylesheet = Paths.get(applicationDefaultStyle.getAbsolutePath(), "/", APPLICATION_DEFAULT_STYLE_SHEET).toFile();
		if (!stylesheet.exists()) {
			try {
				Files.copy(Paths.get(url.toURI()), stylesheet.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}

		// create images directory if not exists
		final File imagesDirectory = new File(getApplicationHome() + File.separator + APPLICATION_DEFAULT_STYLE_IMAGES_DIRECTORY);
		if (!imagesDirectory.exists()) {
			imagesDirectory.mkdirs();
		}

		// copy all images if not exist
		final URL imagesUrl = ApplicationService.class.getResource("/images");
		try {
			for (final File image : Paths.get(imagesUrl.toURI()).toFile().listFiles()) {
				if (!Paths.get(imagesDirectory.getAbsolutePath(), image.getName()).toFile().exists()) {
					Files.copy(image.toPath(), Paths.get(imagesDirectory.getAbsolutePath(), image.getName()), StandardCopyOption.COPY_ATTRIBUTES);
				}
			}
		} catch (URISyntaxException | IOException e) {
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
