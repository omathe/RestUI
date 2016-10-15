package restui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Message {
    
    protected StringProperty body;
    
    public Message(final String body) {
		super();
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
}
