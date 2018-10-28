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

import restui.model.Endpoint;
import restui.model.Exchange;
import restui.model.Exchange.BodyType;
import restui.model.Parameter;
import restui.model.Parameter.Direction;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;
import restui.model.Project;

public class ExchangesService {

	public static void loadExchanges(URI uri, Project project) {

		final SAXBuilder sxb = new SAXBuilder();
		try {

			final Document document = sxb.build(uri.toString());

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
								// add request parameter to exchange only if the endpoint contains it
								if (direction.equalsIgnoreCase(Direction.REQUEST.name())) {
									if (endpoint.containsParameter(parameter)) {
										exchange.addParameter(parameter);
									}
								}
								else {
									// Response parameter
									exchange.addParameter(parameter);
								}
							}
							// retrieve the endpoint parameters that are not in the exchange
							endpoint.getParameters().stream().filter(p -> !exchange.containsParameter(p)).forEach(p -> exchange.addParameter(p.duplicate()));

							if (!exchange.isEmpty()) {
								endpoint.addExchange(exchange);
							}
							exchange.getParameters().stream().forEach(System.err::println);
						}
					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveExchanges(final Project project, final URI uri) {

		if (project != null) {
			Element root = new Element("exchanges");
			project.getAllChildren().filter(item -> item instanceof Endpoint)
				.map(item -> (Endpoint) item)
				.forEach(endpoint -> {

					// endpoint
					Element elementEndpoint = new Element("endpoint");
					elementEndpoint.setAttribute(new Attribute("name", endpoint.getName()));

					root.addContent(elementEndpoint);

					// exchanges
					endpoint.getExchanges().stream().forEach(exchange -> {
						Element elementExchange = new Element("exchange");
						elementExchange.setAttribute(new Attribute("name", exchange.getName()));
						elementExchange.setAttribute(new Attribute("date", exchange.getDate().toString()));
						elementExchange.setAttribute(new Attribute("requestBodyType", exchange.getRequestBodyType().name()));
						elementExchange.setAttribute(new Attribute("status", exchange.getStatus() == null ? "" : exchange.getStatus().toString()));
						elementExchange.setAttribute(new Attribute("duration", exchange.getDuration() == null ? "" : exchange.getDuration().toString()));

						elementEndpoint.addContent(elementExchange);

						// parameters
						exchange.getParameters().forEach(parameter -> {
							Element elementParameter = new Element("parameter");
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

							elementExchange.addContent(elementParameter);
						});

					});
				});
			final XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
			final Document document = new Document(root);

			try {
				xmlOutputter.output(document, new FileOutputStream(new File(uri)));
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

}
