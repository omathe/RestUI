package restui.model;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class Message {

	protected StringProperty body;
	protected List<Parameter> parameters;

	public Message() {
		super();
		this.parameters = FXCollections.observableArrayList();
		this.body = new SimpleStringProperty();
	}

	public Message(final String body) {
		super();
		this.parameters = FXCollections.observableArrayList();
		this.body = new SimpleStringProperty(body);
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

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void addParameter(final Parameter parameter) {

		if (!parameters.contains(parameter)) {
			parameters.add(parameter);
		}
	}
}
