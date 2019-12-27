package fr.omathe.restui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.omathe.restui.model.Parameter.Direction;
import fr.omathe.restui.model.Parameter.Location;
import fr.omathe.restui.model.Parameter.Type;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 * A web service endpoint
 * @author Olivier MATHE
 */
public class Endpoint extends Item {

	private static final long serialVersionUID = 1L;

	private final StringProperty method;
	private final StringProperty path;
	private List<Exchange> exchanges;
	private final List<Parameter> parameters;
	private String description;

	public Endpoint(final Item parent, final String name, final String method) {
		super(parent, name);
		this.method = new SimpleStringProperty(method);
		this.path = new SimpleStringProperty();
		buildPath();
		this.exchanges = FXCollections.observableArrayList();
		this.parameters = FXCollections.observableArrayList();
		this.description = "";
	}

	public Endpoint(final String name, final String path, final String method) {
		super(null, name);
		this.path = new SimpleStringProperty(path);
		this.method = new SimpleStringProperty(method);
		this.exchanges = FXCollections.observableArrayList();
		this.parameters = FXCollections.observableArrayList();
		this.description = "";
	}

	public Endpoint(final String name, final String path, final String method, final String description) {
		this(name, path, method);
		this.description = description == null ? "" : description;
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

	public boolean hasParameters() {
		return parameters != null && !parameters.isEmpty();
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

	public String getRequestRawBody() {

		final Optional<String> body = parameters.stream()
				.filter(p -> p.isRequestParameter())
				.filter(p -> p.isRawBodyParameter())
				.filter(p -> p.getName() == null)
				.map(p -> p.getValue()).findFirst();

		return body.orElse(null);
	}

	public void setRequestRawBody(final String value) {

		final Optional<Parameter> rawBody = parameters.stream()
				.filter(p -> p.isRequestParameter())
				.filter(p -> p.isRawBodyParameter())
				.filter(p -> p.isTypeText())
				.filter(p -> p.getName() == null)
				.findFirst();
		if (rawBody.isPresent()) {
			if (value == null || value.isEmpty()) {
				parameters.remove(rawBody.get());
			} else {
				rawBody.get().setValue(value);
			}
		} else if (value != null && !value.isEmpty()) {
			final Parameter parameter = new Parameter(Boolean.TRUE, Direction.REQUEST, Location.BODY, Type.TEXT, null, value);
			addParameter(parameter);
		}
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void addParameter(final Parameter parameter) {

		if (parameters.contains(parameter)) {
			parameters.remove(parameter);
		}
		parameters.add(parameter);
	}

	public void removeParameters(final List<Parameter> parameters) {

		if (parameters != null) {
			this.parameters.removeAll(parameters);
		}
	}

	public Optional<Parameter> findParameter(final Parameter parameter) {

		return parameters.stream().filter(p -> p.equals(parameter)).findFirst();
	}

	public boolean containsParameter(final Parameter parameter) {

		return parameters.contains(parameter);
	}

	public Optional<Exchange> findExchangeByName(final String name) {
		return exchanges.stream().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst();
	}

	public List<String> getPaths() {

		List<String> paths = new ArrayList<>();
		if (path.get() != null && !path.get().isEmpty()) {
			String[] split = path.get().split("/");
			if (split != null && split.length > 0) {
				paths = Arrays.stream(split)
						.filter(path -> !path.isEmpty())
						.collect(Collectors.toList());
			}
		}
		return paths;
	}

	public String getDescription() {
		return description;
	}

}
