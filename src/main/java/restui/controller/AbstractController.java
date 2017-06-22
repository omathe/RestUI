package restui.controller;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import restui.model.Item;

public class AbstractController {

	protected TreeView<Item> treeView;
	protected TreeItem<Item> treeItem;

	public void setTreeItem(final TreeItem<Item> treeItem) {
		this.treeItem = treeItem;
	}

	public void setTreeView(final TreeView<Item> treeView) {
		this.treeView = treeView;
	}

}
