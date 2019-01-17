package restui.model;

import java.util.ArrayList;
import java.util.List;

public class Project extends Item {

	private static final long serialVersionUID = 1L;

	private final List<Endpoint> endpoints;

	public Project(final String name) {
		super(null, name);
		this.endpoints = new ArrayList<>();
	}
	
	public void addEnpoint(final Endpoint endpoint) {
		this.endpoints.add(endpoint);
	}
	
	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

}
