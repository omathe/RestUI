package restui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Project extends Item {

	private StringProperty baseUrl;

	public Project() {
		super();
	}

	public Project(final String name, String baseUrl) {
		super(name);
		this.baseUrl = new SimpleStringProperty(baseUrl);
	}

	public String getBaseUrl() {
		return baseUrl.get();
	}

	public void setBaseUrl(String baseUrl) {
		if (this.baseUrl == null) {
			this.baseUrl = new SimpleStringProperty(baseUrl);
		}
		this.baseUrl.set(baseUrl);
	}

	public StringProperty baseUrlProperty() {
		return baseUrl;
	}
	
}
