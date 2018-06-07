package restui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;

/**
 * A web service endpoint
 * @author Olivier MATHE
 */
public class Endpoint extends Item {

	private static final long serialVersionUID = 1L;

	private final StringProperty method;
	private final StringProperty path;
	private List<Exchange> exchanges;
	private List<Parameter> parameters; // 2.0

	public Endpoint(final Item parent, final String name, final String method) {
		super(parent, name);
		this.method = new SimpleStringProperty(method);
		this.path = new SimpleStringProperty();
		buildPath();
		this.exchanges = FXCollections.observableArrayList();
		this.parameters = FXCollections.observableArrayList();
	}

	public String getMethod() {
		return method.get();
	}

	public void setMethod(final String method) {
		this.method.set(method);
	}

	public StringProperty methodProperty() {
		return method;
	}

	public String getPath() {
		return path.get();
	}

	public void setPath(final String path) {
		this.path.set(path);
	}

	public StringProperty pathProperty() {
		return path;
	}

	public void addExchange(final Exchange exchange) {

		exchanges.add(exchange);
	}

	public void removeExchange(final Exchange exchange) {

		exchanges.remove(exchange);
	}

	public List<Exchange> getExchanges() {
		return exchanges;
	}

	public void setExchanges(final List<Exchange> exchanges) {
		this.exchanges = exchanges;
	}

	public boolean hasExchanges() {
		return exchanges != null && !exchanges.isEmpty();
	}

	public void buildPath() {

		final List<String> names = new ArrayList<>();
		Item currentItem = this;

		while (currentItem != null) {
			if (currentItem instanceof Path) {
				names.add(currentItem.getName());
			}
			currentItem = currentItem.getParent();
		}
		Collections.reverse(names);
		final String builtPath = "/" + names.stream().collect(Collectors.joining("/")).toString();
		path.set(builtPath);
	}

	public String getBaseUrl() {

		String baseUrl = null;
		Item currentItem = this;

		while (currentItem != null) {
			if (currentItem.getClass().getSimpleName().equals(Project.class.getSimpleName())) {
				final Project project = (Project) currentItem;
				baseUrl = project.getBaseUrl();
				break;
			}
			currentItem = currentItem.getParent();
		}
		return baseUrl;
	}

	// 2.0
	public String getRawBody() {

		final Optional<String> body = parameters.stream()
				.filter(p -> p.getLocation().equals(Location.BODY.name()) && p.getName() == null)
				.map(p -> p.getValue())
				.findFirst();

		return body.orElse(null);
	}

	public void setRawBody(final String value) {

		final Optional<Parameter> rawBody = parameters.stream()
				.filter(p -> p.getLocation().equals(Location.BODY.name()))
				.filter(p -> p.getType().equals(Type.TEXT.name()))
				.filter(p -> p.getName() == null)
				.findFirst();
		if (rawBody.isPresent()) {
			rawBody.get().setValue(value);
		}
		else {
			final Parameter parameter = new Parameter(Boolean.TRUE, Type.TEXT, Location.BODY, null, value);
			addParameter(parameter);
		}
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void addParameter(final Parameter parameter) {

		if (!parameters.contains(parameter)) {
			parameters.add(parameter);
		}
	}

	// 2.0
	public Optional<Parameter> findParameter(final Location location, final String name) {

		return parameters.stream()
				.filter(p -> p.isHeaderParameter() && p.getName().equalsIgnoreCase(name))
				.findFirst();
	}

	public boolean containsParameter(final Parameter parameter) {

		return parameters.contains(parameter);
	}

}
