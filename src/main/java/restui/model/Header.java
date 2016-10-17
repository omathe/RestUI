package restui.model;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Header {
	
	public static final String AUTHORIZATION = "Authorization";
	public static final String CONTENT_TYPE = "Content-Type";
	
	public static Set<String> headerNames = Stream.of(AUTHORIZATION, CONTENT_TYPE).collect(Collectors.toSet());
	
	private final StringProperty name;
	private final StringProperty value;

	public Header(final String name, final String value) {
		super();
		this.name = new SimpleStringProperty(name);
		this.value = new SimpleStringProperty(value);
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
