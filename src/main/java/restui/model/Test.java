package restui.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Test {

	private final BooleanProperty enabled;
	private final StringProperty webServiceName;
	private final StringProperty exchangeName;
	private final IntegerProperty status;
	private final IntegerProperty duration;

	public Test(final Boolean enabled, final String webServiceName, final String exchangeName, final Integer status, final Integer duration) {
		super();
		this.enabled = new SimpleBooleanProperty(enabled);
		this.webServiceName = new SimpleStringProperty(webServiceName);
		this.exchangeName = new SimpleStringProperty(exchangeName);
		this.status = new SimpleIntegerProperty(status);
		this.duration = new SimpleIntegerProperty(duration);
	}

	public Boolean getEnabled() {
		return enabled.get();
	}

	public void setEnabled(final Boolean enabled) {
		this.enabled.set(enabled);
	}

	public BooleanProperty enabledProperty() {
		return enabled;
	}

	public String getWebServiceName() {
		return webServiceName.get();
	}

	public void setWebServiceName(final String webServiceName) {
		this.webServiceName.set(webServiceName);
	}

	public StringProperty webServiceNameProperty() {
		return webServiceName;
	}

	public String getExchangeName() {
		return exchangeName.get();
	}

	public void setExchangeName(final String exchangeName) {
		this.exchangeName.set(exchangeName);
	}

	public StringProperty exchangeNameProperty() {
		return exchangeName;
	}

	public IntegerProperty getStatus() {
		return status;
	}

	public void setStatus(final Integer status) {
		this.status.set(status);
	}

	public IntegerProperty statusProperty() {
		return status;
	}

	public IntegerProperty getDuration() {
		return duration;
	}

	public void setDuration(final Integer duration) {
		this.duration.set(duration);
	}

	public IntegerProperty durationProperty() {
		return duration;
	}

}
