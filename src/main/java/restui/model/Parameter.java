package restui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Parameter {

	private final StringProperty name;
	private final StringProperty value;
	private final StringProperty location;

	public Parameter(final String name, final String value, final String location) {
		super();
		this.name = new SimpleStringProperty(name);
		this.value = new SimpleStringProperty(value);
		this.location = new SimpleStringProperty(location);
	}

	public String getName() {
		return name.get();
	}

	public void setName(final String name) {
		this.name.set(name);
	}

	public StringProperty keyProperty() {
		return name;
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
