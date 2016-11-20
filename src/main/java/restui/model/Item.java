package restui.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.EXTERNAL_PROPERTY, property="type")
public class Item implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String name;
	
	@JsonIgnore
	protected Item parent;
	
	protected Set<Item> children;

	public Item() {
		super();
	}

	public Item(final String name) {
		super();
		this.name = name;
		this.children = new HashSet<>();
	}

	public Item(final String name, final Item parent) {
		super();
		this.name = name;
		this.parent = parent;
		this.children = new HashSet<>();
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Item getParent() {
		return parent;
	}

	public void setParent(final Item parent) {
		this.parent = parent;
	}

	public Set<Item> getChildren() {
		return children;
	}

	public void setChildren(final Set<Item> children) {
		this.children = children;
	}

	public void addChild(final Item child) {

		if (children == null) {
			children = new HashSet<>();
		}
		children.add(child);
		child.setParent(this);
	}
	
	public boolean hasChildren() {
		return children != null && !children.isEmpty();
	}

	@Override
	public String toString() {
		return name;
	}

}
