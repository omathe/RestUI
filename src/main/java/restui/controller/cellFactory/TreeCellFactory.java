package restui.controller.cellFactory;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import restui.model.Endpoint;
import restui.model.Item;
import restui.model.Path;
import restui.model.Project;

public class TreeCellFactory extends TextFieldTreeCell<Item> {

	private TextField textField;
	private final ContextMenu addMenu = new ContextMenu();
	private final MenuItem menuItemPath;
	private final MenuItem menuItemEndpoint;
	private final MenuItem menuDeleteItem;
	private final MenuItem menuAddBookmarkItem;
	private final MenuItem menuRemoveBookmarkItem;
	private TreeItem<Item> treeItemToMove;
	private final Node projectImageView;
	private final Node pathImageView;
	private final Node endpointImageView;
	private final Set<String> bookmarks;

	private static Comparator<TreeItem<Item>> itemName = (ti1, ti2) -> {
		if (ti1.getParent() == ti2.getParent()) {
			return ti1.getValue().getName().compareTo(ti2.getValue().getName());
		} else {
			return -1;
		}
	};

	private static Comparator<TreeItem<Item>> endpointType = (ti1, ti2) -> {
		return ti1.getValue().getClass().getName().compareTo(ti2.getValue().getClass().getName());
	};

	public static Comparator<TreeItem<Item>> comparator = (ti1, ti2) -> {
		return endpointType.thenComparing(itemName).compare(ti1, ti2);
	};

	@SuppressWarnings("unchecked")
	public TreeCellFactory(final TreeView<Item> treeView, final Set<String> bookmarks) {

		this.bookmarks = bookmarks;
		menuItemPath = new MenuItem("New path");
		menuItemEndpoint = new MenuItem("New endpoint");
		menuDeleteItem = new MenuItem("Delete");
		menuAddBookmarkItem = new MenuItem("Add bookmark");
		menuRemoveBookmarkItem = new MenuItem("Remove bookmark");
		projectImageView = new ImageView();
		pathImageView = new ImageView();
		endpointImageView = new ImageView();
		projectImageView.setId("imageViewProject");
		endpointImageView.setId("imageViewEndpoint");
		pathImageView.setId("imageViewPath");

		menuItemPath.setOnAction(new EventHandler() {
			@Override
			public void handle(final Event t) {
				final Item item = getTreeItem().getValue();
				final Path path = new Path(item, "New path");
				item.addChild(path);
				final TreeItem<Item> newItem = new TreeItem<>(path);
				getTreeItem().getChildren().add(newItem);
				treeView.getSelectionModel().select(newItem);
				getTreeItem().setExpanded(true);
			}
		});

		menuItemEndpoint.setOnAction(new EventHandler() {
			@Override
			public void handle(final Event t) {
				final Item item = getTreeItem().getValue();
				final Endpoint endPoint = new Endpoint(item, "New endpoint", "GET");
				item.addChild(endPoint);
				final TreeItem<Item> newItem = new TreeItem<>(endPoint);
				getTreeItem().getChildren().add(newItem);
				treeView.getSelectionModel().select(newItem);
				getTreeItem().setExpanded(true);
			}
		});

		menuDeleteItem.setOnAction(new EventHandler() {
			@Override
			public void handle(final Event t) {

				final TreeItem<Item> treeItemToDelete = getTreeItem();
				final Item itemToDelete = getTreeItem().getValue();

				final Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete the item");
				alert.setHeaderText("Do you want to delete\n" + itemToDelete.getName());
				final Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					if (treeItemToDelete.getParent() != null) {
						getTreeItem().getParent().getChildren().remove(getTreeItem());
						itemToDelete.getParent().getChildren().remove(itemToDelete);

					} else {
						treeView.setRoot(null);
					}
				}
			}
		});

		menuAddBookmarkItem.setOnAction(new EventHandler() {
			@Override
			public void handle(final Event t) {
				final Item itemToBookmark = treeView.getSelectionModel().getSelectedItem().getValue();
				bookmarks.add(itemToBookmark.getName());
			}
		});
		menuRemoveBookmarkItem.setOnAction(new EventHandler() {
			@Override
			public void handle(final Event t) {
				final Item itemToBookmark = treeView.getSelectionModel().getSelectedItem().getValue();
				bookmarks.remove(itemToBookmark.getName());
			}
		});

