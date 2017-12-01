package restui.model;

import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import restui.model.Parameter.Location;
import restui.model.Parameter.Type;

public class Message {

//	protected BodyType bodyType;
	protected List<Parameter> parameters;

	public Message() {
		super();
		this.parameters = FXCollections.observableArrayList();
//		this.bodyType = BodyType.RAW;
	}

//	public Message(final String body) {
//		super();
//		this.parameters = FXCollections.observableArrayList();
//		setRawBody(body);
////		this.bodyType = BodyType.RAW;
//	}

	public Message(final String body, final String bodyType) {
		super();
		this.parameters = FXCollections.observableArrayList();
		setRawBody(body);
//		this.bodyType = bodyType == null ? BodyType.RAW : BodyType.valueOf(bodyType);
	}

	public String getRawBody() {

		final Optional<String> body = parameters.stream()
				.filter(p -> p.getLocation().equals(Location.BODY.name()) && p.getName() == null)
				.map(p -> p.getValue())
				.findFirst();

		return body.orElse(null);
	}

	public void setRawBody(final String value) {

		final Optional<Parameter> rawBody = parameters.stream()
				.filter(p -> p.getLocation().equals(Location.BODY.name()))
				.filter(p -> p.getType().equals(Type.TEXT.name()))
				.filter(p -> p.getName() == null)
				.findFirst();
		if (rawBody.isPresent()) {
			rawBody.get().setValue(value);
		}
		else {
			final Parameter parameter = new Parameter(Boolean.TRUE, Type.TEXT, Location.BODY, null, value);
			addParameter(parameter);
		}
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void addParameter(final Parameter parameter) {
		parameters.add(parameter);
	}

	public Optional<Parameter> findParameter(final Location location, final String name) {

		System.out.println("try to find parameter " + location + ", " + name);

		return parameters.stream()
				.filter(p -> p.isHeaderParameter() && p.getName().equalsIgnoreCase(name))
				.findFirst();
	}
}
