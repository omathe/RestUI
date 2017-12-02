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

	public static Set<String> locations = Arrays.stream(Location.values()).map(e -> e.name()).collect(Collectors.toSet());
	public static Set<String> types = Arrays.stream(Type.values()).map(e -> e.name()).collect(Collectors.toSet());
	public static Set<String> headerNames = Stream.of("Accept", "Authorization", "Content-Type").collect(Collectors.toSet());
	public static final String ID_PREFIX = "{";
	public static final String ID_SUFFIX = "}";

	public enum Location {
		BODY, HEADER, PATH, QUERY;
	}

	public enum Type {
		TEXT, FILE;
	}

	private final BooleanProperty enabled;
	private final StringProperty type;
	private final StringProperty location;
	private final StringProperty name;
	private final StringProperty value;

	public Parameter(final Boolean enabled, Type type, final Location location, final String name, final String value) {
		super();
		this.enabled = new SimpleBooleanProperty(enabled);
		this.type = new SimpleStringProperty(type.name());
		this.location = new SimpleStringProperty(location.name());
		this.name = new SimpleStringProperty(name);
		this.value = new SimpleStringProperty(value);
	}

	public Parameter(final Parameter parameter) {
		super();
		this.enabled = new SimpleBooleanProperty(parameter.getEnabled());
		this.type = new SimpleStringProperty(parameter.getType());
		this.location = new SimpleStringProperty(parameter.getLocation());
		this.name = new SimpleStringProperty(parameter.getName());
		this.value = new SimpleStringProperty(parameter.getValue());
	}

	public Boolean getEnabled() {
		return enabled.get();
	}

	public StringProperty typeProperty() {
		return type;
	}

	public String getType() {
		return type.get();
	}

	public void setType(final Type type) {
		this.type.set(type.name());
	}

	public void setEnabled(final Boolean enabled) {
		if (!isPathParameter()) {
			this.enabled.set(enabled);
		} else {
			this.enabled.set(true);
		}
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

	public boolean isPathParameter() {
		return location.get().equals(Location.PATH.name());
	}

	public boolean isQueryParameter() {
		return location.get().equals(Location.QUERY.name());
	}

	public boolean isBodyParameter() {
		return location.get().equals(Location.BODY.name()) && getName() != null && !getName().trim().isEmpty();
	}

	public boolean isRawBodyParameter() {
		return location.get().equals(Location.BODY.name()) && getName() == null;
	}

	public boolean isHeaderParameter() {
		return location.get().equals(Location.HEADER.name());
	}

	public boolean isTypeText() {
		return type.get().equals(Type.TEXT.name());
	}

	public boolean isTypeFile() {
		return type.get().equals(Type.FILE.name());
	}

	public boolean hasNameNullOrEmpty() {
		return name.get() == null || (name.get() != null && name.get().trim().isEmpty());
	}

	public boolean queryParameterValid() {
		if (!getLocation().equals(Location.QUERY.name())) {
			return true;
		}
		return getName() != null && !getName().trim().isEmpty() && getValue() != null && !getValue().trim().isEmpty();
	}

	public boolean isValid() {

		boolean valid = false;
		if (getLocation().equals(Location.QUERY.name()) || getLocation().equals(Location.HEADER.name())) {
			valid = getName() != null && !getName().trim().isEmpty() && getValue() != null && !getValue().trim().isEmpty();
		} else if (getLocation().equals(Location.PATH.name())) {
			valid = getEnabled() && getValue() != null && !getValue().trim().isEmpty();
		} else if (getLocation().equals(Location.BODY.name())) {
			valid = getValue() != null && !getValue().trim().isEmpty();
		}
		return valid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.get().hashCode());
		result = prime * result + ((name == null) ? 0 : name.get().hashCode());
		result = prime * result + ((type == null) ? 0 : type.get().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parameter other = (Parameter) obj;
		if (getLocation() == null) {
			if (other.getLocation() != null)
				return false;
		} else if (!getLocation().equals(other.getLocation()))
			return false;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		if (getType() == null) {
			if (other.getType() != null)
				return false;
		} else if (!getType().equals(other.getType()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Parameter [enabled=" + enabled.get() + ", type=" + type.get() + ", location=" + location.get() + ", name=" + name.get() + ", value=" + value.get() + "]";
	}

}
