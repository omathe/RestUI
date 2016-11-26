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
	}

	public Message(final String body) {
		super();
		this.body = new SimpleStringProperty(body);
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
	
	public List<Parameter> getParameters() {
		return parameters;
	}

	public void addParameter(final Parameter parameter) {
		
		if (parameters == null) {
			parameters = FXCollections.observableArrayList();
		}
		if (!parameters.contains(parameter)) {
			parameters.add(parameter);
		}
	}
}
