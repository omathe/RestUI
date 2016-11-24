package restui.model;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Exchange {

	private StringProperty name;
	private LongProperty date;
	private Request request;
	private Response response;
	private IntegerProperty status;

	public Exchange() {
		super();
	}
	
	public Exchange(final String name, final Long date) {
		super();
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleLongProperty(date);
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
		if (status == null) {
			this.status = new SimpleIntegerProperty(status);
		}
		this.status.set(status);
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
	
	public LongProperty dateProperty() {
		return date;
	}

	public Request getRequest() {
		return request;
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

	public Integer getResponseStatus() {
		return response.getStatus();
	}

	public void setResponseStatus(final Integer status) {
		if (this.status == null) {
			this.status = new SimpleIntegerProperty();
			this.status.set(status);
		}
		response.setStatus(status);
	}
	
	public StringProperty getRequestBodyProperty() {
		return request.bodyProperty();
	}

	public void setRequestBody(final String body) {
		request.bodyProperty().set(body);
	}
	
	public void setResponseBody(final String body) {
		response.bodyProperty().set(body);
	}
	
	@Override
	public String toString() {
		return "Exchange [name=" + name + ", date=" + date + "]";
	}

}
