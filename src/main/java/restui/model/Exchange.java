package restui.model;

import java.time.Instant;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Exchange {

	public enum BodyType {
		RAW, X_WWW_FORM_URL_ENCODED, FORM_DATA;
	}

	private StringProperty name;
	private ObjectProperty<Long> date;
	// private Request request;
	//private Response response;
	private IntegerProperty status; // 2.0
	private IntegerProperty duration; // 2.0
	private BodyType requestBodyType;
	private StringProperty uri;
	private List<Value> values;

	public Exchange() {
		super();
	}

	public Exchange(final String name, final Long date) {
		super();
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleObjectProperty<>(date);
		// this.request = new Request();
//		this.response = new Response();
		this.status = new SimpleIntegerProperty();
		this.duration = new SimpleIntegerProperty();
		this.requestBodyType = BodyType.RAW;
		this.uri = new SimpleStringProperty("");
	}

	public Exchange duplicate(final String name) {
		final Exchange duplicate = new Exchange(name, Instant.now().toEpochMilli());

		/*
		 * for (final Parameter parameter : this.getRequestParameters()) { final
		 * Parameter duplicateParameter = new Parameter(parameter);
		 * duplicate.addRequestParameter(duplicateParameter); } for (final Parameter
		 * parameter : this.getResponseParameters()) { final Parameter
		 * duplicateParameter = new Parameter(parameter);
		 * duplicate.addResponseParameter(duplicateParameter); }
		 */
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

	public Long getDate() {
		return date.get();
	}

	public void setDate(final Long date) {
		this.date.set(date);
	}

	public ObjectProperty<Long> dateProperty() {
		return date;
	}

	// public Request getRequest() {
	// return request;
	// }

	// public void setRequest(final Request request) {
	// this.request = request;
	// }

	// public List<Parameter> getRequestParameters() {
	// return request.parameters;
	// }

	// public void addRequestParameter(final Parameter parameter) {
	// request.addParameter(parameter);
	// }

	// public void removeRequestParameter(final Parameter parameter) {
	// if (parameter != null) {
	// request.parameters.remove(parameter);
	// }
	// }

	// public void removeRequestParameters(final List<Parameter> parameters) {
	// if (parameters != null) {
	// request.parameters.removeAll(parameters);
	// }
	// }

	// public void clearRequestParameters() {
	// request.parameters.clear();
	// }
	//
	// public List<Parameter> findParameters(final String location, final String
	// name) {
	// return request.parameters.stream().filter(parameter ->
	// parameter.getLocation().equals(location) &&
	// parameter.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
	// }

//	public Response getResponse() {
//		return response;
//	}
//
//	public void setResponse(final Response response) {
//		this.response = response;
//	}
//
//	public List<Parameter> getResponseParameters() {
//		return response.parameters;
//	}
//
//	public void addResponseParameter(final Parameter parameter) {
//		response.parameters.add(parameter);
//	}
//
//	public void clearResponseParameters() {
//		response.parameters.clear();
//	}

	// public String getRequestBody() {
	// return request.getRawBody();
	// }

	// public void setRequestBody(final String body) {
	// request.setRawBody(body);
	// }

//	public String getResponseBody() {
//		return response.getRawBody();
//	}
//
//	public void setResponseBody(final String body) {
//		response.setRawBody(body);
//	}
//
//	public Optional<Parameter> findResponseHeader(final String name) {
//		return response.findParameter(Location.HEADER, name);
//	}
//
//	public void setResponseStatus(Integer status) {
//		response.setStatus(status);
//	}
//
//	public void setResponseDuration(Integer duration) {
//		response.setDuration(duration);
//	}

	public BodyType getRequestBodyType() {
		return requestBodyType;
	}

	public void setRequestBodyType(BodyType requestBodyType) {
		this.requestBodyType = requestBodyType;
	}

	public String getUri() {
		return uri.get();
	}

	public void setUri(final String uri) {
		this.uri.set(uri);
	}

	public StringProperty uriProperty() {
		return uri;
	}

	public IntegerProperty statusProperty() {
		return status;
	}

	public Integer getStatus() {
		return status.get();
	}

	public void setStatus(final Integer status) {
		this.status.set(status);
	}

	public IntegerProperty durationProperty() {
		return duration;
	}

	public Integer getDuration() {
		return duration.get();
	}

	public void setDuration(Integer duration) {
		this.duration.set(duration);
	}

//	@Override
//	public String toString() {
//		return "Exchange [name=" + name + ", date=" + date + ", response=" + response + ", status=" + status + ", duration=" + duration + ", requestBodyType=" + requestBodyType + ", uri=" + uri + "]";
//	}

}
