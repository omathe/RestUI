package restui.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import restui.exception.NotFoundException;
import restui.model.BaseUrl;
import restui.model.Endpoint;
import restui.model.Item;
import restui.model.Parameter;
import restui.model.Parameter.Direction;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;
import restui.model.Path;
import restui.model.Project;

public interface ProjectService {

	/**
	 * Opens a project from an XML file
	 */
	static Project openProject(final URI uri) throws NotFoundException {

		Project project = new Project("");

		try {
			final File file = new File(uri);
			if (!file.exists()) {
				throw new NotFoundException("file", file.getAbsolutePath());
			}
			final SAXBuilder sxb = new SAXBuilder();

			final Document document = sxb.build(uri.toString());

			// project
			final Element elementProject = document.getRootElement();
			project.setName(elementProject.getAttributeValue("name"));

			// baseUrls
			final Element elementBaseUrls = elementProject.getChild("baseUrls");
			if (elementBaseUrls != null) {
				for (final Element elementBaseUrl : elementBaseUrls.getChildren()) {
					// BaseUrl
					final BaseUrl baseUrl = new BaseUrl(elementBaseUrl.getAttributeValue("name"), elementBaseUrl.getAttributeValue("url"), Boolean.valueOf(elementBaseUrl.getAttributeValue("enabled")));
					project.addBaseUrl(baseUrl);
				}
			}

			// endpoints
			final Element elementEndpoints = elementProject.getChild("endpoints");
			if (elementEndpoints != null) {
				for (final Element elementEndpoint : elementEndpoints.getChildren()) {
					final Endpoint endpoint = new Endpoint(elementEndpoint.getAttributeValue("name"), elementEndpoint.getAttributeValue("path"), elementEndpoint.getAttributeValue("method"));

					// Parameters
					final Element elementEndpointParameters = elementEndpoint.getChild("parameters");
					if (elementEndpointParameters != null) {
						for (final Element elementParameter : elementEndpointParameters.getChildren()) {
							if (elementParameter != null) {
								final Boolean enabled = Boolean.valueOf(elementParameter.getAttributeValue("enabled"));
								final Direction direction = Direction.valueOf(elementParameter.getAttributeValue("direction"));
								final Location location = Location.valueOf(elementParameter.getAttributeValue("location"));
								final Type type = Type.valueOf(elementParameter.getAttributeValue("type"));
								String attributeName = elementParameter.getAttributeValue("name");
								final String name = attributeName == null ? null : attributeName;
								final String value = elementParameter.getAttributeValue("value");
								final Parameter parameter = new Parameter(enabled, direction, location, type, name, value);
								endpoint.addParameter(parameter);
							}
						}
					}
					project.addEnpoint(endpoint);
				}
			}

		} catch (final NotFoundException e) {
			throw e;
		} catch (final Exception e) {
			e.printStackTrace();
		}

		// build the hierarchy
		buildHierarchy(project);

		// load exchanges
		ExchangesService.loadExchanges(buildExchangesUri(uri), project);

		return project;
	}

	/**
	 * Saves the project to an XML file
	 */
	static void saveProject(final Project project, final URI uri) {

		if (project != null) {

			// project
			final Element elementProject = new Element("project");
			final Attribute attributeProjectName = new Attribute("name", project.getName());
			elementProject.setAttribute(attributeProjectName);

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
				elementProject.addContent(elementBaseUrls);
			}

			// endpoints
			final Element elementEndpoints = new Element("endpoints");
			elementProject.addContent(elementEndpoints);

			project.getAllChildren().filter(item -> item instanceof Endpoint).forEach(item -> {
				Endpoint endpoint = (Endpoint) item;
				final Element elementEndpoint = new Element("endpoint");
				final Attribute attributeEndpointName = new Attribute("name", endpoint.getName());
				final Attribute attributeEndpointPath = new Attribute("path", endpoint.getPath());
				final Attribute attributeEndpointMethod = new Attribute("method", endpoint.getMethod());
				elementEndpoint.setAttribute(attributeEndpointName);
				elementEndpoint.setAttribute(attributeEndpointPath);
				elementEndpoint.setAttribute(attributeEndpointMethod);
				elementEndpoints.addContent(elementEndpoint);

				// parameters
				if (endpoint.hasParameters()) {
					final Element elementEndpointParameters = new Element("parameters");
					elementEndpoint.addContent(elementEndpointParameters);
					List<Parameter> parameters = endpoint.getParameters().stream()
							.filter(p -> p.isRequestParameter())
							.collect(Collectors.toList());
					for (final Parameter parameter : parameters) {
						final Element elementParameter = new Element("parameter");
						elementParameter.setAttribute(new Attribute("enabled", parameter.getEnabled().toString()));
						elementParameter.setAttribute(new Attribute("direction", parameter.getDirection()));
						elementParameter.setAttribute(new Attribute("location", parameter.getLocation()));
						elementParameter.setAttribute(new Attribute("type", parameter.getType()));
						if (parameter.getName() != null) {
							elementParameter.setAttribute(new Attribute("name", parameter.getName()));
						}
						if (parameter.getValue() != null) {
							elementParameter.setAttribute(new Attribute("value", parameter.getValue()));
						}
						elementEndpointParameters.addContent(elementParameter);
					}
				}
			});

			final XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
			final Document document = new Document(elementProject);

			try {
				xmlOutputter.output(document, new FileOutputStream(new File(uri)));

				// save the exchanges
				ExchangesService.saveExchanges(project, buildExchangesUri(uri));

			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void buildHierarchy(final Project project) {

		for (Endpoint endpoint : project.getEndpoints()) {

			List<String> paths = endpoint.getPaths();

			Item parent = project;
			for (String pathString : paths) {
				Optional<Item> optionalItem = parent.findChild(pathString);
				if (optionalItem.isPresent()) {
					parent = optionalItem.get();
				} else {
					// creates path
					Path path = new Path(parent, pathString);
					parent.getChildren().add(path);
					parent = path;
				}
			}
			// add endpoint
			parent.getChildren().add(endpoint);
		}

		// clear the list of enddpoints
		project.getEndpoints().clear();
	}

	private static URI buildExchangesUri(final URI projectUri) {
		String exchangesUri = null;

		String uri = projectUri.toString();
		int index = uri.lastIndexOf("/");
		if (index != -1) {
			String path = uri.substring(0, index);
			String fileName = uri.substring(index + 1, uri.length());
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
				exchangesUri = path + "/" + exchangeFileName;
			}
		}
		return URI.create(exchangesUri);
	}

}
