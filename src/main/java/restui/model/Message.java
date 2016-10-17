package restui.model;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class Message {
    
    protected StringProperty body;
    protected List<Header> headers;
    
    public Message(final String body) {
		super();
		this.body = new SimpleStringProperty(body);
		this.headers = FXCollections.observableArrayList();
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
	
	public List<Header> getHeaders() {
		return headers;
	}
	
	public void addHeader(final Header header) {
		
		headers.add(header);
	}
	
	public void removeExchange(final Header header) {
		
		headers.remove(header);
	}
}
