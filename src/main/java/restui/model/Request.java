package restui.model;

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Request extends Message {

	public enum BodyType {
		RAW, X_WWW_FORM_URL_ENCODED, FORM_DATA, BINARY;
	}

	private StringProperty uri;
	private BooleanProperty validUri;
	private BodyType bodyType;

	public Request() {
		super();
		this.uri = new SimpleStringProperty("");
		this.validUri = new SimpleBooleanProperty(false);
		this.bodyType = BodyType.RAW;
	}

	public Request(final BodyType bodyType, final String uri) {
		super();
		this.uri = new SimpleStringProperty(uri);
		this.validUri = new SimpleBooleanProperty(false);
		this.bodyType = bodyType;
	}

	public String getUri() {
		return uri.get();
	}

	public void setUri(final String uri) {
		this.uri.set(uri);
	}

	public void buildValuedUri(final String uri) {

		validUri.set(true);
		String valuedUri = new String(uri);
		Set<String> queryParams = new HashSet<String>();

		for (Parameter parameter : parameters) {
			if (parameter.isPathParameter()) {
				if (!parameter.getEnabled() || !parameter.isValid()) {
					validUri.set(false);
					continue;
				} else {
					valuedUri = valuedUri.replace(Parameter.ID_PREFIX + parameter.getName() + Parameter.ID_SUFFIX, parameter.getValue());
				}
			} else if (parameter.isQueryParameter()) {
				if (!parameter.isValid()) {
					validUri.set(false);
				} else if (parameter.getEnabled()) {
					queryParams.add(parameter.getName() + "=" + parameter.getValue());
				}
			}
		}
		if (!queryParams.isEmpty()) {
			valuedUri += "?" + String.join("&", queryParams);
		}
		System.out.println("valuedUri = " + valuedUri);
		this.uri.set(valuedUri);
	}

	public StringProperty uriProperty() {
		return uri;
	}

	public BodyType getBodyType() {
		return bodyType;
	}

	public void setBodyType(final BodyType bodyType) {
		this.bodyType = bodyType;
	}

	public String replaceIdentifiers(String uri) {
		String valuedUri = "";
		for (Parameter parameter : parameters) {
			if (parameter.isPathParameter()) {
				valuedUri = valuedUri.replace(Parameter.ID_PREFIX + parameter.getName() + Parameter.ID_SUFFIX, parameter.getValue());
			}
		}
		return valuedUri;
	}

	public Boolean getValidUri() {
		return validUri.get();
	}

	@Override
	public String toString() {
		return "Request [uri=" + uri + ", bodyType=" + bodyType + "]";
	}

}