		// Drag and drop management

		// (1)
		setOnDragDetected(e -> {
			final Item item = getItem();
			System.out.println("DragDetected " + item);

			final Dragboard db = startDragAndDrop(TransferMode.MOVE);

			/* Put a string on a dragboard */
			final ClipboardContent content = new ClipboardContent();
			content.putString("toto");

			db.setContent(content);

			treeItemToMove = getTreeItem();
			System.out.println("treeItemToMove() " + treeItemToMove);

			e.consume();

		});

		// (2)
		setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(final DragEvent event) {
				if (event.getGestureSource() != event.getTarget()) {
					/*
					 * allow for both copying and moving, whatever user chooses
					 */
					event.acceptTransferModes(TransferMode.MOVE);
					final Item item = getItem();

					// System.out.println("over " + item);
					// System.out.println("getTreeItem() " + getTreeItem());
					// System.out.println("DragOver " + event.getTarget());
					// item.setName(event.getDragboard().getString());
				}
				event.consume();
			}
		});

		// (3)
		setOnDragEntered(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {

				// System.out.println("DragEntered " + event.getTarget());
				System.out.println("DragEntered() " + getTreeItem());

				/* the drag-and-drop gesture entered the target */
				/* show to the user that it is an actual gesture target */

				// target = event.getTarget();
				// if (event.getGestureSource() != event.getTarget() &&
				// event.getDragboard().hasString()) {
				// event.getTarget().setFill(Color.GREEN);
				// }

				event.consume();
			}
		});

		// (4)
		setOnDragExited(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				/* mouse moved away, remove the graphical cues */
				System.out.println("DragExited " + event.getTarget());

				event.consume();
			}
		});

		// (5)
		setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				System.out.println("DragDropped " + getTreeItem());
				// System.out.println("DragDropped source" + event.getSource());
				final Object obj = event.getSource();
				// System.out.println("DragDropped event.getGestureSource()" +
				// event.getGestureSource());
				/*
				 * let the source know whether the string was successfully transferred and used
				 */
				event.setDropCompleted(true);

				event.consume();
			}
		});

		// (6)
		setOnDragDone(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				/* the drag and drop gesture ended */
				/* if the data was successfully moved, clear it */
				System.out.println("DragDone " + event.getTarget());
				event.consume();
			}
		});

		//treeView.setOnMouseClicked(e -> System.out.println("left clic ..."));

		// contextual menu
		treeView.setContextMenu(addMenu);
		addMenu.getItems().add(menuRemoveBookmarkItem);

		treeView.setOnContextMenuRequested(e -> {
			final Item selectedItem = treeView.getSelectionModel().getSelectedItem().getValue();
			if (selectedItem != null) {
				addMenu.getItems().clear();
				if (selectedItem instanceof Project || selectedItem instanceof Path) {
					addMenu.getItems().add(menuItemPath);
					addMenu.getItems().add(menuItemEndpoint);
					addMenu.getItems().add(menuDeleteItem);
					setGraphic(selectedItem instanceof Project ? projectImageView : pathImageView);
				} else if (selectedItem instanceof Endpoint) {
					addMenu.getItems().add(menuDeleteItem);
					if (bookmarks.contains(selectedItem.getName())) {
						addMenu.getItems().add(menuRemoveBookmarkItem);
					} else {
						addMenu.getItems().add(menuAddBookmarkItem);
					}
					setGraphic(endpointImageView);
				}
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
				if (textField != null) {
					textField.setText(getString());
				}
				setText(getString());
				setGraphic(getTreeItem().getGraphic());
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
		textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (textField.isFocused() && !textField.getText().isEmpty()) {
						textField.selectAll();
					}
				}
			});
		});

		textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(final KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					getItem().setName(textField.getText());

					// sort the items
					getTreeItem().getParent().getChildren().sort(comparator);

					commitEdit(getItem());

					if (getItem() instanceof Path) { // renaming all the endpoints path
						Item currentItem = getItem();
						while (currentItem.hasChildren()) {
							for (final Item child : currentItem.getChildren()) {
								if (child instanceof Endpoint) {
									final Endpoint endpoint = (Endpoint) child;
									endpoint.buildPath();
								}
								currentItem = child;
							}
						}
					}
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
