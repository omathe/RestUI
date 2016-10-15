package restui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Exchange {

	private final StringProperty name;
	private final Long date;
	private final Request request;
	private final Response response;

	public Exchange(final String name, final Long date, final Request request, final Response response) {
		super();
		this.name = new SimpleStringProperty(name);
		this.date = date;
		this.request = request;
		this.response = response;
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
}
