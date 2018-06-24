package restui.model;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import restui.model.Parameter.Direction;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;

public class Exchange {

	public enum BodyType {
		RAW, X_WWW_FORM_URL_ENCODED, FORM_DATA;
	}

	private StringProperty name;
	private ObjectProperty<Long> date;
	// private Request request;
	// private Response response;
	private IntegerProperty status; // 2.0
	private IntegerProperty duration; // 2.0
	private BodyType requestBodyType;
	private StringProperty uri;
	// à supprimer private String endpointName;
	private List<Parameter> parameters;

	/*
	 * public Exchange() { super(); }
	 */

	public Exchange(final String name, final Long date) {
		super();
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleObjectProperty<>(date);
		this.status = new SimpleIntegerProperty();
		this.duration = new SimpleIntegerProperty();
		this.requestBodyType = BodyType.RAW;
		this.uri = new SimpleStringProperty("");
		this.parameters = FXCollections.observableArrayList();
	}

	public Exchange(String endpointName, final String name, final Long date, Integer duration, Integer status, BodyType requestBodyType) {
		super();
		// this.endpointName = endpointName;
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleObjectProperty<>(date);
		this.status = new SimpleIntegerProperty(status);
		this.duration = new SimpleIntegerProperty(duration);
		this.requestBodyType = requestBodyType;
		this.uri = new SimpleStringProperty("");
		this.parameters = FXCollections.observableArrayList();
	}

	public Exchange duplicate(final String name) {
		final Exchange duplicate = new Exchange(name, Instant.now().toEpochMilli());

		duplicate.addParameters(parameters);
		duplicate.setUri(uri.get());
		duplicate.setDuration(duration.get());
		duplicate.setStatus(status.get());

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

	public List<Parameter> getParameters() {

		return parameters;
	}

	public Optional<Parameter> findParameter(final Parameter parameter) {

		return parameters.stream().filter(p -> p.equals(parameter)).findFirst();
	}

	// public void addRequestParameter(final Parameter parameter) {
	// request.addParameter(parameter);
	// }

	// public void removeRequestParameter(final Parameter parameter) {
	// if (parameter != null) {
	// request.parameters.remove(parameter);
	// }
	// }

	public void removeParameters(final List<Parameter> parameters) {
		if (parameters != null) {
			parameters.removeAll(parameters);
		}
	}

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

	// public Response getResponse() {
	// return response;
	// }
	//
	// public void setResponse(final Response response) {
	// this.response = response;
	// }
	//
	// public List<Parameter> getResponseParameters() {
	// return response.parameters;
	// }
	//
	// public void addResponseParameter(final Parameter parameter) {
	// response.parameters.add(parameter);
	// }
	//
	// public void clearResponseParameters() {
	// response.parameters.clear();
	// }

	// public String getRequestBody() {
	// return request.getRawBody();
	// }

	// public void setRequestBody(final String body) {
	// request.setRawBody(body);
	// }

	// public String getResponseBody() {
	// return response.getRawBody();
	// }
	//

	public void setRequestRawBody(final String body) {

		final Optional<Parameter> rawBody = parameters.stream().filter(p -> p.isRequestParameter()).filter(p -> p.isRawBodyParameter()).filter(p -> p.isTypeText()).filter(p -> p.getName() == null).findFirst();
		if (rawBody.isPresent()) {
			rawBody.get().setValue(body);
		} else {
			final Parameter parameter = new Parameter(Boolean.TRUE, Direction.REQUEST, Location.BODY, Type.TEXT, null, body);
			addParameter(parameter);
		}
	}

	public String getRequestRawBody() {

		final Optional<String> body = parameters.stream().filter(p -> p.isRequestParameter() && p.isRawBodyParameter() && p.getName() == null).map(p -> p.getValue()).findFirst();

		return body.orElse(null);
	}

	public void setResponseBody(final String body) {

		final Optional<Parameter> rawBody = parameters.stream().filter(p -> p.getLocation().equals(Location.BODY.name())).filter(p -> p.getType().equals(Type.TEXT.name())).filter(p -> p.getName() == null).findFirst();
		if (rawBody.isPresent()) {
			rawBody.get().setValue(body);
		} else {
			final Parameter parameter = new Parameter(Boolean.TRUE, Direction.RESPONSE, Location.BODY, Type.TEXT, null, body);
			addParameter(parameter);
		}
	}

	public String getResponseBody() {

		String responseBody = "";
		final Optional<Parameter> rawBody = parameters.stream().filter(p -> p.getLocation().equals(Location.BODY.name())).filter(p -> p.getType().equals(Type.TEXT.name())).filter(p -> p.getName() == null).findFirst();
		if (rawBody.isPresent()) {
			responseBody = rawBody.get().getValue();
		}
		return responseBody;
	}

	public void clearResponseParameters() {
		parameters.removeIf(p -> p.isResponseParameter());
	}

	public Optional<Parameter> findParameter(Direction direction, final Location location, final String name) {

		return parameters.stream().filter(p -> p.getDirection().equals(direction.name()) && p.getLocation().equals(location.name()) && p.getName().equalsIgnoreCase(name)).findFirst();
	}

	//
	// public Optional<Parameter> findResponseHeader(final String name) {
	// return response.findParameter(Location.HEADER, name);
	// }
	//
	// public void setResponseStatus(Integer status) {
	// response.setStatus(status);
	// }
	//
	// public void setResponseDuration(Integer duration) {
	// response.setDuration(duration);
	// }

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

	// public String getEndpointName() {
	// return endpointName;
	// }
	//
	// public void setEndpointName(String endpointName) {
	// this.endpointName = endpointName;
	// }

	public void addParameter(final Parameter parameter) {

		if (!parameters.contains(parameter)) {
			parameters.add(parameter);
		}
	}

	public void addParameters(final List<Parameter> parameterList) {

		parameterList.stream().forEach(p -> {
			if (!parameters.contains(p)) {
				parameters.add(p);
			}
		});
	}

	public boolean isEmpty() {
		return parameters == null || parameters.isEmpty();
	}

	public boolean containsParameter(final Parameter parameter) {

		return parameters.contains(parameter);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exchange other = (Exchange) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.get().equalsIgnoreCase(other.name.get()))
			return false;
		return true;
	}



	// @Override
	// public String toString() {
	// return "Exchange [name=" + name + ", date=" + date + ", response=" + response
	// + ", status=" + status + ", duration=" + duration + ", requestBodyType=" +
	// requestBodyType + ", uri=" + uri + "]";
	// }

}
