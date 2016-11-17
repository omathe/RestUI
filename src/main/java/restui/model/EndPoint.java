package restui.model;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class EndPoint extends Item {

	private static final long serialVersionUID = 1L;
	
	private Path path;
	private final StringProperty method;
	private List<Exchange> exchanges;

	public EndPoint() {
		super();
		this.method = new SimpleStringProperty();
		this.exchanges = FXCollections.observableArrayList();
	}
	
	public EndPoint(final String name, final String method) {
		super(name);
		this.method = new SimpleStringProperty(method);
		this.exchanges = FXCollections.observableArrayList();
	}

	public Path getPath() {
		return path;
	}
	
	public void setPath(final Path path) {
		this.path = path;
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

	public void addExchange(final Exchange exchange) {
		
		exchanges.add(exchange);
	}
	
	public void removeExchange(final Exchange exchange) {
		
		exchanges.remove(exchange);
	}
	
	public List<Exchange> getExchanges() {
		return exchanges;
	}
	
	public void setExchanges(final List<Exchange> exchanges) {
		this.exchanges = exchanges;
	}
	
}
