package restui.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Parameter {

	public enum Location {
		PATH, QUERY, HEADER;
	}

	public static Set<String> locations = Arrays.stream(Location.values()).map(e -> e.name()).collect(Collectors.toSet());
	public static Set<String> headerNames = Stream.of("Accept", "Authorization", "Content-Type").collect(Collectors.toSet());

	private final BooleanProperty enabled;
	private final StringProperty location;
	private final StringProperty name;
	private final StringProperty value;

	public Parameter(final Boolean enabled, final Location location, final String name, final String value) {
		super();
		this.enabled = new SimpleBooleanProperty(enabled);
		this.location = new SimpleStringProperty(location.name());
		this.name = new SimpleStringProperty(name);
		this.value = new SimpleStringProperty(value);
	}
	
	public Boolean getEnabled() {
		return enabled.get();
	}
	
	public void setEnabled(final Boolean enabled) {
		this.enabled.set(enabled);
	}
	
	public BooleanProperty enabledProperty() {
		return enabled;
	}
	
	public String getLocation() {
		return location.get();
	}

	public void setLocation(final Location location) {
		this.location.set(location.name());
	}

	public StringProperty locationProperty() {
		return location;
	}

	public String getName() {
		return name.get();
	}

	public void setName(final String name) {
		this.name.set(name);
	}

	public StringProperty nameProperty() {
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

}
