package restui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 * An endpoint is a web service item.
 * @author Olivier MATHE
 */
public class Endpoint extends Item {

	private static final long serialVersionUID = 1L;
	
	private final StringProperty method;
	private final StringProperty path;
	private List<Exchange> exchanges;

	public Endpoint(final Item parent, final String name, final String method) {
		super(parent, name);
		this.method = new SimpleStringProperty(method);
		this.path = new SimpleStringProperty();
		buildPath();
		this.exchanges = FXCollections.observableArrayList();
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
	
	public String getPath() {
		return path.get();
	}
	
	public void setPath(final String path) {
		this.path.set(path);
	}
	
	public StringProperty pathProperty() {
		return path;
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
	
	public boolean hasExchanges() {
		return exchanges != null && !exchanges.isEmpty();
	}
	
	public void buildPath() {

		final List<String> names = new ArrayList<>();
		Item currentItem = this;
		
		while (currentItem != null) {
			if (currentItem instanceof Path) {
				names.add(currentItem.getName());
			}
			currentItem = currentItem.getParent();
		}
		Collections.reverse(names);
		final String builtPath = "/" + names.stream().collect(Collectors.joining("/")).toString();
		path.set(builtPath);
	}
	
	public String getBaseUrl() {
		
		String baseUrl = null;
		Item currentItem = this;
		
		while (currentItem != null) {
			if (currentItem.getClass().getSimpleName().equals(Project.class.getSimpleName())) {
				final Project project = (Project) currentItem;
				baseUrl = project.getBaseUrl();
				break;
			}
			currentItem = currentItem.getParent();
		}
		return baseUrl;
	}
}
