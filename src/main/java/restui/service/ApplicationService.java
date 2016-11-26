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

import javafx.scene.control.TreeItem;
import restui.model.Endpoint;
import restui.model.Exchange;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Location;
import restui.model.Path;
import restui.model.Project;
import restui.model.Request;
import restui.model.Response;

public class ApplicationService {

	public static final String APPLICATION_HOME = ".restui";

	public static void saveProject(final Project project) {

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
					final Attribute attributeEndpointPath = new Attribute("path", endpoint.getPath());
					final Attribute attributeEndpointMethod = new Attribute("method", endpoint.getMethod());
					elementEndpoint.setAttribute(attributeEndpointName);
					elementEndpoint.setAttribute(attributeEndpointPath);
					elementEndpoint.setAttribute(attributeEndpointMethod);
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
							final Attribute attributeRequestUri = new Attribute("uri", request.getUri());
							elementRequest.setAttribute(attributeRequestUri);
							elementExchange.addContent(elementRequest);
							final Element elementRequestBody = new Element("body");
							elementRequestBody.addContent(request.getBody());
							elementRequest.addContent(elementRequestBody);
							final Element elementRequestParameters = new Element("parameters");
							elementRequest.addContent(elementRequestParameters);
							for (final Parameter parameter : request.getParameters()) {
								final Element elementRequestParameter = new Element("parameter");
								final Attribute attributeRequestParameterEnabled = new Attribute("enabled",
										parameter.getEnabled().toString());
								final Attribute attributeRequestParameterLocation = new Attribute("location",
										parameter.getLocation());
								final Attribute attributeRequestParameterName = new Attribute("name",
										parameter.getName());
								final Attribute attributeRequestParameterValue = new Attribute("value",
										parameter.getValue());
								elementRequestParameter.setAttribute(attributeRequestParameterEnabled);
								elementRequestParameter.setAttribute(attributeRequestParameterLocation);
								elementRequestParameter.setAttribute(attributeRequestParameterName);
								elementRequestParameter.setAttribute(attributeRequestParameterValue);
								elementRequestParameters.addContent(elementRequestParameter);
							}

							// response
							final Response response = exchange.getResponse();
							final Element elementResponse = new Element("response");
							final Attribute attributeResponseStatus = new Attribute("status",
									response.getStatus() == null ? "" : response.getStatus().toString());
							final Element elementResponseBody = new Element("body");
							elementResponseBody.addContent(response.getBody());
							elementResponse.addContent(elementResponseBody);
							elementResponse.setAttribute(attributeResponseStatus);
							final Element elementResponseHeaders = new Element("headers");
							elementResponse.addContent(elementResponseHeaders);
							for (final Parameter parameter : response.getParameters()) {
								final Element elementResponseHeader = new Element("header");
								final Attribute attributeResponsetHeaderName = new Attribute("name",
										parameter.getName());
								final Attribute attributeResponseHeaderValue = new Attribute("value",
										parameter.getValue());
								elementResponseHeader.setAttribute(attributeResponsetHeaderName);
								elementResponseHeader.setAttribute(attributeResponseHeaderValue);
								elementResponseHeaders.addContent(elementResponseHeader);
							}
							elementExchange.addContent(elementResponse);
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

	public static TreeItem<Item> openProject(final File file) {

		final Project project = new Project();
		final TreeItem<Item> projectItem = new TreeItem<>(project);

		final SAXBuilder sxb = new SAXBuilder();
		try {
			final Document document = sxb.build(file);
			// project
			final Element elementProject = document.getRootElement();
			project.setName(elementProject.getAttributeValue("name"));
			project.setBaseUrl(elementProject.getAttributeValue("baseUrl"));

			Element currentElement = elementProject;
			TreeItem<Item> currentItem = projectItem;

			while (!currentElement.getChildren().isEmpty()) {
				
				for (final Element elementChild : currentElement.getChildren()) {
					if (elementChild.getName().equalsIgnoreCase(Path.class.getSimpleName())) {
						// Path
						final Path path = new Path(elementChild.getAttributeValue("name"));
						final TreeItem<Item> childItem = new TreeItem<>(path);
						currentElement = elementChild;
						currentItem.getChildren().add(childItem);
						currentItem = childItem;
					} else if (elementChild.getName().equalsIgnoreCase(Endpoint.class.getSimpleName())) {
						// Endpoint
						final Endpoint endpoint = new Endpoint(elementChild.getAttributeValue("name"), elementChild.getAttributeValue("method"));
						final TreeItem<Item> childItem = new TreeItem<>(endpoint);
						currentItem.getChildren().add(childItem);
						currentElement = elementChild;
						currentItem = childItem;
						final Element elementExchanges = elementChild.getChild("exchanges");
						if (elementExchanges != null) {
							for (final Element elementExchange : elementExchanges.getChildren()) {
								// Exchange
								final Exchange exchange = new Exchange(elementExchange.getAttributeValue("name"),
										Long.valueOf(elementExchange.getAttributeValue("date")), Integer.valueOf(elementExchange.getAttributeValue("status")));
								endpoint.addExchange(exchange);
								// Request
								final Element elementRequest = elementExchange.getChild("request");
								final Element elementRequestBody = elementRequest.getChild("body");
								final Request request = new Request(elementRequestBody.getValue(), elementRequest.getAttributeValue("uri"));
								exchange.setRequest(request);
								// Request parameters
								final Element elementRequestParameters = elementRequest.getChild("parameters");
								for (final Element elementRequestParameter : elementRequestParameters.getChildren()) {
									if (elementRequestParameter != null) {
										final Boolean enabled = Boolean.valueOf(elementRequestParameter.getAttributeValue("enabled"));
										final Location location = Location.valueOf(elementRequestParameter.getAttributeValue("location"));
										final String name = elementRequestParameter.getAttributeValue("name");
										final String value = elementRequestParameter.getAttributeValue("value");
										final Parameter parameter = new Parameter(enabled, location, name, value);
										request.addParameter(parameter);
									}
								}
								// Response
								final Element elementResponse = elementExchange.getChild("response");
								final Element elementResponseBody = elementResponse.getChild("body");
								final Response response = new Response(elementResponseBody.getValue(), Integer.valueOf(elementResponse.getAttributeValue("status")));
								exchange.setResponse(response);
								// headers
								final Element elementResponseHeaders = elementResponse.getChild("headers");
								for (final Element elementResponseHeader : elementResponseHeaders.getChildren()) {
									if (elementResponseHeader != null) {
										final String name = elementResponseHeader.getAttributeValue("name");
										final String value = elementResponseHeader.getAttributeValue("value");
										final Parameter header = new Parameter(true, Location.HEADER, name, value);
										response.addParameter(header);
									}
								}
							}
						}
					} else {
						currentElement = elementChild;
						continue;
					}
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}

		return projectItem;
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
