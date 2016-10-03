package restui.controller;

import javafx.scene.control.TreeItem;
import restui.model.Item;

public class AbstractController {

	protected TreeItem<Item> treeItem;
	
	public void setTreeItem(TreeItem<Item> treeItem) {
		this.treeItem = treeItem;
		System.out.println("construct AbstractController ");
	}
	
}
