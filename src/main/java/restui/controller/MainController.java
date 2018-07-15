package restui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;
import restui.commons.Strings;
import restui.controller.cellFactory.TreeCellFactory;
import restui.exception.NotFoundException;
import restui.model.Application;
import restui.model.Endpoint;
import restui.model.Item;
import restui.model.Project;
import restui.service.ApplicationService;
import restui.service.ProjectService;

public class MainController implements Initializable {

	private Map<String, Node> centerNodes = new HashMap<String, Node>();
	private WebView webView;
	private WebEngine webEngine;

	@FXML
	private TreeView<Item> treeView;

	@FXML
	private ComboBox<String> searchItem;

	@FXML
	private VBox vBox;

	@FXML
	private Label memory;

	@FXML
	private Label time;

	@FXML
	private Label file;

	@FXML
	private Label searchCount;

	@FXML
	private BorderPane borderPane;

	@FXML
	private TabPane topTabPane;

	@FXML
	private ComboBox<String> url;

	@FXML
	private TextField authorizationHeader;

	private ProjectController projectController;
	private EndPointController endPointController;
	private Application application;
	private File projectFile;
	private Set<String> bookmarks;
	TreeCellFactory treeCellFactory;
	private HBox projectHbox;
	private HBox endpointHbox;

	int index = 0;

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		// initialization of the application
		ApplicationService.init();

		application = ApplicationService.openApplication();

		loadProject(URI.create(application.getLastProjectUri()));

		if (application.getStyleFile() != null) {
			setStyle(application.getStyleFile());
		}

		bookmarks = new HashSet<>();

		treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		treeView.setEditable(true);

		treeView.setCellFactory(new Callback<TreeView<Item>, TreeCell<Item>>() {

			@Override
			public TreeCell<Item> call(final TreeView<Item> param) {
				return new TreeCellFactory(treeView, bookmarks);
			}
		});

		treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Item>>() {

			@Override
			public void changed(final ObservableValue<? extends TreeItem<Item>> observable, final TreeItem<Item> oldValue, final TreeItem<Item> newValue) {

				if (newValue != null) {
					if (newValue.getValue() instanceof Project) {

						// Project
						if (projectHbox == null) {
							try {
								final FXMLLoader fxmlLoader = new FXMLLoader();
								projectHbox = fxmlLoader.load(MainController.class.getResource("/fxml/project.fxml").openStream());
								projectHbox.setAlignment(Pos.TOP_LEFT);
								projectController = (ProjectController) fxmlLoader.getController();
							} catch (final IOException e) {
								e.printStackTrace();
							}
						}
						projectController.setTreeItem(newValue);
						VBox.setVgrow(projectHbox, Priority.ALWAYS); // webView fill height
						vBox.getChildren().clear();
						vBox.getChildren().add(projectHbox);
					} else if (newValue.getValue() instanceof Endpoint) {

						// Endpoint
						try {
							final FXMLLoader fxmlLoader = new FXMLLoader();
							endpointHbox = fxmlLoader.load(MainController.class.getResource("/fxml/endpoint.fxml").openStream());
							endPointController = (EndPointController) fxmlLoader.getController();
						} catch (final IOException e) {
							e.printStackTrace();
						}
						endPointController.setTreeView(treeView);
						endPointController.setTreeItem(newValue);

						vBox.getChildren().clear();
						vBox.getChildren().add(endpointHbox);
					}
				}
			}
		});

		// time
		final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), event -> {
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
			time.setText(simpleDateFormat.format(Instant.now().toEpochMilli()));
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

		// memory usage
		final Timeline timelineMemory = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
			final Double mem = (double) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1_000_000);
			memory.setText(mem.toString() + " Mo");
		}));
		timelineMemory.setCycleCount(Animation.INDEFINITE);
		timelineMemory.play();

		// searching for endpoints
		searchItem.getEditor().textProperty().addListener((observable, oldItem, newItem) -> {

			if (!newItem.isEmpty() && treeView.getRoot() != null) {
				treeView.getSelectionModel().clearSelection();
				final List<TreeItem<Item>> search = findChildren(treeView.getRoot(), newItem, false);
				searchCount.setText(String.valueOf(search == null ? 0 : search.size()));
				search.stream().forEach(item -> {
					treeView.getSelectionModel().select(item);
					treeView.scrollTo(treeView.getSelectionModel().getSelectedIndices().get(0));
				});
			} else {
				treeView.getSelectionModel().clearSelection();
			}
		});

		searchItem.setOnMouseClicked(mouseEvent -> {
			if (mouseEvent.getClickCount() == 2) {
			} else {
				searchItem.getItems().clear();
				final List<TreeItem<Item>> endpoints = collectEndpoints();
				endpoints.stream().forEach(e -> searchItem.getItems().add(e.getValue().getName()));
				for (final String bookmark : bookmarks) {
					searchItem.getItems().add(0, bookmark);
				}
				if (bookmarks.size() > 0) {
					searchItem.getItems().add(bookmarks.size(), "____________________________");
				}
			}
		});

		searchItem.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(final KeyEvent ke) {

				if (ke.getCode() == KeyCode.ENTER) {
					if (treeView.getRoot() != null) {
						treeView.getSelectionModel().clearSelection();
						final List<TreeItem<Item>> search = findChildren(treeView.getRoot(), searchItem.getValue(), true);

						searchCount.setText(String.valueOf(search == null ? 0 : search.size()));
						search.stream().forEach(item -> {
							treeView.getSelectionModel().select(item);
						});
						treeView.scrollTo(treeView.getSelectionModel().getSelectedIndices().get(0));
					} else {
						treeView.getSelectionModel().clearSelection();
					}
				} else {

				}
			}
		});

		// topTabPane
		topTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> ov, Tab oldValue, Tab newValue) {
				// store previous center node used by borderPane
				centerNodes.put(oldValue.getId(), borderPane.getCenter());

				// set borderPane center node
				borderPane.setCenter(getCenterNode(newValue.getId()));
			}
		});

		centerNodes.put("projectTab", borderPane.getCenter());
		centerNodes.put("editTab", borderPane.getCenter());
		centerNodes.put("styleTab", borderPane.getCenter());
		centerNodes.put("settingsTab", borderPane.getCenter());
	}

	private Node getCenterNode(String tabId) {

		Node center = centerNodes.get(tabId);

		if (center == null) {
			if (tabId.equals("webTab")) {
				getWebEngine().load("https://www.qwant.com/?l=fr");
				center = webView;
			}
		}
		return center;
	}

	private WebView getWebView() {
		if (webView == null) {
			webView = new WebView();
		}
		return webView;
	}

	private WebEngine getWebEngine() {
		if (webEngine == null) {
			webEngine = getWebView().getEngine();
		}
		return webEngine;
	}

	@FXML
	protected void newProject(final ActionEvent event) {

		final ButtonData choice = confirmSaveProject();
		if (choice.equals(ButtonData.YES)) {
			save(null);
		}

		final Project project = new Project("New project");
		final TreeItem<Item> projectItem = new TreeItem<>(project);
		treeView.setRoot(projectItem);

		projectFile = null;
		file.setText("");
	}

	@FXML
	protected void open(final ActionEvent event) {

		final ButtonData choice = confirmSaveProject();
		if (choice.equals(ButtonData.YES)) {
			save(null);
		}

		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open a project");

		final File initialDirectory = projectFile == null ? new File(ApplicationService.getApplicationHome()) : projectFile.getParentFile();
		fileChooser.setInitialDirectory(initialDirectory);

		final File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			loadProject(file.toURI());
		}
	}

	private void loadProject(final URI uri) {

		try {
			final Project project = ProjectService.openProject(uri);
			if (project != null) {
				final TreeItem<Item> projectItem = new TreeItem<>(project);
				builTree(projectItem);
				treeView.setRoot(projectItem);

				sort(projectItem);

				projectItem.setExpanded(true);
				projectFile = new File(uri);
				file.setText(projectFile.getAbsolutePath());
				application.setLastProjectUri(uri.toString());
			}
		} catch (final NotFoundException e) {
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Initialization error");
			alert.setHeaderText("Project not found");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	protected void save(final ActionEvent event) {

		if (treeView.getRoot() != null) {
			final Project project = (Project) treeView.getRoot().getValue();

			if (projectFile == null) {
				final FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save the project " + project.getName());

				final File initialDirectory = Strings.isNullOrEmpty(application.getLastProjectUri()) ? new File(ApplicationService.getApplicationHome()) : new File(URI.create(application.getLastProjectUri())).getParentFile();
				fileChooser.setInitialDirectory(initialDirectory);
				fileChooser.setInitialFileName(project.getName() + ".xml");

				final File file = fileChooser.showSaveDialog(borderPane.getScene().getWindow());
				if (file != null) {
					projectFile = file.getName().endsWith(".xml") ? file : new File(file.getAbsolutePath() + ".xml");
				}
			}
			if (projectFile != null) {
				ProjectService.saveProject(project, projectFile.toURI());
				application.setLastProjectUri(projectFile.toURI().toString());
				file.setText(projectFile.getAbsolutePath());
			}
		}
	}

	@FXML
	protected void saveAs(final ActionEvent event) {

		if (projectFile != null) {
			final FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save the project as");

			final File initialDirectory = Strings.isNullOrEmpty(application.getLastProjectUri()) ? new File(ApplicationService.getApplicationHome()) : new File(URI.create(application.getLastProjectUri())).getParentFile();
			fileChooser.setInitialDirectory(initialDirectory);
			fileChooser.setInitialFileName("projectCopy.xml");

			final File file = fileChooser.showSaveDialog(borderPane.getScene().getWindow());
			if (file != null) {
				try {
					Files.copy(projectFile.toPath(), file.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
					loadProject(file.toURI());
				} catch (final IOException e) {
					final Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Save as project");
					alert.setHeaderText("Copy error");
					alert.setContentText(e.getMessage());
					alert.showAndWait();
				}
			}
		}
	}

	// @FXML
	// protected void openStyle(final ActionEvent event) {
	//
	// final FileChooser fileChooser = new FileChooser();
	// fileChooser.setTitle("Select the style sheet file");
	// fileChooser.setInitialDirectory(new
	// File(ApplicationService.getApplicationHome()));
	// final File file = fileChooser.showOpenDialog(null);
	// if (file != null) {
	// setStyle(file.toURI().toString());
	// application.setStyleFile(file.toURI().toString());
	// }
	// }

	@FXML
	protected void delete(final ActionEvent event) {

		final Alert alert = new Alert(AlertType.CONFIRMATION);

		alert.setTitle("Delete the project");
		alert.setHeaderText("Confirm your choice");
		final Project project = (Project) treeView.getRoot().getValue();
		alert.setContentText("Are you sure \nto delete the project " + project.getName() + " ?\n\n");
		final ButtonType yesButton = new ButtonType("Yes");
		final ButtonType noButton = new ButtonType("No");

		alert.getButtonTypes().setAll(noButton, yesButton);

		final Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == yesButton) {
			if (projectFile != null) {
				projectFile.delete();
			}
			projectFile = null;
			treeView.setRoot(null);
		}
	}

	@FXML
	public void exit(final ActionEvent event) {

		ApplicationService.saveApplication(application);

		final ButtonData choice = confirmSaveProject();
		if (choice.equals(ButtonData.YES)) {
			save(null);
		}
		if (!choice.equals(ButtonData.CANCEL_CLOSE)) {
			Platform.exit();
		}
	}

	@FXML
	public void launchWebPage(final ActionEvent event) {

		getWebEngine().load(url.getValue());
		borderPane.setCenter(webView);
	}

	@FXML
	protected void collapse(final ActionEvent event) {

		collapseTreeView(treeView.getRoot());
	}

	private void collapseTreeView(final TreeItem<?> item) {
		if (item != null && !item.isLeaf()) {
			item.setExpanded(false);
			for (final TreeItem<?> child : item.getChildren()) {
				collapseTreeView(child);
			}
		}
	}

	private void setStyle(final String uri) {

		borderPane.getStylesheets().clear();
		borderPane.getStylesheets().add(uri);
		application.setStyleFile(uri);
	}

	private void builTree(final TreeItem<Item> parent) {

		final Item currentItem = parent.getValue();
		for (final Item childItem : currentItem.getChildren()) {
			final TreeItem<Item> childTreeItem = new TreeItem<>(childItem);
			childTreeItem.setExpanded(true);
			parent.getChildren().add(childTreeItem);
			builTree(childTreeItem);
		}
	}

	public List<TreeItem<Item>> collectEndpoints() {

		return treeView.getRoot() == null ? new ArrayList<>()
				: flattened(treeView.getRoot()).filter(ti -> ti.getValue().getClass().equals(Endpoint.class)).sorted((ti1, ti2) -> ti1.getValue().getName().compareTo(ti2.getValue().getName())).collect(Collectors.toList());
	}

	public List<TreeItem<Item>> findChildren(final TreeItem<Item> parent, final String name, final boolean contains) {

		final Predicate<TreeItem<Item>> predicate = contains ? ti -> ti.getValue().getName().toLowerCase().contains(name.toLowerCase()) : ti -> ti.getValue().getName().toLowerCase().equals(name.toLowerCase());
		return parent == null ? null : flattened(parent).filter(predicate).filter(ti -> ti.getValue().getClass().equals(Endpoint.class)).collect(Collectors.toList());
	}

	public Optional<TreeItem<Item>> findChild(final TreeItem<Item> parent, final String name) {

		return flattened(parent).filter(ti -> ti.getValue().getName().toLowerCase().contains(name.toLowerCase())).filter(ti -> ti.getValue().getClass().equals(Endpoint.class)).findFirst();
	}

	private static Stream<TreeItem<Item>> flattened(final TreeItem<Item> parent) {

		return Stream.concat(Stream.of(parent), parent.getChildren().stream().flatMap(a -> MainController.flattened(a)));
	}

	private ButtonData confirmSaveProject() {

		ButtonData buttonData = ButtonData.OTHER;

		if (treeView.getRoot() != null) {
			final Project project = (Project) treeView.getRoot().getValue();
			final Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Save the project");
			alert.setHeaderText("Do you want to save the project\n" + project.getName() + " ?");
			final ButtonType yesButton = new ButtonType("Yes", ButtonData.YES);
			final ButtonType noButton = new ButtonType("No", ButtonData.NO);
			final ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);
			final Optional<ButtonType> result = alert.showAndWait();
			buttonData = result.get().getButtonData();
		}
		return buttonData;
	}

	private void sort(final TreeItem<Item> parent) {

		parent.getChildren().sort(TreeCellFactory.comparator);
		for (final TreeItem<Item> child : parent.getChildren()) {
			sort(child);
		}
	}

	@FXML
	protected void closeWebView(final ActionEvent event) {

		getWebEngine().load(null);
		webEngine = null;
	}

	@FXML
	protected void setAuthorizationHeader(final ActionEvent event) {

		final Item item = treeView.getRoot().getValue();
		if (item != null && !authorizationHeader.getText().isEmpty()) {
			setItemsAuthorizationHeader(item, authorizationHeader.getText());
		}
	}

	private void setItemsAuthorizationHeader(final Item parent, String value) {

		for (final Item child : parent.getChildren()) {
			if (child instanceof Endpoint) {
				final Endpoint endpoint = (Endpoint) child;
				endpoint.getExchanges().stream().forEach(exchange -> {
					// final List<Parameter> parameters = exchange.findParameters(Parameter.Location.HEADER.name(), "Authorization"); FIXME 2.0
					/*  FIXME 2.0 if (parameters != null && !parameters.isEmpty() && parameters.size() == 1) {
						parameters.get(0).setValue(value);
					}*/
				});
			} else {
				setItemsAuthorizationHeader(child, value);
			}
		}
	}

}
