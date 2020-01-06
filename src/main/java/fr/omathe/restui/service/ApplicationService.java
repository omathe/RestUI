package fr.omathe.restui.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import fr.omathe.restui.conf.App;
import fr.omathe.restui.model.Application;
import fr.omathe.restui.model.BaseUrl;

public class ApplicationService {

	/*public static void init() {

		// create application home directory if not exists
//		File applicationDirectory = new File(App.HOME);
//		if (!applicationDirectory.exists()) {
//			applicationDirectory.mkdir();
//		}
		
		// create default style if not exists
		try {
			ResourceHelper.copyResource(App.STYLE_LOCATION, App.HOME);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// create application.xml if not exists
		File applicationFile = new File(App.APLICATION_FILE);
		if (!applicationFile.exists()) {
			createDefaultApplicationFile();
		}
	}*/

	public static Application openApplication() {

		Application application = new Application();

		SAXBuilder saxBuilder = new SAXBuilder();

		try {
			Document document = saxBuilder.build(App.APLICATION_FILE);
			Element applicationElement = document.getRootElement();
			Element lastProjectUriElement = applicationElement.getChild("lastProjectUri");
			application.setLastProjectUri(lastProjectUriElement.getValue());
			Element styleFileElement = applicationElement.getChild("styleFile");
			if (styleFileElement == null || styleFileElement.getValue().isEmpty()) {
				application.setStyleFile(App.DEFAULT_STYLE_URI);
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
			File applicationFile = new File(App.APLICATION_FILE);
			//FileOutputStream fileOutputStream = null;
			try (FileOutputStream fileOutputStream = new FileOutputStream(applicationFile)) {
				xmlOutputter.output(document, fileOutputStream);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	static void createDefaultApplicationFile() {

		Element rootElement = new Element("application");
		Element lastProjectUriElement = new Element("lastProjectUri");
		rootElement.addContent(lastProjectUriElement);
		Element styleFileElement = new Element("styleFile");
		styleFileElement.addContent(App.DEFAULT_STYLE_URI);
		rootElement.addContent(styleFileElement);

		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		Document document = new Document(rootElement);
		File applicationFile = new File(App.APLICATION_FILE);
		
		if (!applicationFile.getParentFile().exists()) {
			applicationFile.getParentFile().mkdir();
		}
		
		try(FileOutputStream fos = new FileOutputStream(applicationFile)) {
			xmlOutputter.output(document, fos);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}