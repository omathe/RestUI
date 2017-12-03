package restui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Request extends Message {

	public enum BodyType {
		RAW, X_WWW_FORM_URL_ENCODED, FORM_DATA, BINARY;
	}

	private StringProperty uri;
	private BodyType bodyType;

	public Request() {
		super();
		this.uri = new SimpleStringProperty("");
		this.bodyType = BodyType.RAW;
	}

	public Request(final BodyType bodyType, final String uri) {
		super();
		this.uri = new SimpleStringProperty(uri);
		this.bodyType = bodyType;
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
				valuedUri = valuedUri.replace(Path.ID_PREFIX + parameter.getName() + Path.ID_SUFFIX, parameter.getValue());
			}
		}
		return valuedUri;
	}

	@Override
	public String toString() {
		return "Request [uri=" + uri.get() + ", bodyType=" + bodyType + "]";
	}

}
