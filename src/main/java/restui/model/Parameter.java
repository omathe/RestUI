package restui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Parameter {

	private final StringProperty key;
	private final StringProperty value;
	private final StringProperty location;

	public Parameter(final String key, final String value, final String location) {
		super();
		this.key = new SimpleStringProperty(key);
		this.value = new SimpleStringProperty(value);
		this.location = new SimpleStringProperty(location);
	}

	public String getKey() {
		return key.get();
	}

	public void setKey(final String key) {
		this.key.set(key);
	}

	public StringProperty keyProperty() {
		return key;
	}

	public String getValue() {
		return value.get();
	}

	public void setValue(final String value) {
		this.value.set(value);
	}

	public StringProperty valueProperty() {
		return value;
	}

	public String getLocation() {
		return location.get();
	}

	public void setLocation(final String location) {
		this.value.set(location);
	}

	public StringProperty locationProperty() {
		return location;
	}

}
