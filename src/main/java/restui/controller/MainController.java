package restui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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

	@FXML
	private TreeView<Item> treeView;

	@FXML
	private TextField searchItem;

	@FXML
	private VBox vBox;

	@FXML
	private Label memory;

	@FXML
	private Label time;

	@FXML
	private Label style;

	@FXML
	private Label file;

	@FXML
	private BorderPane borderPane;

	private ProjectController projectController;
	private EndPointController endPointController;
	private Application application;
	private File projectFile;

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		application = ApplicationService.openApplication();

		loadProject(application.getLastProjectUri());

		if (application.getStyleFile() != null) {
			setStyle(application.getStyleFile());
			style.setText(application.getStyleName());
		}

		treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		treeView.setEditable(true);
		treeView.setCellFactory(new Callback<TreeView<Item>, TreeCell<Item>>() {
			@Override
			public TreeCell<Item> call(final TreeView<Item> param) {
				return new TreeCellFactory(treeView);
			}
		});

		treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Item>>() {

			@Override
			public void changed(final ObservableValue<? extends TreeItem<Item>> observable,
					final TreeItem<Item> oldValue, final TreeItem<Item> newValue) {

				if (newValue != null) {
					if (newValue.getValue() instanceof Project) {

						final FXMLLoader fxmlLoader = new FXMLLoader();
						try {
							final HBox hBox = fxmlLoader.load(MainController.class.getResource("/fxml/project.fxml").openStream());
							hBox.setAlignment(Pos.TOP_LEFT);
							projectController = (ProjectController) fxmlLoader.getController();
							projectController.setTreeItem(newValue);

							VBox.setVgrow(hBox, Priority.ALWAYS); // webView fill height
							vBox.getChildren().clear();
							vBox.getChildren().add(hBox);
						} catch (final IOException e) {
							e.printStackTrace();
						}
					} else if (newValue.getValue() instanceof Endpoint) {
						final FXMLLoader fxmlLoader = new FXMLLoader();
						try {
							final HBox hBox = fxmlLoader.load(MainController.class.getResource("/fxml/endpoint.fxml").openStream());
							endPointController = (EndPointController) fxmlLoader.getController();
							endPointController.setTreeItem(newValue);
							vBox.getChildren().clear();
							vBox.getChildren().add(hBox);
						} catch (final IOException e) {
							e.printStackTrace();
						}
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

		searchItem.textProperty().addListener((observable, oldItem, newItem) -> {
			if (!newItem.isEmpty() && treeView.getRoot() != null) {
				treeView.getSelectionModel().clearSelection();
				final List<TreeItem<Item>> search = findChildren(treeView.getRoot(), newItem);
				search.stream().forEach(item -> {
					treeView.getSelectionModel().select(item);
				});
			}
		});

	}

	@FXML
	protected void newProject(final ActionEvent event) {

		final Project project = new Project(null, "New project", "");
		final TreeItem<Item> projectItem = new TreeItem<>(project);
		treeView.setRoot(projectItem);

		projectFile = null;
		file.setText("");
	}

	@FXML
	protected void open(final ActionEvent event) {

		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open a project");

		final File initialDirectory = projectFile == null ? new File(ApplicationService.getApplicationHome()) : projectFile.getParentFile();
		fileChooser.setInitialDirectory(initialDirectory);

		final File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			loadProject(file.toURI().toString());
		}
	}

	private void loadProject(final String uri) {

		try {
			final Project project = ProjectService.openProject(uri);
			if (project != null) {
				final TreeItem<Item> projectItem = new TreeItem<>(project);
				builTree(projectItem);
				treeView.setRoot(projectItem);
				projectItem.setExpanded(true);
				projectFile = new File(URI.create(uri));
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

				final File initialDirectory = Strings.isNullOrEmpty(application.getLastProjectUri()) ? new File(ApplicationService.getApplicationHome())
						: new File(URI.create(application.getLastProjectUri())).getParentFile();
				fileChooser.setInitialDirectory(initialDirectory);
				fileChooser.setInitialFileName(project.getName() + ".xml");

				final File file = fileChooser.showSaveDialog(borderPane.getScene().getWindow());
				if (file != null) {
					projectFile = file.getName().endsWith(".xml") ? file : new File(file.getAbsolutePath() + ".xml");
				}
			}
			if (projectFile != null) {
				ProjectService.saveProject(project, projectFile);
				application.setLastProjectUri(projectFile.toURI().toString());
			}
		}
	}

	@FXML
	protected void saveAs(final ActionEvent event) {

		if (projectFile != null) {
			final FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save the project as");

			final File initialDirectory = Strings.isNullOrEmpty(application.getLastProjectUri()) ? new File(ApplicationService.getApplicationHome())
					: new File(URI.create(application.getLastProjectUri())).getParentFile();
			fileChooser.setInitialDirectory(initialDirectory);
			fileChooser.setInitialFileName("projectCopy.xml");

			final File file = fileChooser.showSaveDialog(borderPane.getScene().getWindow());
			if (file != null) {
				try {
					Files.copy(projectFile.toPath(), file.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
					loadProject(file.toURI().toString());
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

	@FXML
	protected void openStyle(final ActionEvent event) {

		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select the style sheet file");
		fileChooser.setInitialDirectory(new File(ApplicationService.getApplicationHome()));
		final File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			setStyle(file.toURI().toString());
			application.setStyleFile(file.toURI().toString());
		}
	}

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
	protected void exit(final ActionEvent event) {

		ApplicationService.saveApplication(application);
		Platform.exit();
	}

	private void setStyle(final String uri) {

		borderPane.getStylesheets().clear();
		borderPane.getStylesheets().add(uri);
		application.setStyleFile(uri);
		style.setText(application.getStyleName());
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

	public List<TreeItem<Item>> findChildren(final TreeItem<Item> parent, final String name) {

		return flattened(parent)
				.filter(ti -> ti.getValue().getName().toLowerCase().contains(name.toLowerCase()))
				.filter(ti -> ti.getValue().getClass().equals(Endpoint.class))
				.collect(Collectors.toList());
	}

	public Optional<TreeItem<Item>> findChild(final TreeItem<Item> parent, final String name) {

		return flattened(parent)
				.filter(ti -> ti.getValue().getName().toLowerCase().contains(name.toLowerCase()))
				.filter(ti -> ti.getValue().getClass().equals(Endpoint.class))
				.findFirst();
	}

	private static Stream<TreeItem<Item>> flattened(final TreeItem<Item> parent) {

		return Stream.concat(
				Stream.of(parent),
				parent.getChildren().stream().flatMap(a -> MainController.flattened(a)));
	}

}
