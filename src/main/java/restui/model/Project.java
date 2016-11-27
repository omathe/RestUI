package restui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Project extends Item {

	private static final long serialVersionUID = 1L;
	
	private StringProperty baseUrl;

	public Project() {
		super();
		this.baseUrl = new SimpleStringProperty();
	}

	public Project(final Item parent, final String name, final String baseUrl) {
		super(parent, name);
		this.baseUrl = new SimpleStringProperty(baseUrl);
	}

	public String getBaseUrl() {
		return baseUrl.get();
	}

	public void setBaseUrl(final String baseUrl) {
		if (this.baseUrl == null) {
			this.baseUrl = new SimpleStringProperty(baseUrl);
		}
		this.baseUrl.set(baseUrl);
	}

	public StringProperty baseUrlProperty() {
		return baseUrl;
	}
	
}
