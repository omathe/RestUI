package restui.model;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import restui.model.Parameter.Location;

public class Exchange {

	private StringProperty name;
	private ObjectProperty<Long> date;
	private Request request;
	private Response response;

	public Exchange() {
		super();
	}

	public Exchange(final String name, final Long date) {
		super();
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleObjectProperty<>(date);
		this.request = new Request();
		this.response = new Response();
	}

	public Exchange duplicate(final String name) {
		final Exchange duplicate = new Exchange(name, Instant.now().toEpochMilli());

		for (final Parameter parameter : this.getRequestParameters()) {
			final Parameter duplicateParameter = new Parameter(parameter);
			duplicate.addRequestParameter(duplicateParameter);
		}
		for (final Parameter header : this.getResponseHeaders()) {
			final Parameter duplicateParameter = new Parameter(header);
			duplicate.addResponseHeader(duplicateParameter);
		}
		duplicate.setRequestBody(this.getRequest().getRawBody());
		duplicate.setResponseBody(this.getResponseBody());

		return duplicate;
	}

	public String getName() {
		return name.get();
	}

	public void setName(final String name) {
		this.name.set(name);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public Integer getStatus() {
		return response.getStatus();
	}

	public Long getDate() {
		return date.get();
	}

	public void setDate(final Long date) {
		this.date.set(date);
	}

	public ObjectProperty<Long> dateProperty() {
		return date;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(final Request request) {
		this.request = request;
	}

	public List<Parameter> getRequestParameters() {
		return request.parameters;
	}

	public void addRequestParameter(final Parameter parameter) {
		request.addParameter(parameter);
	}

	public void removeRequestParameter(final Parameter parameter) {
		if (parameter != null) {
			request.parameters.remove(parameter);
		}
	}

	public void removeRequestParameters(final List<Parameter> parameters) {
		if (parameters != null) {
			request.parameters.removeAll(parameters);
		}
	}

	public void clearRequestParameters() {
		request.parameters.clear();
	}

	public List<Parameter> findParameters(final String location, final String name) {
		return request.parameters.stream().filter(parameter -> parameter.getLocation().equals(location) && parameter.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(final Response response) {
		this.response = response;
	}

	public List<Parameter> getResponseHeaders() {
		return response.parameters;
	}

	public void addResponseHeader(final Parameter parameter) {

		if (!response.parameters.contains(parameter)) {
			response.parameters.add(parameter);
		}
	}

	public void clearResponseHeaders() {
		response.parameters.clear();
	}

	public String getRequestBody() {
		return request.getRawBody();
	}

	public void setRequestBody(final String body) {
		request.setRawBody(body);
	}

	public String getResponseBody() {
		return response.getRawBody();
	}

	public void setResponseBody(final String body) {
		response.setRawBody(body);
	}

	public Optional<Parameter> findResponseHeader(final String name) {
		return response.findParameter(Location.HEADER, name);
	}

	public void setResponseStatus(Integer status) {
		response.setStatus(status);
	}

	public void setResponseDuration(Integer duration) {
		response.setDuration(duration);
	}

	@Override
	public String toString() {
		return "Exchange [name=" + name + ", date=" + date + ", request=" + request + ", response=" + response + "]";
	}

}
