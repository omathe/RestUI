package restui.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Exchange {

	private final StringProperty name;
	private final LongProperty date;
	private Request request;
	private Response response;

	public Exchange(final String name, final Long date) {
		super();
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleLongProperty(date);
	}

	public Exchange(final String name, final Long date, final Request request, final Response response) {
		super();
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleLongProperty(date);
		;
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

	public LongProperty dateProperty() {
		return date;
	}

	@Override
	public String toString() {
		return "Exchange [name=" + name + ", date=" + date + "]";
	}

}
