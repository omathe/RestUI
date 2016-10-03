package restui.model;

import java.util.HashSet;
import java.util.Set;

public class Path extends Item {

	private Path parent;
	private Set<Path> children;

	public Path(final String name) {
		super(name);
		this.parent = parent;
	}

	public Path getParent() {
		return parent;
	}
	
	public void setParent(Path parent) {
		this.parent = parent;
	}
	
	public void addChild(Path child) {

		if (children == null) {
			children = new HashSet<>();
		}
		children.add(child);
	}

}
