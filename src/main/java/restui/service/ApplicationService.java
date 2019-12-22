package restui.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import restui.commons.ResourceHelper;
import restui.model.Application;
import restui.model.BaseUrl;

public class ApplicationService {

	private static final String APPLICATION_HOME = "restui";
	private static final String APPLICATION_FILE = getApplicationHome() + "/" + "application.xml";
	public static final String DEFAULT_STYLE_URI = "file:/" + getApplicationHome() + "/style/default/stylesheet.css";

	public static String getApplicationHome() {

		final String userHome = System.getProperty("user.home").replace("\\", "/");
		return userHome + "/" + getPrefix() + APPLICATION_HOME;
	}

	public static void init() {

		// create application home directory if not exists
		if (!applicationDirectory.exists()) {
			File applicationDirectory = new File(getApplicationHome());
			applicationDirectory.mkdir();
		}

		// create default style if not exists
		try {
			ResourceHelper.copyResource("/style", getApplicationHome());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// create application.xml if not exists
		File applicationFile = new File(APPLICATION_FILE);
		if (!applicationFile.exists()) {
			createDefaultApplicationFile();
		}
	}

	public static Application openApplication() {

		Application application = new Application();

		SAXBuilder saxBuilder = new SAXBuilder();

		try {
			Document document = saxBuilder.build(APPLICATION_FILE);
			Element applicationElement = document.getRootElement();
			Element lastProjectUriElement = applicationElement.getChild("lastProjectUri");
			application.setLastProjectUri(lastProjectUriElement.getValue());
			Element styleFileElement = applicationElement.getChild("styleFile");
			if (styleFileElement == null || styleFileElement.getValue().isEmpty()) {
				application.setStyleFile(DEFAULT_STYLE_URI);
			} else {
				application.setStyleFile(styleFileElement.getValue());
			}
			// baseUrls
			final Element elementBaseUrls = applicationElement.getChild("baseUrls");
			if (elementBaseUrls != null) {
				for (final Element elementBaseUrl : elementBaseUrls.getChildren()) {
					// BaseUrl
					final BaseUrl baseUrl = new BaseUrl(elementBaseUrl.getAttributeValue("name"), elementBaseUrl.getAttributeValue("url"), Boolean.valueOf(elementBaseUrl.getAttributeValue("enabled")));
					application.addBaseUrl(baseUrl);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return application;
	}

	public static void saveApplication(final Application application) {

		if (application != null) {
			Element rootElement = new Element("application");

			Element lastProjectUriElement = new Element("lastProjectUri");
			lastProjectUriElement.addContent(application.getLastProjectUri());
			rootElement.addContent(lastProjectUriElement);

			Element styleFileElement = new Element("styleFile");
			styleFileElement.addContent(application.getStyleFile());
			rootElement.addContent(styleFileElement);

			// base URLs
			if (!application.getBaseUrls().isEmpty()) {
				final Element elementBaseUrls = new Element("baseUrls");
				for (final BaseUrl baseUrl : application.getBaseUrls()) {
					final Element elementBaseUrl = new Element("baseUrl");
					elementBaseUrl.setAttribute(new Attribute("name", baseUrl.getName()));
					elementBaseUrl.setAttribute(new Attribute("url", baseUrl.getUrl()));
					elementBaseUrl.setAttribute(new Attribute("enabled", baseUrl.getEnabled().toString()));
					elementBaseUrls.addContent(elementBaseUrl);
				}
				rootElement.addContent(elementBaseUrls);
			}

			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
			Document document = new Document(rootElement);
			File applicationFile = new File(APPLICATION_FILE);
			try {
				xmlOutputter.output(document, new FileOutputStream(applicationFile));
			} catch (final IOException e) {
				e.printStackTrace();
			}
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

	private static void createDefaultApplicationFile() {

		Element rootElement = new Element("application");
		Element lastProjectUriElement = new Element("lastProjectUri");
		rootElement.addContent(lastProjectUriElement);
		Element styleFileElement = new Element("styleFile");
		styleFileElement.addContent(DEFAULT_STYLE_URI);
		rootElement.addContent(styleFileElement);

		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		Document document = new Document(rootElement);
		File applicationFile = new File(APPLICATION_FILE);
		try {
			xmlOutputter.output(document, new FileOutputStream(applicationFile));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
