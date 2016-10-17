package restui.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Response extends Message {

	private final IntegerProperty status;

	public Response(final String body, final Integer status) {
		super(body);
		this.status = new SimpleIntegerProperty(status);
	}

	public Integer getStatus() {
		return status.get();
	}

	public void setBody(final Integer status) {
		this.status.set(status);
	}

	public IntegerProperty statusProperty() {
		return status;
	}
}
