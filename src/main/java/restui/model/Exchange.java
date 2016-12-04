package restui.model;

import java.util.List;
import java.util.Optional;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import restui.model.Parameter.Location;

public class Exchange {

	private StringProperty name;
	private ObjectProperty<Long> date;
	private Request request;
	private Response response;
	private IntegerProperty status;

	public Exchange() {
		super();
	}

	public Exchange(final String name, final Long date) {
		super();
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleObjectProperty<>(date);
		this.request = new Request("", "");
		this.response = new Response("");
		this.status = new SimpleIntegerProperty();
	}

	public Exchange(final String name, final Long date, final Integer status) {
		super();
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleObjectProperty<>(date);
		this.status = new SimpleIntegerProperty(status);
		this.request = new Request("", "");
		this.response = new Response("");
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
		return status == null ? null : status.get();
	}

	public void setStatus(final Integer status) {

		this.status.set(status);
		response.setStatus(status);
	}

	public IntegerProperty statusProperty() {
		return status;
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

		if (!request.parameters.contains(parameter)) {
			request.parameters.add(parameter);
		}
	}

	public void removeRequestParameter(final Parameter parameter) {
		request.parameters.remove(parameter);
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

	public StringProperty getRequestBodyProperty() {
		return request.bodyProperty();
	}

	public void setRequestBody(final String body) {
		request.bodyProperty().set(body);
	}

	public String getResponseBody() {
		return response.getBody();
	}

	public void setResponseBody(final String body) {
		response.bodyProperty().set(body);
	}

	public StringProperty getResponseBodyProperty() {
		return response.bodyProperty();
	}
	
	public Optional<Parameter> findResponseHeader(final String name) {
		return response.findParameter(Location.HEADER, name);
	}

	@Override
	public String toString() {
		return "Exchange [name=" + name + ", date=" + date + ", status=" + status.get() + "]";
	}

}
