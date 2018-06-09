package restui.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import restui.exception.NotFoundException;
import restui.model.BaseUrl;
import restui.model.Endpoint;
import restui.model.Exchange;
import restui.model.Exchange.BodyType;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Direction;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;
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
			project = new Project("");
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
			// load exchanges file
			loadExchanges(uri, project);
		}
		return project;
	}

	private static void loadExchanges(String uri, Project project) {

		final SAXBuilder sxb = new SAXBuilder();
		try {

			final Document document = sxb.build(buildExchangesUri(uri));

			// exchanges
			final Element exchangesElement = document.getRootElement();
			if (exchangesElement != null) {
				for (final Element endpointElement : exchangesElement.getChildren()) {
					// endpoints
					String endpointName = endpointElement.getAttributeValue("name");

					// searching the endpoint in the project
					Optional<Endpoint> optionalEndpoint = project.getAllChildren().filter(item -> item instanceof Endpoint && item.getName().equalsIgnoreCase(endpointName)).map(item -> (Endpoint) item).findFirst();

					if (optionalEndpoint.isPresent()) {
						Endpoint endpoint = optionalEndpoint.get();

						// exchanges of the enpoint
						for (final Element exchangeElement : endpointElement.getChildren()) {
							String name = exchangeElement.getAttributeValue("name");
							String date = exchangeElement.getAttributeValue("date");
							String requestBodyType = exchangeElement.getAttributeValue("requestBodyType");
							String status = exchangeElement.getAttributeValue("status");
							String duration = exchangeElement.getAttributeValue("duration");
							Exchange exchange = new Exchange(endpointName, name, Long.valueOf(date), Integer.valueOf(duration), Integer.valueOf(status), BodyType.valueOf(requestBodyType));
							for (final Element parameterElement : exchangeElement.getChildren()) {
								String enabled = parameterElement.getAttributeValue("enabled");
								String type = parameterElement.getAttributeValue("type");
								String location = parameterElement.getAttributeValue("location");
								String parameterName = parameterElement.getAttributeValue("location");
								String value = parameterElement.getAttributeValue("value");
								Parameter parameter = new Parameter(Boolean.valueOf(enabled), Type.valueOf(type), Location.valueOf(location), parameterName, value);

								if (endpoint.containsParameter(parameter)) {
									exchange.addParameter(parameter);
								}
							}
							if (!exchange.isEmpty()) {
								endpoint.addExchange(exchange);
							}
						}
					}
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}

		List<Exchange> exchanges = new ArrayList<>();

		Exchange e1 = new Exchange("getFleet", "OK", 1481616772605L, 82, 200, BodyType.RAW);

		// TODO Récupérer les paramètres du endpoint et mettre à jour sa value

		Parameter pe1 = new Parameter(true, Direction.REQUEST, Location.QUERY, Type.TEXT, "pageSize", "1");
		e1.addParameter(pe1);
		exchanges.add(e1);

		Exchange e2 = new Exchange("getCustomer", "OK", 1481616772605L, 82, 200, BodyType.RAW);
		Parameter pe2 = new Parameter(true, Direction.REQUEST, Location.QUERY, Type.TEXT, "fields", "name");
		e2.addParameter(pe2);
		exchanges.add(e2);
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
			// baseUrls
			if (!project.getBaseUrls().isEmpty()) {
				final Element elementBaseUrls = new Element("baseUrls");
				for (final BaseUrl baseUrl : project.getBaseUrls()) {
					final Element elementBaseUrl = new Element("baseUrl");
					elementBaseUrl.setAttribute(new Attribute("name", baseUrl.getName()));
					elementBaseUrl.setAttribute(new Attribute("url", baseUrl.getUrl()));
					elementBaseUrl.setAttribute(new Attribute("enabled", baseUrl.getEnabled().toString()));
					elementBaseUrls.addContent(elementBaseUrl);
				}
				element.addContent(elementBaseUrls);
			}
			element.setAttribute(attributeProjectName);
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
					elementExchange.setAttribute(new Attribute("name", exchange.getName()));
					elementExchange.setAttribute(new Attribute("date", exchange.getDate().toString()));
					elementExchanges.addContent(elementExchange);

					// request
					// final Request request = exchange.getRequest();FIXME 2.0
					Request request = new Request();
					final Element elementRequest = new Element("request");
					elementRequest.setAttribute(new Attribute("uri", request.getUri()));
					elementRequest.setAttribute(new Attribute("bodyType", request.getBodyType().name()));
					elementExchange.addContent(elementRequest);

					final Element elementRequestParameters = new Element("parameters");
					elementRequest.addContent(elementRequestParameters);
					for (final Parameter parameter : request.getParameters()) {
						final Element elementRequestParameter = new Element("parameter");
						if (parameter.getName() != null) {
							elementRequestParameter.setAttribute(new Attribute("name", parameter.getName()));
						}
						elementRequestParameter.setAttribute(new Attribute("enabled", parameter.getEnabled().toString()));
						elementRequestParameter.setAttribute(new Attribute("type", parameter.getType()));
						elementRequestParameter.setAttribute(new Attribute("location", parameter.getLocation()));
						elementRequestParameter.setAttribute(new Attribute("value", parameter.getValue()));
						elementRequestParameters.addContent(elementRequestParameter);
					}

					// response
					// final Response response = exchange.getResponse(); FIXME 2.0
					final Response response = new Response();
					final Element elementResponse = new Element("response");
					elementResponse.setAttribute(new Attribute("status", response.getStatus() == null ? "" : response.getStatus().toString()));
					elementResponse.setAttribute(new Attribute("duration", response.getDuration() == null ? "" : response.getDuration().toString()));
					elementExchange.addContent(elementResponse);

					final Element elementResponseParameters = new Element("parameters");
					elementResponse.addContent(elementResponseParameters);
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
						elementResponseParameters.addContent(elementResponseParameter);
					}
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
			final Project project = new Project("");
			project.setName(element.getAttributeValue("name"));
			// baseUrls
			final Element elementBaseUrls = element.getChild("baseUrls");
			if (elementBaseUrls != null) {
				for (final Element elementBaseUrl : elementBaseUrls.getChildren()) {
					// BaseUrl
					final BaseUrl baseUrl = new BaseUrl(elementBaseUrl.getAttributeValue("name"), elementBaseUrl.getAttributeValue("url"), Boolean.valueOf(elementBaseUrl.getAttributeValue("enabled")));
					project.addBaseUrl(baseUrl);
				}
			}
			return project;
		} else if (element.getName().equalsIgnoreCase(Path.class.getSimpleName())) {
			final Path path = new Path(parent, element.getAttributeValue("name"));
			parent.getChildren().add(path);
			return path;
		} else if (element.getName().equalsIgnoreCase(Endpoint.class.getSimpleName())) {
			// Endpoint
			final Endpoint endpoint = new Endpoint(parent, element.getAttributeValue("name"), element.getAttributeValue("method"));
			parent.getChildren().add(endpoint);

			// parameters
			final Element elementParameters = element.getChild("parameters");
			if (elementParameters != null) {
				for (final Element elementParameter : elementParameters.getChildren()) {
					if (elementParameter != null) {
						final Boolean enabled = Boolean.valueOf(elementParameter.getAttributeValue("enabled"));
						final Direction direction = Direction.valueOf(elementParameter.getAttributeValue("direction"));
						final Location location = Location.valueOf(elementParameter.getAttributeValue("location"));
						final Type type = Type.valueOf(elementParameter.getAttributeValue("type"));
						final String name = elementParameter.getAttributeValue("name");
						final Parameter parameter = new Parameter(enabled, direction, location, type, name, null);
						endpoint.addParameter(parameter);
					}
				}

			}

			/*
			 * FIXME 2.0 final Element elementExchanges = element.getChild("exchanges"); if
			 * (elementExchanges != null) { for (final Element elementExchange :
			 * elementExchanges.getChildren()) { // Exchange final Exchange exchange = new
			 * Exchange(elementExchange.getAttributeValue("name"),
			 * Long.valueOf(elementExchange.getAttributeValue("date")));
			 * endpoint.addExchange(exchange);
			 *
			 * // Request final Element elementRequest =
			 * elementExchange.getChild("request"); final Request request = new
			 * Request(BodyType.valueOf(elementRequest.getAttributeValue("bodyType")),
			 * elementRequest.getAttributeValue("uri")); //exchange.setRequest(request);
			 * FIXME 2.0
			 *
			 * // Request parameters final Element requestParameters =
			 * elementRequest.getChild("parameters"); if (requestParameters != null) { for
			 * (final Element elementParameter : requestParameters.getChildren()) { if
			 * (elementParameter != null) { final Boolean enabled =
			 * Boolean.valueOf(elementParameter.getAttributeValue("enabled")); final Type
			 * type = Type.valueOf(elementParameter.getAttributeValue("type")); final
			 * Location location =
			 * Location.valueOf(elementParameter.getAttributeValue("location")); final
			 * String name = elementParameter.getAttributeValue("name"); final String value
			 * = elementParameter.getAttributeValue("value"); final Parameter parameter =
			 * new Parameter(enabled, type, location, name, value);
			 * request.addParameter(parameter); } } } // Response final Element
			 * elementResponse = elementExchange.getChild("response"); final Response
			 * response = new
			 * Response(Integer.valueOf(elementResponse.getAttributeValue("status")),
			 * Integer.valueOf(elementResponse.getAttributeValue("duration")));
			 * //exchange.setResponse(response); FIXME 2.0
			 *
			 * // Response parameters final Element responseParameters =
			 * elementResponse.getChild("parameters"); if (responseParameters != null) { for
			 * (final Element elementParameter : responseParameters.getChildren()) { if
			 * (elementParameter != null) { final Boolean enabled =
			 * Boolean.valueOf(elementParameter.getAttributeValue("enabled")); final Type
			 * type = Type.valueOf(elementParameter.getAttributeValue("type")); final
			 * Location location =
			 * Location.valueOf(elementParameter.getAttributeValue("location")); final
			 * String name = elementParameter.getAttributeValue("name"); final String value
			 * = elementParameter.getAttributeValue("value"); final Parameter parameter =
			 * new Parameter(enabled, type, location, name, value);
			 * response.addParameter(parameter); } } } } }
			 */
			return endpoint;
		}
		return null;
	}

	public static String buildExchangesUri(String projectUri) {
		String exchangesUri = null;

		if (projectUri != null && !projectUri.isEmpty()) {
			int index = projectUri.lastIndexOf(File.separator);
			if (index != -1) {
				String path = projectUri.substring(0, index);
				String fileName = projectUri.substring(index + 1, projectUri.length());
				if (!fileName.isEmpty()) {
					String name = null;
					String extension = null;
					String[] split = fileName.split("\\.");
					if (split.length == 1) {
						name = split[0];
						extension = "";

					} else {
						name = split[0];
						extension = split[1];
					}
					String exchangeFileName = name + "-exchanges" + (extension.isEmpty() ? "" : "." + extension);
					exchangesUri = path + File.separator + exchangeFileName;
				}
			}
		}
		return exchangesUri;
	}

}
