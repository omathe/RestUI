package restui.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Response extends Message {

	private IntegerProperty status;

	public Response() {
		super();
	}
	
	public Response(final Integer status) {
		super();
		this.status = new SimpleIntegerProperty(status);
	}
	
	public Response(final String body, final Integer status) {
		super(body);
		this.status = new SimpleIntegerProperty(status);
	}

	public Integer getStatus() {
		return status == null ? null : status.get();
	}
	
	public void setStatus(final Integer status) {
		if (this.status == null) {
			this.status = new SimpleIntegerProperty(status);
		}else {
			this.status.set(status);
		}
	}

	public void setBody(final Integer status) {
		this.status.set(status);
	}

	public IntegerProperty statusProperty() {
		return status;
	}
}
