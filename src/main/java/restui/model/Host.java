package restui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * An host is a name and an URL
 *
 * @author Olivier MATHE
 */
public class Host {

	private static final long serialVersionUID = 1L;

	private final StringProperty name;
	private final StringProperty url;

	public Host(final String name, final String url) {
		this.name = new SimpleStringProperty(name);
		this.url = new SimpleStringProperty(url);
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

	public String getUrl() {
		return url.get();
	}

	public void setUrl(final String url) {
		this.url.set(url);
	}

	public StringProperty urlProperty() {
		return url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Host other = (Host) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Host [name=" + name.get() + ", url=" + url.get() + "]";
	}

}
