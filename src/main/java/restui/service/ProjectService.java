package restui.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
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
								String direction = parameterElement.getAttributeValue("direction");
								String location = parameterElement.getAttributeValue("location");
								String type = parameterElement.getAttributeValue("type");
								String parameterName = parameterElement.getAttributeValue("name");
								String value = parameterElement.getAttributeValue("value");
								Parameter parameter = new Parameter(Boolean.valueOf(enabled), Direction.valueOf(direction), Location.valueOf(location), Type.valueOf(type), parameterName, value);

								// add parameter to exchange only if endpoint contains it
								if (endpoint.containsParameter(parameter)) {
									exchange.addParameter(parameter);
								}
							}
							// retrieve the endpoint parameters that are not in the exchange
							endpoint.getParameters().stream().filter(p -> !exchange.containsParameter(p)).forEach(p -> exchange.addParameter(p));

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
	}

	private static void saveExchanges(final Project project, final File file) {
		// TODO
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

			// parameters
			if (endpoint.hasParameters()) {
				final Element elementParameters = new Element("parameters");
				for (final Parameter parameter : endpoint.getParameters()) {
					final Element elementParameter = new Element("parameter");
					elementParameter.setAttribute(new Attribute("enabled", parameter.getEnabled().toString()));
					elementParameter.setAttribute(new Attribute("direction", parameter.getDirection()));
					elementParameter.setAttribute(new Attribute("location", parameter.getLocation()));
					elementParameter.setAttribute(new Attribute("type", parameter.getType()));
					elementParameter.setAttribute(new Attribute("name", parameter.getName()));

					elementParameters.addContent(elementParameter);
				}
				element.addContent(elementParameters);
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

			// Parameters
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
