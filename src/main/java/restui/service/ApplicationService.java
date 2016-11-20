package restui.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import restui.model.Endpoint;
import restui.model.Exchange;
import restui.model.Item;
import restui.model.Path;
import restui.model.Project;
import restui.model.Request;

public class ApplicationService {

	public static final String APPLICATION_HOME = ".restui";

	public static void saveProjectXml(final Project project) {

		// project
		final Element elementProject = new Element("project");
		final Attribute attributeProjectName = new Attribute("name", project.getName());
		final Attribute attributeProjectBaseUrl = new Attribute("baseUrl", project.getBaseUrl());
		elementProject.setAttribute(attributeProjectName);
		elementProject.setAttribute(attributeProjectBaseUrl);

		Item currentItem = project;
		Element currentElement = elementProject;
		while (currentItem.hasChildren()) {
			for (final Item child : currentItem.getChildren()) {
				if (child instanceof Path) {
					final Path path = (Path) child;
					final Element elementPath = new Element("path");
					final Attribute attributePathName = new Attribute("name", path.getName());
					elementPath.setAttribute(attributePathName);
					currentElement.addContent(elementPath);
					currentItem = path;
					currentElement = elementPath;
				}
				if (child instanceof Endpoint) {
					final Endpoint endpoint = (Endpoint) child;
					final Element elementEndpoint = new Element("endpoint");
					final Attribute attributeEndpointName = new Attribute("name", endpoint.getName());
					elementEndpoint.setAttribute(attributeEndpointName);
					currentElement.addContent(elementEndpoint);
					currentItem = endpoint;
					currentElement = elementEndpoint;
					// exchanges
					if (endpoint.hasExchanges()) {
						final Element elementExchanges = new Element("exchanges");
						for (final Exchange exchange : endpoint.getExchanges()) {
							final Element elementExchange = new Element("exchange");
							final Attribute attributeExchangeName = new Attribute("name", exchange.getName());
							final Attribute attributeExchangeDate = new Attribute("date",
									exchange.getDate().toString());
							final Attribute attributeExchangeStatus = new Attribute("status",
									exchange.getStatus() == null ? "" : exchange.getStatus().toString());
							elementExchange.setAttribute(attributeExchangeName);
							elementExchange.setAttribute(attributeExchangeDate);
							elementExchange.setAttribute(attributeExchangeStatus);
							elementExchanges.addContent(elementExchange);
							// request
							final Request request = exchange.getRequest();
							final Element elementRequest = new Element("request");
							elementExchange.addContent(elementRequest);
							final Element elementRequestBody = new Element("body");
							elementRequestBody.addContent(request.getBody());
							elementRequest.addContent(elementRequestBody);
							
						}
						elementEndpoint.addContent(elementExchanges);
					}
				}
			}
		}

		final XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
		final Document document = new Document(elementProject);

		final String userHome = System.getProperty("user.home");
		final File projectFile = new File(
				userHome + File.separator + APPLICATION_HOME + File.separator + project.getName() + ".xml");
		try {
			output.output(document, new FileOutputStream(projectFile));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveProject(final Project project) {

		createApplication();
		final String userHome = System.getProperty("user.home");
		final File projectFile = new File(
				userHome + File.separator + APPLICATION_HOME + File.separator + project.getName() + ".json");
		System.out.println("projectFile = " + projectFile.getAbsolutePath());

		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			mapper.writeValue(projectFile, project);

			final Project iot = mapper.readValue(projectFile, Project.class);
			System.out.println(iot.getName());
			System.out.println(iot.getBaseUrl());

		} catch (final JsonGenerationException e) {
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	public static Project openProject(final File file) {

		Project project = null;
		final ObjectMapper mapper = new ObjectMapper();

		try {
			project = mapper.readValue(file, Project.class);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return project;
	}

	public static void createApplication() {

		final String userHome = System.getProperty("user.home");
		final File applicationDirectory = new File(userHome + File.separator + APPLICATION_HOME);
		if (!applicationDirectory.exists()) {
			applicationDirectory.mkdir();
		}
	}

	public static String getHomeDirectory() {

		final String userHome = System.getProperty("user.home");
		return userHome + File.separator + APPLICATION_HOME;
	}

}
