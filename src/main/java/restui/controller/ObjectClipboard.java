package restui.controller;

import java.util.List;

import restui.model.Parameter;

public class ObjectClipboard {

	public List<Parameter> parameters;

	private ObjectClipboard() {
	}

	private static class SingletonHolder {
		private final static ObjectClipboard objectClipboard = new ObjectClipboard();
	}

	public static ObjectClipboard getInstance() {
		return SingletonHolder.objectClipboard;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}
	
	public void setParameters(final List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
}
