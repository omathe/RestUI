package restui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Request extends Message {

	private StringProperty uri;

	public Request() {
		super();
	}

	public Request(final String body, final String uri) {
		super(body);
		this.uri = new SimpleStringProperty(uri);
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
}
