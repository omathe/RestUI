package restui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EndPoint extends Item {

	private Path path;
	private StringProperty verb;
	private String method;
	private String version;

	public EndPoint(final String name, String verb) {
		super(name);
		this.verb = new SimpleStringProperty(verb);
	}
	
	public Path getPath() {
		return path;
	}

	public String getVerb() {
		return verb.get();
	}

	public void setVerb(String verb) {
		this.verb.set(verb);
	}

	public StringProperty verbProperty() {
		return verb;
	}
	
}
