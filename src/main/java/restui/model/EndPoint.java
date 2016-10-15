package restui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EndPoint extends Item {

	private Path path;
	private final StringProperty method;

	public EndPoint(final String name, final String method) {
		super(name);
		this.method = new SimpleStringProperty(method);
	}

	public Path getPath() {
		return path;
	}

	public String getMethod() {
		return method.get();
	}

	public void setMethod(final String method) {
		this.method.set(method);
	}

	public StringProperty methodProperty() {
		return method;
	}

}
