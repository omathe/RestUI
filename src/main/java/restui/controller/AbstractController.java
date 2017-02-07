package restui.controller;

import javafx.scene.control.TreeItem;
import restui.model.Item;

public class AbstractController {

	protected TreeItem<Item> treeItem;
	
	public void setTreeItem(final TreeItem<Item> treeItem) {
		this.treeItem = treeItem;
	}
	
}
