package restui.controller;

import java.util.ArrayList;
import java.util.List;

import restui.model.Parameter;

public class ObjectClipboard {

	private final List<Parameter> parameters;

	private ObjectClipboard() {
		parameters = new ArrayList<>();
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
		
		this.parameters.clear();
		this.parameters.addAll(parameters);
	}
	
}
