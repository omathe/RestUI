package restui.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Response extends Message {

	private IntegerProperty status;

	public Response() {
		super();
	}
	
	public Response(final String body) {
		super(body);
		this.status = new SimpleIntegerProperty();
	}
	
	public Response(final String body, final Integer status) {
		super(body);
		this.status = new SimpleIntegerProperty(status);
	}

	public Integer getStatus() {
		return status.get();
	}
	
	public void setStatus(final Integer status) {
		
		this.status.set(status);
	}

	public void setBody(final Integer status) {
		this.status.set(status);
	}

	public IntegerProperty statusProperty() {
		return status;
	}
}
