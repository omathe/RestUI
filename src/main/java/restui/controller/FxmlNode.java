package restui.controller;

import javafx.scene.Node;

public class FxmlNode {

	private Node node;
	private Object controller;

	public FxmlNode(Node node, Object controller) {
		super();
		this.node = node;
		this.controller = controller;
	}

	public Node getNode() {
		return node;
	}

	public Object getController() {
		return controller;
	}
}
