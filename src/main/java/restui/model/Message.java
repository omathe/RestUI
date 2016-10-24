package restui.model;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class Message {

	protected StringProperty body;
	protected List<Property> headers;
	protected List<Property> parameters;

	public Message() {
		super();
		this.headers = FXCollections.observableArrayList();
		this.parameters = FXCollections.observableArrayList();
	}

	public Message(final String body) {
		super();
		this.body = new SimpleStringProperty(body);
		this.headers = FXCollections.observableArrayList();
		this.parameters = FXCollections.observableArrayList();
	}

	public String getBody() {
		return body.get();
	}

	public void setBody(final String body) {
		this.body.set(body);
	}

	public StringProperty bodyProperty() {
		return body;
	}

	public List<Property> getHeaders() {
		return headers;
	}

	public void addHeader(final Property header) {

		if (headers == null) {
			this.headers = FXCollections.observableArrayList();
		}
		headers.add(header);
	}

	public void removeHeader(final Property header) {

		headers.remove(header);
	}
	
	public void addParameter(final Property parameter) {
		
		if (parameters == null) {
			this.parameters = FXCollections.observableArrayList();
		}
		parameters.add(parameter);
	}
	
	public void removeParameter(final Property parameter) {
		
		if (parameters == null) {
			this.parameters = FXCollections.observableArrayList();
		}
		parameters.remove(parameter);
	}
}
