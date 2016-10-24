package restui.model;

import java.util.List;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Exchange {

	private final StringProperty name;
	private final LongProperty date;
	private final Request request;
	private Response response;

	public Exchange(final String name, final Long date) {
		super();
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleLongProperty(date);
		this.request = new Request();
	}

	public Exchange(final String name, final Long date, final Request request, final Response response) {
		super();
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleLongProperty(date);
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
	
	public List<Property> getRequestHeaders() {
		return request.headers;
	}

	public void addRequestHeader(final Property header) {
		if (header != null) {
			request.headers.add(header);
		}
	}
	
	public void removeRequestHeader(final Property header) {
		if (header != null) {
			request.headers.remove(header);
		}
	}
	
	@Override
	public String toString() {
		return "Exchange [name=" + name + ", date=" + date + "]";
	}

}
