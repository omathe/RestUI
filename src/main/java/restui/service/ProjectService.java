package restui.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import restui.exception.NotFoundException;
import restui.model.Endpoint;
import restui.model.Exchange;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Location;
import restui.model.Path;
import restui.model.Project;
import restui.model.Request;
import restui.model.Response;

public class ProjectService {

	public static Project openProject(final String uri) throws NotFoundException {
		
		Project project = null;
		if (uri != null && !uri.isEmpty()) {
			
			final File file = new File(URI.create(uri));
			if (!file.exists()) {
				throw new NotFoundException("file", file.getAbsolutePath());
			}
			
			project = new Project();

			final SAXBuilder sxb = new SAXBuilder();
			try {
				final Document document = sxb.build(uri);
				// project
				final Element element = document.getRootElement();
				project = (Project) buildItem(null, element);
				browseXml(project, element);

			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return project;
	}

	private static void browseXml(final Item item, final Element element) {

		for (final Element child : element.getChildren()) {
			final Item i = buildItem(item, child);
			browseXml(i, child);
		}
	}

	public static void saveProject(final Project project) {

		if (project != null) {
			final Element projectElement = buildElement(null, project);
			browseTree(project, projectElement);

			final XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
			final Document document = new Document(projectElement);

			final File projectFile = new File(ApplicationService.getApplicationHome() + File.separator + project.getName() + ".xml");
			try {
				output.output(document, new FileOutputStream(projectFile));
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void browseTree(final Item item, final Element element) {

		for (final Item child : item.getChildren()) {
			final Element e = buildElement(element, child);
			browseTree(child, e);
		}
	}

	private static Element buildElement(final Element parent, final Object object) {

		Element element = null;

		switch (object.getClass().getSimpleName()) {
		case "Project":
			final Project project = (Project) object;
			element = new Element("project");
			final Attribute attributeProjectName = new Attribute("name", project.getName());
			final Attribute attributeProjectBaseUrl = new Attribute("baseUrl", project.getBaseUrl());
			element.setAttribute(attributeProjectName);
			element.setAttribute(attributeProjectBaseUrl);
			break;
		case "Path":
			final Path path = (Path) object;
			element = new Element("path");
			final Attribute attributePathName = new Attribute("name", path.getName());
			element.setAttribute(attributePathName);
			parent.addContent(element);
			break;
		case "Endpoint":
			final Endpoint endpoint = (Endpoint) object;
			element = new Element("endpoint");
			final Attribute attributeEndpointName = new Attribute("name", endpoint.getName());
			final Attribute attributeEndpointPath = new Attribute("path", endpoint.getPath());
			final Attribute attributeEndpointMethod = new Attribute("method", endpoint.getMethod());
			element.setAttribute(attributeEndpointName);
			element.setAttribute(attributeEndpointPath);
			element.setAttribute(attributeEndpointMethod);
			// exchanges
			if (endpoint.hasExchanges()) {
				final Element elementExchanges = new Element("exchanges");
				for (final Exchange exchange : endpoint.getExchanges()) {
					final Element elementExchange = new Element("exchange");
					final Attribute attributeExchangeName = new Attribute("name", exchange.getName());
					final Attribute attributeExchangeDate = new Attribute("date", exchange.getDate().toString());
					final Attribute attributeExchangeStatus = new Attribute("status", exchange.getStatus() == null ? "" : exchange.getStatus().toString());
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
						final Attribute attributeRequestParameterEnabled = new Attribute("enabled", parameter.getEnabled().toString());
						final Attribute attributeRequestParameterLocation = new Attribute("location", parameter.getLocation());
						final Attribute attributeRequestParameterName = new Attribute("name", parameter.getName());
						final Attribute attributeRequestParameterValue = new Attribute("value", parameter.getValue());
						elementRequestParameter.setAttribute(attributeRequestParameterEnabled);
						elementRequestParameter.setAttribute(attributeRequestParameterLocation);
						elementRequestParameter.setAttribute(attributeRequestParameterName);
						elementRequestParameter.setAttribute(attributeRequestParameterValue);
						elementRequestParameters.addContent(elementRequestParameter);
					}

					// response
					final Response response = exchange.getResponse();
					final Element elementResponse = new Element("response");
					final Attribute attributeResponseStatus = new Attribute("status", response.getStatus() == null ? "" : response.getStatus().toString());
					final Element elementResponseBody = new Element("body");
					elementResponseBody.addContent(response.getBody());
					elementResponse.addContent(elementResponseBody);
					elementResponse.setAttribute(attributeResponseStatus);
					final Element elementResponseHeaders = new Element("headers");
					elementResponse.addContent(elementResponseHeaders);
					for (final Parameter parameter : response.getParameters()) {
						final Element elementResponseHeader = new Element("header");
						final Attribute attributeResponsetHeaderName = new Attribute("name", parameter.getName());
						final Attribute attributeResponseHeaderValue = new Attribute("value", parameter.getValue());
						elementResponseHeader.setAttribute(attributeResponsetHeaderName);
						elementResponseHeader.setAttribute(attributeResponseHeaderValue);
						elementResponseHeaders.addContent(elementResponseHeader);
					}
					elementExchange.addContent(elementResponse);
				}
				element.addContent(elementExchanges);
			}
			parent.addContent(element);
			break;

		default:
			break;
		}
		return element;
	}

	private static Item buildItem(final Item parent, final Element element) {

		// System.out.println("build item with element " + element.getName() + " and parent " + parent);

		if (element.getName().equalsIgnoreCase(Project.class.getSimpleName())) {
			final Project project = new Project();
			project.setName(element.getAttributeValue("name"));
			project.setBaseUrl(element.getAttributeValue("baseUrl"));
			return project;
		} else if (element.getName().equalsIgnoreCase(Path.class.getSimpleName())) {
			final Path path = new Path(parent, element.getAttributeValue("name"));
			parent.getChildren().add(path);
			return path;
		} else if (element.getName().equalsIgnoreCase(Endpoint.class.getSimpleName())) {
			final Endpoint endpoint = new Endpoint(parent, element.getAttributeValue("name"), element.getAttributeValue("method"));
			parent.getChildren().add(endpoint);

			final Element elementExchanges = element.getChild("exchanges");
			if (elementExchanges != null) {
				for (final Element elementExchange : elementExchanges.getChildren()) {
					// Exchange
					final Exchange exchange = new Exchange(elementExchange.getAttributeValue("name"), Long.valueOf(elementExchange.getAttributeValue("date")),
							Integer.valueOf(elementExchange.getAttributeValue("status")));
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
			return endpoint;
		}
		return null;
	}

}
