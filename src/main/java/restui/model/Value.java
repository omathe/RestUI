package restui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A parameter value
 * @author Olivier MATHE
 *
 */
public class Value {

	private StringProperty value;
	private Parameter parameter;
	private Exchange exchange;

	public Value(Parameter parameter, Exchange exchange, String value) {
		super();
		this.parameter = parameter;
		this.exchange = exchange;
		this.value = new SimpleStringProperty(value);
	}

	public Parameter getParameter() {
		return parameter;
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
