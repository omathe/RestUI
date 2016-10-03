package restui.controller.cellFactory;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import restui.model.EndPoint;
import restui.model.Item;
import restui.model.Path;
import restui.model.Project;

public class TreeCellFactory extends TextFieldTreeCell<Item> {

	private TextField textField;
	private ContextMenu addMenu = new ContextMenu();
	private MenuItem menuItemPath;
	private MenuItem menuItemWs;

	public TreeCellFactory() {

		menuItemPath = new MenuItem("Nouveau chemin");
		menuItemWs = new MenuItem("Nouveau ws");
		// addMenu.getItems().add(menuItemPath);
		// addMenu.getItems().add(menuItemWs);

		menuItemPath.setOnAction(new EventHandler() {
			@Override
			public void handle(final Event t) {
				TreeItem<Item> newItem = new TreeItem<>(new Path("path"));
				getTreeItem().getChildren().add(newItem);
			}
		});

		menuItemWs.setOnAction(new EventHandler() {
			@Override
			public void handle(final Event t) {
				TreeItem<Item> newItem = new TreeItem<>(new EndPoint("ws", "POST"));
				getTreeItem().getChildren().add(newItem);
			}
		});
	}

	@Override
	public void updateItem(final Item item, final boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (textField != null) {
					textField.setText(getString());
				}
				setText(null);
				setGraphic(textField);
			} else {
				setText(getString());
				setGraphic(getTreeItem().getGraphic());
				setContextMenu(addMenu);

				if (item instanceof Project || item instanceof Path) {
					setContextMenu(addMenu);
					addMenu.getItems().add(menuItemPath);
					addMenu.getItems().add(menuItemWs);
				}

//				if (!getTreeItem().isLeaf() && getTreeItem().getParent() != null) {
//					setContextMenu(addMenu);
//				}
			}
		}
	}

	@Override
	public void startEdit() {
		super.startEdit();

		if (textField == null) {
			createTextField();
		}
		setText(null);
		setGraphic(textField);
		textField.selectAll();
	}

	private void createTextField() {

		textField = new TextField(getString());
		textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(final KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					getItem().setName(textField.getText());
					commitEdit(getItem());
				} else if (e.getCode() == KeyCode.ESCAPE) {
					cancelEdit();
				}
			}
		});
	}

	private String getString() {
		return getItem() == null ? "" : getItem().toString();
	}
}
