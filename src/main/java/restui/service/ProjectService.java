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
import restui.model.Parameter.Type;
import restui.model.Path;
import restui.model.Project;
import restui.model.Request;
import restui.model.Request.BodyType;
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

	public static void saveProject(final Project project, final File file) {

		if (project != null) {
			final Element projectElement = buildElement(null, project);
			browseTree(project, projectElement);

			final XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
			final Document document = new Document(projectElement);

			try {
				xmlOutputter.output(document, new FileOutputStream(file));
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

	// Object to XML
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
					final Attribute attributeRequestBodyType = new Attribute("bodyType", request.getBodyType().name());
					elementRequest.setAttribute(attributeRequestBodyType);
					elementExchange.addContent(elementRequest);
					//final Element elementRequestBody = new Element("body");
					//final Attribute attributeBodyType = new Attribute("type", request.getBodyType().name());
					//elementRequestBody.setAttribute(attributeBodyType);
					//elementRequestBody.addContent(request.getRawBody());
					//elementRequest.addContent(elementRequestBody);
					final Element elementRequestParameters = new Element("parameters");
					elementRequest.addContent(elementRequestParameters);
					for (final Parameter parameter : request.getParameters()) {
						final Element elementRequestParameter = new Element("parameter");
						final Attribute attributeRequestParameterEnabled = new Attribute("enabled", parameter.getEnabled().toString());
						final Attribute attributeRequestParameterType = new Attribute("type", parameter.getType());
						final Attribute attributeRequestParameterLocation = new Attribute("location", parameter.getLocation());
						if (parameter.getName() != null) {
							final Attribute attributeRequestParameterName = new Attribute("name", parameter.getName());
							elementRequestParameter.setAttribute(attributeRequestParameterName);
						}
						final Attribute attributeRequestParameterValue = new Attribute("value", parameter.getValue());
						elementRequestParameter.setAttribute(attributeRequestParameterEnabled);
						elementRequestParameter.setAttribute(attributeRequestParameterType);
						elementRequestParameter.setAttribute(attributeRequestParameterLocation);
						elementRequestParameter.setAttribute(attributeRequestParameterValue);
						elementRequestParameters.addContent(elementRequestParameter);
					}

					// response
					final Response response = exchange.getResponse();
					final Element elementResponse = new Element("response");
					final Attribute attributeResponseStatus = new Attribute("status", response.getStatus() == null ? "" : response.getStatus().toString());
					//final Element elementResponseBody = new Element("body");
					//elementResponseBody.addContent(response.getRawBody());
					//elementResponse.addContent(elementResponseBody);
					elementResponse.setAttribute(attributeResponseStatus);
					//final Element elementResponseHeaders = new Element("headers");
					//elementResponse.addContent(elementResponseHeaders);

					// response parameters
					for (final Parameter parameter : response.getParameters()) {
						final Element elementResponseParameter = new Element("parameter");
						elementResponseParameter.setAttribute(new Attribute("enabled", parameter.getEnabled().toString()));
						elementResponseParameter.setAttribute(new Attribute("type", parameter.getType()));
						elementResponseParameter.setAttribute(new Attribute("location", parameter.getLocation()));
						if (parameter.getName() != null) {
							elementResponseParameter.setAttribute(new Attribute("name", parameter.getName()));
						}
						elementResponseParameter.setAttribute(new Attribute("value", parameter.getValue()));
						elementRequestParameters.addContent(elementResponseParameter);
					}

					/*for (final Parameter parameter : response.getParameters()) {
						final Element elementResponseHeader = new Element("header");
						if (parameter.getName() != null) {
							final Attribute attributeResponsetHeaderName = new Attribute("name", parameter.getName());
							elementResponseHeader.setAttribute(attributeResponsetHeaderName);
						}
						if (parameter.getValue() != null) {
							final Attribute attributeResponseHeaderValue = new Attribute("value", parameter.getValue());
							elementResponseHeader.setAttribute(attributeResponseHeaderValue);
						}
						elementResponseHeaders.addContent(elementResponseHeader);
					}*/

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

	// XML to object
	private static Item buildItem(final Item parent, final Element element) {

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
					final Exchange exchange = new Exchange(elementExchange.getAttributeValue("name"), Long.valueOf(elementExchange.getAttributeValue("date")));
					endpoint.addExchange(exchange);

					// Request
					final Element elementRequest = elementExchange.getChild("request");
					final Request request = new Request(BodyType.valueOf(elementRequest.getAttributeValue("bodyType")), elementRequest.getAttributeValue("uri"));
					exchange.setRequest(request);

					// Request parameters
					final Element requestParameters = elementRequest.getChild("parameters");
					if (requestParameters != null) {
						for (final Element elementParameter : requestParameters.getChildren()) {
							if (elementParameter != null) {
								final Boolean enabled = Boolean.valueOf(elementParameter.getAttributeValue("enabled"));
								final Type type = Type.valueOf(elementParameter.getAttributeValue("type"));
								final Location location = Location.valueOf(elementParameter.getAttributeValue("location"));
								final String name = elementParameter.getAttributeValue("name");
								final String value = elementParameter.getAttributeValue("value");
								final Parameter parameter = new Parameter(enabled, type, location, name, value);
								request.addParameter(parameter);
							}
						}
					}
					// Response
					final Element elementResponse = elementExchange.getChild("response");
//					final Element elementResponseBody = elementResponse.getChild("body");
					final Response response = new Response(Integer.valueOf(elementResponse.getAttributeValue("status")));
					exchange.setResponse(response);


					// Response parameters
					final Element responseParameters = elementResponse.getChild("parameters");
					if (responseParameters != null) {
						for (final Element elementParameter : responseParameters.getChildren()) {
							if (elementParameter != null) {
								final Boolean enabled = Boolean.valueOf(elementParameter.getAttributeValue("enabled"));
								final Type type = Type.valueOf(elementParameter.getAttributeValue("type"));
								final Location location = Location.valueOf(elementParameter.getAttributeValue("location"));
								final String name = elementParameter.getAttributeValue("name");
								final String value = elementParameter.getAttributeValue("value");
								final Parameter parameter = new Parameter(enabled, type, location, name, value);
								response.addParameter(parameter);
							}
						}
					}

					/*
					// headers
					final Element elementResponseHeaders = elementResponse.getChild("headers");
					for (final Element elementResponseHeader : elementResponseHeaders.getChildren()) {
						if (elementResponseHeader != null) {
							final String name = elementResponseHeader.getAttributeValue("name");
							final String value = elementResponseHeader.getAttributeValue("value");
							final Parameter header = new Parameter(true, Location.HEADER, name, value);
							response.addParameter(header);
						}
					}*/
				}
			}
			return endpoint;
		}
		return null;
	}

}
