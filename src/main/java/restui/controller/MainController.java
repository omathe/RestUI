package restui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
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
import restui.controller.cellFactory.TreeCellFactory;
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
	private BorderPane borderPane;

	private ProjectController projectController;
	private EndPointController endPointController;
	private Application application;

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

		ApplicationService.createApplicationDefaultSettings();
		
		ApplicationService.createApplicationDirectory();
		application = ApplicationService.openApplication();

		if (application.getCurrentProject() != null) {
			final Project project = ProjectService.openProject(application.getCurrentProject());
			final TreeItem<Item> projectItem = new TreeItem<>(project);
			builTree(projectItem);
			treeView.setRoot(projectItem);
			projectItem.setExpanded(true);
		}
		
		if (application.getStyleFile() != null) {
			setStyle(application.getStyleFile());
		}

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
							final HBox hBox = fxmlLoader.load(MainController.class.getResource("/project.fxml").openStream());
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
							final HBox hBox = fxmlLoader.load(MainController.class.getResource("/endpoint.fxml").openStream());
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
		
		searchItem.textProperty().addListener((observable, oldValue, newValue) -> {
		    System.out.println("textfield changed from " + oldValue + " to " + newValue);
		});
		
		final Optional<TreeItem<Item>> search = findChild(treeView.getRoot(), "createCustomer");
		System.out.println("search found ? : " + search.isPresent());
		
		final Stream<TreeItem<Item>> flattened = MainController.flattened(treeView.getRoot());
		System.out.println("flattened count = " + flattened.count());
		
		treeView.getSelectionModel().select(search.get());
	}

	@FXML
	protected void exit(final ActionEvent event) {

		Platform.exit();
	}

	@FXML
	protected void newProject(final ActionEvent event) {

		final Project project = new Project(null, "Project 1", "");
		final TreeItem<Item> projectItem = new TreeItem<>(project);
		treeView.setRoot(projectItem);
	}

	@FXML
	protected void save(final ActionEvent event) {

		if (treeView.getRoot() != null) {
			final Project project = (Project) treeView.getRoot().getValue();
			ProjectService.saveProject(project);
		}
	}

	@FXML
	protected void open(final ActionEvent event) {

		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select the project file");
		fileChooser.setInitialDirectory(new File(ApplicationService.getHomeDirectory()));
		final File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			final Project project = ProjectService.openProject(file.getAbsoluteFile().getAbsolutePath());
			final TreeItem<Item> projectItem = new TreeItem<>(project);

			builTree(projectItem);
			treeView.setRoot(projectItem);
		}
	}
	
	@FXML
	protected void openStyle(final ActionEvent event) {
		
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select the style sheet file");
		fileChooser.setInitialDirectory(new File(ApplicationService.getHomeDirectory()));
		final File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			setStyle(file.toURI().toString());
		}
	}
	
	private void setStyle(final String uri) {
		
		borderPane.getStylesheets().clear();
		borderPane.getStylesheets().add(uri);
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
	
	public Optional<TreeItem<Item>> findChild(final TreeItem<Item> parent, final String name) {
		
		
		return flattened(parent).filter(ti -> ti.getValue().getName().equals(name)).findFirst();
	}
	
	public static Stream<TreeItem<Item>> flattened(final TreeItem<Item> parent) {
		
		return Stream.concat(
				Stream.of(parent),
				parent.getChildren().stream().flatMap(a -> MainController.flattened(a)));
	}

}
