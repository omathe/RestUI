package restui.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import restui.model.Application;

public class ApplicationService {

	public static final String APPLICATION_HOME = "restui";
	public static final String APPLICATION_DEFAULT_STYLE_DIRECTORY = "style/default";
	public static final String APPLICATION_DEFAULT_STYLE_IMAGES_DIRECTORY = "style/default/images";
	public static final String APPLICATION_DEFAULT_STYLE_SHEET = "stylesheet.css";
	public static final String APPLICATION_SETTINGS = "application.xml";

	public static String getHomeDirectory() {

		final String userHome = System.getProperty("user.home");
		return userHome + File.separator + getPrefix() + APPLICATION_HOME;
	}

	public static void createApplicationDirectory() {

		final String userHome = System.getProperty("user.home");

		final File applicationDirectory = new File(userHome + File.separator + getPrefix() + APPLICATION_HOME);
		if (!applicationDirectory.exists()) {
			applicationDirectory.mkdir();
		}
	}

	public static void createApplicationDefaultSettings() {

		final File applicationDefaultStyle = new File(getHomeDirectory() + File.separator + APPLICATION_DEFAULT_STYLE_DIRECTORY);

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
		final File imagesDirectory = new File(getHomeDirectory() + File.separator + APPLICATION_DEFAULT_STYLE_IMAGES_DIRECTORY);
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

	public static Application openApplication() {

		final Application application = new Application();

		final SAXBuilder sxb = new SAXBuilder();

		try {
			final Document document = sxb.build(ApplicationService.getHomeDirectory() + File.separator + APPLICATION_SETTINGS);
			// application
			final Element applicationElement = document.getRootElement();
			final Element currentProjectElement = applicationElement.getChild("currentProject");
			application.setCurrentProject(currentProjectElement.getAttributeValue("name"));

			final Element styleDirectoryElement = applicationElement.getChild("styleDirectory");
			application.setStyleDirectory(styleDirectoryElement.getValue());
			
			final Element styleFileElement = applicationElement.getChild("styleFile");
			application.setStyleFile(styleFileElement.getValue());

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return application;
	}
}
